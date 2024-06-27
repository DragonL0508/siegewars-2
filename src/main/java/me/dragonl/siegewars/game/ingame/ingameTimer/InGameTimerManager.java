package me.dragonl.siegewars.game.ingame.ingameTimer;

import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.scheduler.MCSchedulers;
import io.fairyproject.scheduler.repeat.RepeatPredicate;
import io.fairyproject.scheduler.response.TaskResponse;
import me.dragonl.siegewars.game.ingame.InGameRunTime;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

@InjectableComponent
public class InGameTimerManager {
    private List<TimerMap> timers = new ArrayList<>();

    public InGameTimerManager(InGameRunTime inGameRunTime) {
        registerTimer(new PreparingTimer(35, inGameRunTime));
        registerTimer(new PositionChoosingTimer(30, inGameRunTime));
        registerTimer(new FightingTimer(90, inGameRunTime));
        registerTimer(new RoundEndTimer(5, inGameRunTime));
    }

    public void registerTimer(Timer timer) {
        timers.add(new TimerMap(timer.getID(), timer));
    }

    public Timer registerTimer(Timer timer, int index) {
        timers.add(index, new TimerMap(timer.getID(), timer));
        return timer;
    }

    public void stopAndUnregisterTimer(Timer timer) {
        stopTimer(timer);
        timers.remove(getTimerMap(timer.getID()));
    }

    public TimerMap getTimerMap(String id) {
        AtomicReference<TimerMap> map = new AtomicReference<>();
        timers.forEach(timerMap -> {
            if (Objects.equals(timerMap.getKey(), id)) {
                map.set(timerMap);
            }
        });
        return map.get();
    }

    public List<TimerMap> getTimers() {
        return timers;
    }

    public void startTimer(Timer timer) {
        timer.setIsStop(false);
        timer.runTime();
        CompletableFuture<?> future = MCSchedulers.getGlobalScheduler().scheduleAtFixedRate(() -> {
            if (timer.isStop())
                return TaskResponse.failure("");

            if (timer.getTime() == 0) {
                return TaskResponse.success("");
            }

            if (timer.getTime() > 0)
                timer.setTime(timer.getTime() - 1);

            return TaskResponse.continueTask();
        }, 0, 20, RepeatPredicate.length(Duration.ofSeconds(timer.getTime()))).getFuture();
        future.thenRun(() -> {
            MCSchedulers.getGlobalScheduler().schedule(() -> {
                if (timer.thenUnregister()) {
                    stopAndUnregisterTimer(timer);
                } else {
                    stopTimer(timer);

                    if (!timer.thenStop())
                        startTimer(getNextTimer(timer));
                }
            }, 20);
        });
    }

    public void stopTimer(Timer timer) {
        timer.setIsStop(true);
        timer.reset();
    }

    public void pauseTimer(Timer timer) {
        timer.setIsStop(true);
    }

    public Timer getNextTimer(Timer timer) {
        AtomicReference<TimerMap> timerMap = new AtomicReference<>();
        timers.forEach(tm -> {
            if (tm.getValue() == timer)
                timerMap.set(tm);
        });

        int i = timers.indexOf(timerMap.get());
        if (i == timers.size() - 1)
            return timers.get(timers.size() - 1).getValue();

        return timers.get(i + 1).getValue();
    }

    public String getFormattedTimer(Timer timer) {
        Integer seconds = timer.getTime(), minutes = seconds / 60;
        seconds -= minutes * 60;

        String min = minutes.toString(), sec = seconds.toString();
        if (minutes < 10)
            min = 0 + min;
        if (seconds < 10)
            sec = 0 + sec;

        return min + ":" + sec;
    }

    public Timer getTimerRunningNow() {
        AtomicReference<Timer> timer = new AtomicReference<>();

        timers.forEach(timerMap -> {
            Timer value = timerMap.getValue();
            if (!value.isStop())
                timer.set(value);
        });

        return timer.get();
    }
}
