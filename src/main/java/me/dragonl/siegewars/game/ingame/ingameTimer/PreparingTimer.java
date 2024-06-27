package me.dragonl.siegewars.game.ingame.ingameTimer;

import me.dragonl.siegewars.game.ingame.InGameRunTime;

public class PreparingTimer implements Timer{
    private Integer time;
    private final Integer initialTime;
    private Boolean isStop = true;
    private final InGameRunTime inGameRunTime;
    public PreparingTimer(Integer time, InGameRunTime inGameRunTime){
        this.time = time;
        this.initialTime = time;
        this.inGameRunTime = inGameRunTime;
    }
    @Override
    public String getID() {
        return "preparing";
    }

    @Override
    public String getName() {
        return "雙方備戰";
    }

    @Override
    public Integer getTime() {
        return time;
    }

    @Override
    public void setTime(Integer i) {
        this.time = i;
    }

    @Override
    public Integer getInitialTime() {
        return initialTime;
    }

    @Override
    public void reset() {
        this.time = getInitialTime();
    }

    @Override
    public Boolean isStop() {
        return isStop;
    }

    @Override
    public Boolean thenUnregister() {
        return false;
    }

    @Override
    public Boolean thenStop() {
        return false;
    }

    @Override
    public void setIsStop(Boolean bool) {
        this.isStop = bool;
    }

    @Override
    public void runTime() {
        inGameRunTime.preparingRunTime(this);
    }
}
