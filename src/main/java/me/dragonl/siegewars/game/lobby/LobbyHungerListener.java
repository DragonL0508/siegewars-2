package me.dragonl.siegewars.game.lobby;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import me.dragonl.siegewars.game.GameState;
import me.dragonl.siegewars.game.GameStateManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

@InjectableComponent
@RegisterAsListener
public class LobbyHungerListener implements Listener {
    private final GameStateManager gameStateManager;

    public LobbyHungerListener(GameStateManager gameStateManager) {
        this.gameStateManager = gameStateManager;
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event){
        if(gameStateManager.isCurrentState(GameState.IN_LOBBY)){
            event.setCancelled(true);
        }
    }
}
