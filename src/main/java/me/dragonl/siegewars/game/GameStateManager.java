package me.dragonl.siegewars.game;

import io.fairyproject.container.InjectableComponent;

@InjectableComponent
public class GameStateManager {
    private GameState currentState = GameState.IN_LOBBY;
    public void setCurrentState(GameState state){
        currentState = state;
    }

    public GameState getCurrentState(){
        return this.currentState;
    }

    public boolean isCurrentState(GameState state){
        return this.currentState == state;
    }
}
