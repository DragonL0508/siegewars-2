package me.dragonl.siegewars.game.ingame;

import com.cryptomorin.xseries.messages.ActionBar;
import com.cryptomorin.xseries.messages.Titles;
import io.fairyproject.bootstrap.bukkit.BukkitPlugin;
import io.fairyproject.bukkit.util.BukkitPos;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.scheduler.MCSchedulers;
import io.fairyproject.mc.util.Position;
import io.fairyproject.scheduler.repeat.RepeatPredicate;
import io.fairyproject.scheduler.response.TaskResponse;
import me.dragonl.siegewars.game.GameStateManager;
import me.dragonl.siegewars.game.RoundState;
import me.dragonl.siegewars.game.ingame.ingameTimer.Timer;
import me.dragonl.siegewars.team.Team;
import me.dragonl.siegewars.team.TeamManager;
import me.dragonl.siegewars.yaml.element.MapConfigElement;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pig;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

@InjectableComponent
public class InGameRunTime {
    private final TeamManager teamManager;
    private final GameStateManager gameStateManager;
    Map<UUID, Boolean> preparingPlayers = new HashMap<>();
    Map<UUID, ItemStack[]> savedArmors = new HashMap<>();
    Map<UUID, ItemStack[]> savedInv = new HashMap<>();

    public InGameRunTime(TeamManager teamManager, GameStateManager gameStateManager) {
        this.teamManager = teamManager;
        this.gameStateManager = gameStateManager;
    }

    public Map<UUID, Boolean> getPreparingPlayers() {
        return preparingPlayers;
    }

    public void preparingRunTime(Timer timer) {
        Team A = teamManager.getTeam("A");
        Team B = teamManager.getTeam("B");
        List<UUID> aPlayers = A.getPlayers();
        List<UUID> bPlayers = B.getPlayers();
        aPlayers.forEach(uuid -> preparingPlayers.put(uuid, false));
        bPlayers.forEach(uuid -> preparingPlayers.put(uuid, false));

        CompletableFuture<?> future = MCSchedulers.getGlobalScheduler().scheduleAtFixedRate(() -> {
            long teamAPrepared = aPlayers.stream()
                    .filter(preparingPlayers::get)
                    .count();
            long teamBPrepared = bPlayers.stream()
                    .filter(preparingPlayers::get)
                    .count();
            String teamAString = createPreparedString((int) teamAPrepared, A.getPlayers().size());
            String teamBString = createPreparedString((int) teamBPrepared, B.getPlayers().size());

            if (!preparingPlayers.containsValue(false))
                return TaskResponse.success("");
            else {
                preparingPlayers.forEach((k, v) -> {
                    if (v)
                        Titles.sendTitle(Bukkit.getPlayer(k), 0, 20, 0, "§e玩家備戰中...", "§6" + teamAString + " §7| " + "§b" + teamBString);
                });
            }

            Bukkit.getOnlinePlayers().forEach(p -> {
                if (teamManager.isInTeam(p, teamManager.getTeam("spectator")))
                    Titles.sendTitle(p, 0, 20, 0, "§e玩家備戰中...", "§6" + teamAString + " §7| " + "§b" + teamBString);
                if (timer.getTime() <= 5)
                    p.playSound(p.getLocation(), Sound.NOTE_STICKS, 1, 0.75f);
            });

            return TaskResponse.continueTask();
        }, 0, 1, RepeatPredicate.length(Duration.ofSeconds(timer.getTime()))).getFuture();

        future.thenRun(() -> {
            timer.setTime(0);
            Bukkit.broadcastMessage("§e[系統] §a備戰時間結束!");
        });
    }

    private static String createPreparedString(int count, int max) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < max - count; i++) {
            result.append("♢");
        }
        for (int i = 0; i < count; i++) {
            result.append("♦");
        }
        return result.toString();
    }

    public void positionChoosingRunTime(Timer timer) {
        gameStateManager.setCurrentRoundState(RoundState.POSITION_CHOOSING);

        MapConfigElement map = gameStateManager.getSelectedMap();
        List<Position> defendSpawn = map.getDefendSpawn();
        List<Position> attackSpawn = map.getAttackSpawn();
        savedArmors.clear();
        savedInv.clear();
        PotionEffect invisible = new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 0, false, false);

        Bukkit.getOnlinePlayers().forEach(p -> {
            Random r = new Random();
            if(teamManager.swGetAnotherTeam(p) == gameStateManager.getAttackTeam()){
                p.teleport(BukkitPos.toBukkitLocation(defendSpawn.get(r.nextInt(defendSpawn.size() - 1))));
                p.setGameMode(GameMode.SURVIVAL);
                Titles.sendTitle(p, 10,40,20,"§a人員部屬階段","§e請做好防守準備");
            }
            else if(teamManager.getPlayerTeam(p) == gameStateManager.getAttackTeam()){
                p.teleport(BukkitPos.toBukkitLocation(attackSpawn.get(0)));
                ActionBar.clearActionBar(p);
                ActionBar.sendActionBar(BukkitPlugin.INSTANCE, p, "§a進攻點 §e1 §7| §a左鍵§7前往下一個", 999999);
                p.setGameMode(GameMode.SURVIVAL);
                Titles.sendTitle(p, 10,40,20,"§a人員部屬階段","§e請選擇進攻位置");
                PlayerInventory inventory = p.getInventory();

                ItemStack air = ItemBuilder.of(Material.AIR).build();
                savedArmors.put(p.getUniqueId(), inventory.getArmorContents());
                savedInv.put(p.getUniqueId(), inventory.getContents());
                inventory.setArmorContents(new ItemStack[]{air, air, air, air});
                inventory.clear();

                p.addPotionEffect(invisible);
            }
            else{
                Titles.sendTitle(p, 10,40,20,"§a人員部屬階段","§e遊戲即將開始");
            }
        });

        CompletableFuture<?> future = MCSchedulers.getGlobalScheduler().scheduleAtFixedRate(() -> {

        },0,1,RepeatPredicate.length(Duration.ofSeconds(timer.getTime()))).getFuture();
        future.thenRun(() -> {
            Bukkit.broadcastMessage("Position Choosing Done!");
        });
    }
}
