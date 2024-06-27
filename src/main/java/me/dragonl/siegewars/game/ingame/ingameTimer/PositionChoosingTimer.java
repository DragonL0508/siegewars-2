package me.dragonl.siegewars.game.ingame.ingameTimer;

import me.dragonl.siegewars.game.ingame.InGameRunTime;

public class PositionChoosingTimer implements Timer{
    private Integer time;
    private final Integer initialTime;
    private Boolean isStop = true;
    private final InGameRunTime inGameRunTime;
    public PositionChoosingTimer(Integer time, InGameRunTime inGameRunTime){
        this.time = time;
        this.initialTime = time;
        this.inGameRunTime = inGameRunTime;
    }
    @Override
    public String getID() {
        return "positionChoosing";
    }

    @Override
    public String getName() {
        return "人員部屬";
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
        inGameRunTime.positionChoosingRunTime(this);
    }
}
