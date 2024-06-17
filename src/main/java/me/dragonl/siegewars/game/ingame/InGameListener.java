package me.dragonl.siegewars.game.ingame;

import io.fairyproject.bukkit.events.player.PlayerDamageByPlayerEvent;
import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import me.dragonl.siegewars.game.GameState;
import me.dragonl.siegewars.game.GameStateManager;
import me.dragonl.siegewars.player.NameGetter;
import me.dragonl.siegewars.player.data.PlayerData;
import me.dragonl.siegewars.player.data.PlayerDataManager;
import me.dragonl.siegewars.SoundPlayer;
import me.dragonl.siegewars.team.SiegeWarsTeam;
import me.dragonl.siegewars.team.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.stream.Collectors;

@InjectableComponent
@RegisterAsListener
public class InGameListener implements Listener {
    private final PlayerDataManager playerDataManager;
    private final GameStateManager gameStateManager;
    private final TeamManager teamManager;
    private final SoundPlayer soundPlayer;
    private final NameGetter nameGetter;

    public InGameListener(PlayerDataManager playerDataManager, GameStateManager gameStateManager, TeamManager teamManager, SoundPlayer soundPlayer, NameGetter nameGetter) {
        this.playerDataManager = playerDataManager;
        this.gameStateManager = gameStateManager;
        this.teamManager = teamManager;
        this.soundPlayer = soundPlayer;
        this.nameGetter = nameGetter;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        if(gameStateManager.isCurrentGameState(GameState.IN_GAME)){
            Player player = event.getPlayer();
            if(teamManager.isInTeam(player, teamManager.getTeam("A")) || teamManager.isInTeam(player, teamManager.getTeam("B")))
                return;

            teamManager.joinTeam(player, SiegeWarsTeam.Spectator);
        }
    }

    @EventHandler void onDrop(PlayerDropItemEvent event){
        if(gameStateManager.isCurrentGameState(GameState.IN_GAME)){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(PlayerDamageByPlayerEvent event) {
        if(gameStateManager.isCurrentGameState(GameState.IN_GAME)){
            Player damager = event.getDamager();
            PlayerData damagerData = playerDataManager.getPlayerData(damager);
            damagerData.setTotalDamage(damagerData.getTotalDamage() + event.getFinalDamage());
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        if(gameStateManager.isCurrentGameState(GameState.IN_GAME)){
            if(event.getEntity().getKiller() != null){
                Player victim = event.getEntity().getPlayer();
                Player killer = event.getEntity().getKiller();
                PlayerData killerData = playerDataManager.getPlayerData(killer);
                PlayerData victimData = playerDataManager.getPlayerData(victim);
                killerData.setKills(killerData.getKills() + 1);
                victimData.setDeaths(victimData.getDeaths() + 1);

                //message
                event.setDeathMessage("§c[擊殺] " + nameGetter.getNameWithTeamColor(killer) + " §c✘ " + nameGetter.getNameWithTeamColor(victim));
                victim.spigot().respawn();

                soundPlayer.playSound(Bukkit.getOnlinePlayers().stream().map(Player::getUniqueId).collect(Collectors.toList()), Sound.IRONGOLEM_DEATH,1,1 + (float)Math.random());
            }
        }
    }
}
