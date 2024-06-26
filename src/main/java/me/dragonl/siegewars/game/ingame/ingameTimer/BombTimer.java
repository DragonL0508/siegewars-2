package me.dragonl.siegewars.game.ingame.ingameTimer;

import me.dragonl.siegewars.game.ingame.InGameRunTime;

public class BombTimer implements Timer{
    private Integer time;
    private final Integer initialTime;
    private Boolean isStop = true;
    private final InGameRunTime inGameRunTime;
    public BombTimer(Integer time, InGameRunTime inGameRunTime){
        this.time = time;
        this.initialTime = time;
        this.inGameRunTime = inGameRunTime;
    }
    @Override
    public String getID() {
        return "bombPlanted";
    }

    @Override
    public String getName() {
        return "炸藥已安裝";
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
        return true;
    }

    @Override
    public void setIsStop(Boolean bool) {
        this.isStop = bool;
    }

    @Override
    public void runTime() {
        inGameRunTime.bombPlantedRunTime(this);
    }
}
