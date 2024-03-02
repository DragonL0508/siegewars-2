package me.dragonl.siegewars.game;

import io.fairyproject.container.InjectableComponent;

@InjectableComponent
public class GameStateManager {
    private GameState currentState = GameState.IN_LOBBY;
    private Integer round = 0;
    private Integer ScoreA = 0;
    private Integer ScoreB = 0;

    public Integer getRound() {
        return round;
    }

    public void setRound(Integer round) {
        this.round = round;
    }

    public Integer getScoreA() {
        return ScoreA;
    }

    public void setScoreA(Integer scoreA) {
        ScoreA = scoreA;
    }

    public Integer getScoreB() {
        return ScoreB;
    }

    public void setScoreB(Integer scoreB) {
        ScoreB = scoreB;
    }

    public void setCurrentState(GameState state){
        currentState = state;
    }

    public boolean isCurrentState(GameState state){
        return this.currentState == state;
    }
}
