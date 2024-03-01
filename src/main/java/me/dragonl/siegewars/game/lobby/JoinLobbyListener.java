package me.dragonl.siegewars.game.lobby;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import me.dragonl.siegewars.game.GameState;
import me.dragonl.siegewars.game.GameStateManager;
import me.dragonl.siegewars.player.PlayerJoinListener;
import me.dragonl.siegewars.team.TeamManager;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@RegisterAsListener
@InjectableComponent
public class JoinLobbyListener implements Listener {
    private final GameStateManager gameStateManager;
    private final TeamManager teamManager;

    public JoinLobbyListener(GameStateManager gameStateManager, TeamManager teamManager) {
        this.gameStateManager = gameStateManager;
        this.teamManager = teamManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        if(gameStateManager.isCurrentState(GameState.IN_LOBBY)){
            event.setJoinMessage(ChatColor.YELLOW + event.getPlayer().getName() + "joined the lobby !");
            //Join lobby Team
            teamManager.joinTeam(event.getPlayer(), teamManager.getTeam("lobby"));
        }
    }
}
