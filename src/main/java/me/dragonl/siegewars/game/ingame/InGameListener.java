package me.dragonl.siegewars.game.ingame;

import com.cryptomorin.xseries.messages.ActionBar;
import com.cryptomorin.xseries.messages.Titles;
import io.fairyproject.bootstrap.bukkit.BukkitPlugin;
import io.fairyproject.bukkit.events.player.PlayerDamageByPlayerEvent;
import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.bukkit.util.BukkitPos;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.MCPlayer;
import io.fairyproject.mc.scheduler.MCSchedulers;
import me.dragonl.siegewars.game.*;
import me.dragonl.siegewars.player.NameGetter;
import me.dragonl.siegewars.player.NameTagTemporaryManager;
import me.dragonl.siegewars.player.data.PlayerData;
import me.dragonl.siegewars.player.data.PlayerDataManager;
import me.dragonl.siegewars.SoundPlayer;
import me.dragonl.siegewars.team.SiegeWarsTeam;
import me.dragonl.siegewars.team.TeamManager;
import me.dragonl.siegewars.yaml.element.MapConfigElement;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

import java.util.stream.Collectors;

@InjectableComponent
@RegisterAsListener
public class InGameListener implements Listener {
    private final PlayerDataManager playerDataManager;
    private final GameStateManager gameStateManager;
    private final TeamManager teamManager;
    private final SoundPlayer soundPlayer;
    private final NameGetter nameGetter;
    private final MapObjectDestroyer mapObjectDestroyer;

    public InGameListener(PlayerDataManager playerDataManager, GameStateManager gameStateManager, TeamManager teamManager, SoundPlayer soundPlayer, NameGetter nameGetter, MapObjectDestroyer mapObjectDestroyer) {
        this.playerDataManager = playerDataManager;
        this.gameStateManager = gameStateManager;
        this.teamManager = teamManager;
        this.soundPlayer = soundPlayer;
        this.nameGetter = nameGetter;
        this.mapObjectDestroyer = mapObjectDestroyer;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (gameStateManager.isCurrentGameState(GameState.IN_GAME)) {
            Player player = event.getPlayer();
            if (teamManager.isInTeam(player, teamManager.getTeam("A")) || teamManager.isInTeam(player, teamManager.getTeam("B")))
                return;

            teamManager.joinTeam(player, SiegeWarsTeam.Spectator);
        }
    }

    @EventHandler
    void onDrop(PlayerDropItemEvent event) {
        if (gameStateManager.isCurrentGameState(GameState.IN_GAME)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (gameStateManager.isCurrentGameState(GameState.IN_GAME)) {
            if (event.getEntity().getKiller() != null) {
                Player victim = event.getEntity().getPlayer();
                Player killer = event.getEntity().getKiller();
                PlayerData killerData = playerDataManager.getPlayerData(killer);
                PlayerData victimData = playerDataManager.getPlayerData(victim);
                killerData.setKills(killerData.getKills() + 1);
                victimData.setDeaths(victimData.getDeaths() + 1);

                //message
                event.setDeathMessage("§c[擊殺] " + nameGetter.getNameWithTeamColor(killer) + " §c✘ " + nameGetter.getNameWithTeamColor(victim));
                victim.getWorld().spigot().playEffect(victim.getLocation().add(0, 1, 0), Effect.STEP_SOUND, Material.REDSTONE_BLOCK.getId(), 0, 0.2F, 0.25F, 0.2F, 1, 30, 32);
                victim.setHealth(20);
                victim.setGameMode(GameMode.SPECTATOR);
                Titles.sendTitle(victim,0,30,10,"§c死亡","§7你被擊殺了");

                soundPlayer.playSound(Bukkit.getOnlinePlayers().stream().map(Player::getUniqueId).collect(Collectors.toList()), Sound.IRONGOLEM_DEATH, 1, 1 + (float) Math.random());
            }
        }
    }

    @EventHandler
    public void onBaffleBreak(BlockBreakEvent event) {
        if (gameStateManager.isCurrentGameState(GameState.IN_GAME))
            mapObjectDestroyer.destroyBaffle(event.getBlock().getLocation());
    }

    @EventHandler
    public void onLocChoosingClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        MapConfigElement map = gameStateManager.getSelectedMap();

        if (gameStateManager.isCurrentGameState(GameState.IN_GAME) && gameStateManager.isCurrentRoundState(RoundState.POSITION_CHOOSING)
                && teamManager.isInTeam(player, gameStateManager.getAttackTeam())) {
            if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                //next position
                Location nextAttackSpawn = map.getNextAttackSpawn(player.getLocation());
                ActionBar.clearActionBar(player);
                ActionBar.sendActionBar(BukkitPlugin.INSTANCE, player, "§a進攻點 §e" + (map.getAttackSpawn().indexOf(BukkitPos.toMCPos(nextAttackSpawn)) + 1) + " §7| §a左鍵§7前往下一個");
                player.teleport(nextAttackSpawn);
                player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1, 0.7f);
            }
        }
    }

    @EventHandler
    public void moveOnChoosing(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (gameStateManager.isCurrentGameState(GameState.IN_GAME) && gameStateManager.isCurrentRoundState(RoundState.POSITION_CHOOSING)
                && teamManager.isInTeam(player, gameStateManager.getAttackTeam())) {
            Location from = event.getFrom(), to = event.getTo();
            if (!(from.getBlockX() == to.getBlockX()
                    && from.getBlockY() == to.getBlockY()
                    && from.getBlockZ() == to.getBlockZ())){
                event.setCancelled(true);
                player.teleport(new Location(from.getWorld(), from.getBlockX() + 0.5, from.getBlockY() + 0.5, from.getBlockZ() + 0.5));
            }
        }
    }
}
