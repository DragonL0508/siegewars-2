package me.dragonl.siegewars.game.lobby;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import me.dragonl.siegewars.game.GameState;
import me.dragonl.siegewars.game.GameStateManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

@InjectableComponent
@RegisterAsListener
public class LobbyInteractListener implements Listener {
    private final GameStateManager gameStateManager;

    public LobbyInteractListener(GameStateManager gameStateManager) {
        this.gameStateManager = gameStateManager;
    }

    @EventHandler
    public void playerInteract(PlayerInteractEvent event){
        if(gameStateManager.isCurrentState(GameState.IN_LOBBY)){
            event.setCancelled(true);
        }
    }
}
