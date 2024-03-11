package me.dragonl.siegewars.game;

import io.fairyproject.container.InjectableComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@InjectableComponent
public class GameStateManager {
    private List<UUID> inGamePlayers = new ArrayList<>();
    private GameState currentGameState = GameState.IN_LOBBY;
    private RoundState currentRoundState = RoundState.PREPARING;

    public RoundState getCurrentRoundState() {
        return currentRoundState;
    }

    public void setCurrentRoundState(RoundState currentRoundState) {
        this.currentRoundState = currentRoundState;
    }

    public boolean isCurrentRoundState(RoundState state){
        return this.currentRoundState == state;
    }

    public List<UUID> getInGamePlayers() {
        return inGamePlayers;
    }

    public void setInGamePlayers(List<UUID> inGamePlayers) {
        this.inGamePlayers = inGamePlayers;
    }

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

    public void setCurrentGameState(GameState state){
        currentGameState = state;
    }
    public GameState getCurrentGameState(){
        return this.currentGameState;
    }

    public boolean isCurrentGameState(GameState state){
        return this.currentGameState == state;
    }
}
