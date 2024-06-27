package me.dragonl.siegewars.player;

import io.fairyproject.bukkit.events.player.PlayerDamageByEntityEvent;
import io.fairyproject.bukkit.events.player.PlayerDamageByPlayerEvent;
import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import me.dragonl.siegewars.game.GameState;
import me.dragonl.siegewars.game.GameStateManager;
import me.dragonl.siegewars.game.RoundState;
import me.dragonl.siegewars.player.data.PlayerData;
import me.dragonl.siegewars.player.data.PlayerDataManager;
import me.dragonl.siegewars.team.Team;
import me.dragonl.siegewars.team.TeamManager;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

@RegisterAsListener
@InjectableComponent
public class PlayerFightListener implements Listener {
    private final TeamManager teamManager;
    private final GameStateManager gameStateManager;
    private final NameTagTemporaryManager nameTagTemporaryManager;
    private final PlayerDataManager playerDataManager;

    public PlayerFightListener(TeamManager teamManager, GameStateManager gameStateManager, NameTagTemporaryManager nameTagTemporaryManager, PlayerDataManager playerDataManager) {
        this.teamManager = teamManager;
        this.gameStateManager = gameStateManager;
        this.nameTagTemporaryManager = nameTagTemporaryManager;
        this.playerDataManager = playerDataManager;
    }

    @EventHandler
    public void onPvp(PlayerDamageByPlayerEvent event) {
        if (gameStateManager.isCurrentGameState(GameState.IN_GAME)) {
            if (gameStateManager.isCurrentRoundState(RoundState.FIGHTING) || gameStateManager.isCurrentRoundState(RoundState.ENDING)) {
                Team playerTeam = teamManager.getPlayerTeam(event.getPlayer());

                Player target = event.getPlayer();
                Player player = event.getDamager();

                if (playerTeam.isShowNameTagToClicker()) {
                    nameTagTemporaryManager.startDisplayToPlayer(target, player);
                }

                PlayerData damagerData = playerDataManager.getPlayerData(player);
                damagerData.setTotalDamage(damagerData.getTotalDamage() + event.getFinalDamage());
            } else
                event.setCancelled(true);
        }
    }
}
