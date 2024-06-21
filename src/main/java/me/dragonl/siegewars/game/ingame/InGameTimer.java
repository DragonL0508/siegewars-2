package me.dragonl.siegewars.game.ingame;

import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.scheduler.MCSchedulers;
import io.fairyproject.scheduler.ScheduledTask;
import io.fairyproject.scheduler.response.TaskResponse;

import java.text.DecimalFormat;
import java.text.Format;

@InjectableComponent
public class InGameTimer {
    private Integer timeLeft;
    private Boolean isStop = true;

    public Integer getTimeLeft() {
        return timeLeft;
    }

    public String getFormattedTimer() {
        Integer seconds = timeLeft, minutes = seconds / 60;
        seconds -= minutes * 60;

        String min = minutes.toString(), sec = seconds.toString();
        if(minutes < 10)
            min = 0 + min;
        if(seconds < 10)
            sec = 0 + sec;

        return min + ":" + sec;
    }

    public void setTimeLeft(Integer timeLeft) {
        this.timeLeft = timeLeft;
    }

    public void startTimer() {
        isStop = false;
        ScheduledTask<?> task = MCSchedulers.getGlobalScheduler().scheduleAtFixedRate(() -> {
            if (!isStop && timeLeft > 0)
                timeLeft--;
            else if(isStop)
                return TaskResponse.failure("");

            return TaskResponse.continueTask();
        }, 0, 20);
    }

    public void stopTimer() {
        isStop = true;
    }

    public Boolean getStop() {
        return isStop;
    }

    public void setStop(Boolean stop) {
        isStop = stop;
    }
}
