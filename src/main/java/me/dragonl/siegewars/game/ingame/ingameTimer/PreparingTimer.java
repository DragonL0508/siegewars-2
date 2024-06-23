package me.dragonl.siegewars.game.ingame.ingameTimer;

import javax.persistence.criteria.CriteriaBuilder;

public class PreparingTimer implements Timer{
    private Integer time;
    private final Integer initialTime;
    private Boolean isStop = true;
    public PreparingTimer(Integer time){
        this.time = time;
        this.initialTime = time;
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
    public void setIsStop(Boolean bool) {
        this.isStop = bool;
    }
}
