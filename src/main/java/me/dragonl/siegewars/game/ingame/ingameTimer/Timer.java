package me.dragonl.siegewars.game.ingame.ingameTimer;

import javax.persistence.criteria.CriteriaBuilder;

public interface Timer {
    String getID();
    String getName();
    Integer getTime();
    void setTime(Integer i);
    Integer getInitialTime();
    void reset();
    Boolean isStop();
    Boolean thenUnregister();
    Boolean thenStop();
    void setIsStop(Boolean bool);
    void runTime();
}
