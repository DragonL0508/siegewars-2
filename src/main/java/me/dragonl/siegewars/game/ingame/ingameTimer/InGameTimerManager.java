package me.dragonl.siegewars.game.ingame.ingameTimer;

import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.scheduler.MCSchedulers;
import io.fairyproject.scheduler.repeat.RepeatPredicate;
import io.fairyproject.scheduler.response.TaskResponse;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

@InjectableComponent
public class InGameTimerManager {
    private List<TimerMap> timers = new ArrayList<>();

    public InGameTimerManager() {
        registerTimer(new PreparingTimer(35));
        registerTimer(new PositionChoosingTimer(30));
    }

    public void registerTimer(Timer timer) {
        timers.add(new TimerMap(timer.getID(), timer));
    }

    public List<TimerMap> getTimers() {
        return timers;
    }

    public void startTimer(Timer timer) {
        timer.setIsStop(false);
        CompletableFuture<?> future = MCSchedulers.getGlobalScheduler().scheduleAtFixedRate(() -> {
            if (timer.isStop())
                return TaskResponse.failure("");

            if (timer.getTime() > 0)
                timer.setTime(timer.getTime() - 1);

            return TaskResponse.continueTask();
        }, 0, 20, RepeatPredicate.length(Duration.ofSeconds(timer.getTime()))).getFuture();
        future.thenRun(() -> {

            startTimer(getNextTimer(timer));
            stopTimer(timer);
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
        if(i == timers.size() - 1)
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
