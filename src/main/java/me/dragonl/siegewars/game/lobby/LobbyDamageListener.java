package me.dragonl.siegewars.game.lobby;

import io.fairyproject.bukkit.events.player.PlayerDamageEvent;
import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import me.dragonl.siegewars.game.GameState;
import me.dragonl.siegewars.game.GameStateManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.net.http.WebSocket;

@InjectableComponent
@RegisterAsListener
public class LobbyDamageListener implements Listener {
    private final GameStateManager gameStateManager;

    public LobbyDamageListener(GameStateManager gameStateManager) {
        this.gameStateManager = gameStateManager;
    }

    @EventHandler
    public void playerHurt(PlayerDamageEvent event){
        if(gameStateManager.isCurrentState(GameState.IN_LOBBY)){
            event.setCancelled(true);
        }
    }
}
