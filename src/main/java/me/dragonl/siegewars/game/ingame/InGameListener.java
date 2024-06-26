package me.dragonl.siegewars.game.ingame;

import com.cryptomorin.xseries.messages.ActionBar;
import com.cryptomorin.xseries.messages.Titles;
import io.fairyproject.bootstrap.bukkit.BukkitPlugin;
import io.fairyproject.bukkit.events.player.EntityDamageByPlayerEvent;
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
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleExitEvent;

import java.util.Arrays;
import java.util.List;
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
    private final BombManager bombManager;

    public InGameListener(PlayerDataManager playerDataManager, GameStateManager gameStateManager, TeamManager teamManager, SoundPlayer soundPlayer, NameGetter nameGetter, MapObjectDestroyer mapObjectDestroyer, BombManager bombManager) {
        this.playerDataManager = playerDataManager;
        this.gameStateManager = gameStateManager;
        this.teamManager = teamManager;
        this.soundPlayer = soundPlayer;
        this.nameGetter = nameGetter;
        this.mapObjectDestroyer = mapObjectDestroyer;
        this.bombManager = bombManager;
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
    void onPlacedBlock(BlockPlaceEvent event) {
        if (gameStateManager.isCurrentGameState(GameState.IN_GAME)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    void onBreakBlock(BlockBreakEvent event) {
        if (gameStateManager.isCurrentGameState(GameState.IN_GAME)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    void onHangingBreak(HangingBreakEvent event) {
        if (gameStateManager.isCurrentGameState(GameState.IN_GAME)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    void onHangingPlace(HangingPlaceEvent event) {
        if (gameStateManager.isCurrentGameState(GameState.IN_GAME)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (gameStateManager.isCurrentGameState(GameState.IN_GAME)
                    && isNotAllowed(event.getClickedBlock())) {
                event.setCancelled(true);
            }
        }
    }

    private Boolean isNotAllowed(Block block) {
        List<Material> blocks = Arrays.asList(
                Material.CHEST,
                Material.ENDER_CHEST,
                Material.ENCHANTMENT_TABLE,
                Material.ANVIL,
                Material.WORKBENCH,
                Material.FURNACE,
                Material.BURNING_FURNACE,
                Material.FENCE_GATE,
                Material.BED,
                Material.ACACIA_DOOR,
                Material.DARK_OAK_DOOR,
                Material.BIRCH_DOOR,
                Material.JUNGLE_DOOR,
                Material.WOODEN_DOOR,
                Material.SPRUCE_DOOR,
                Material.FENCE_GATE,
                Material.ACACIA_FENCE_GATE,
                Material.BIRCH_FENCE_GATE,
                Material.JUNGLE_FENCE_GATE,
                Material.SPRUCE_FENCE_GATE,
                Material.DARK_OAK_FENCE_GATE,
                Material.TRAP_DOOR,
                Material.TRAPPED_CHEST,
                Material.WOOD_BUTTON,
                Material.STONE_BUTTON,
                Material.DISPENSER,
                Material.HOPPER,
                Material.LEVER
        );

        for (Material m : blocks) {
            if (block.getType() == m)
                return true;
        }

        return false;
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
                Titles.sendTitle(victim, 0, 30, 10, "§c死亡", "§7你被擊殺了");

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
    public void onLocChoosingInteract(PlayerInteractEvent event) {
        if (gameStateManager.isCurrentGameState(GameState.IN_GAME) && gameStateManager.isCurrentRoundState(RoundState.POSITION_CHOOSING)
                && teamManager.isInTeam(event.getPlayer(), gameStateManager.getAttackTeam())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        //cancel vehicle interaction in game
        if (gameStateManager.isCurrentGameState(GameState.IN_GAME)) {
            Entity entity = event.getRightClicked();
            if (entity.getType() == EntityType.MINECART || entity.getType() == EntityType.BOAT) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerAttackEntity(EntityDamageByPlayerEvent event) {
        if (gameStateManager.isCurrentGameState(GameState.IN_GAME)
                && event.getEntity().getType() != EntityType.PLAYER)
            event.setCancelled(true);
    }

    @EventHandler
    public void onLocChoosingJump(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        MapConfigElement map = gameStateManager.getSelectedMap();

        if (gameStateManager.isCurrentGameState(GameState.IN_GAME) && gameStateManager.isCurrentRoundState(RoundState.POSITION_CHOOSING)
                && teamManager.isInTeam(player, gameStateManager.getAttackTeam())) {
            Location from = event.getFrom(), to = event.getTo();
            if (from.getBlockY() < to.getBlockY()) {
                //next position
                Location nextAttackSpawn = map.getNextAttackSpawn(player.getLocation());
                ActionBar.clearActionBar(player);
                ActionBar.sendActionBar(BukkitPlugin.INSTANCE, player, "§a進攻點 §e" + (map.getAttackSpawn().indexOf(BukkitPos.toMCPos(nextAttackSpawn)) + 1) + " §7| §a跳躍前往下一個");
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
                    && from.getBlockZ() == to.getBlockZ())) {
                player.teleport(new Location(from.getWorld(), from.getBlockX() + 0.5, from.getBlockY() + 0.5, from.getBlockZ() + 0.5));
            }
        }
    }
}
