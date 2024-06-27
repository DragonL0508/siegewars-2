package me.dragonl.siegewars.game.ingame;

import com.cryptomorin.xseries.messages.ActionBar;
import com.cryptomorin.xseries.messages.Titles;
import io.fairyproject.bootstrap.bukkit.BukkitPlugin;
import io.fairyproject.bukkit.util.BukkitPos;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.MCPlayer;
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
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEventSource;
import org.bukkit.*;

import java.sql.Time;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

@InjectableComponent
public class InGameRunTime {
    private final TeamManager teamManager;
    private final GameStateManager gameStateManager;
    private Map<UUID, Boolean> preparingPlayers = new HashMap<>();
    private Location bomblocation = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);

    public void setBomblocation(Location bomblocation) {
        this.bomblocation = bomblocation;
    }

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
            if (timer.isStop())
                return TaskResponse.failure("");

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
                    Titles.sendTitle(p, 0, 40, 0, "§e玩家備戰中...", "§6" + teamAString + " §7| " + "§b" + teamBString);
                if (timer.getTime() <= 5)
                    p.playSound(p.getLocation(), Sound.CLICK, 1, 0.75f);
            });

            return TaskResponse.continueTask();
        }, 0, 20, RepeatPredicate.length(Duration.ofSeconds(timer.getTime()))).getFuture();

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

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "bomb clearBombKeeper");

        Bukkit.getOnlinePlayers().forEach(p -> {
            p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);

            Random r = new Random();
            if (teamManager.swGetAnotherTeam(p) == gameStateManager.getAttackTeam()) {
                p.teleport(BukkitPos.toBukkitLocation(defendSpawn.get(r.nextInt(defendSpawn.size() - 1))));
                p.setGameMode(GameMode.SURVIVAL);
                p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);
                Titles.sendTitle(p, 10, 40, 20, "§a人員部屬階段", "§e請做好防守準備");

            } else if (teamManager.getPlayerTeam(p) == gameStateManager.getAttackTeam()) {
                MCPlayer player = MCPlayer.from(p);
                //create adventure component for clickable message
                player.sendMessage("§7-----------------------------",
                        "§e你可以選擇拿取炸藥包");
                Component component = Component.text("§a[點我拿取]")
                        .hoverEvent(HoverEventSource.unbox(Component.text("§7點擊拿取炸藥包")))
                        .clickEvent(ClickEvent.runCommand("/bomb claim"));
                player.sendMessage(component);
                player.sendMessage("§7-----------------------------");

                p.teleport(BukkitPos.toBukkitLocation(attackSpawn.get(0)));
                ActionBar.clearActionBar(p);
                ActionBar.sendActionBar(BukkitPlugin.INSTANCE, p, "§a進攻點 §e1 §7| §a跳躍前往下一個");
                p.setGameMode(GameMode.SURVIVAL);
                Titles.sendTitle(p, 10, 40, 20, "§a人員部屬階段", "§e請選擇進攻位置");

            } else {
                Titles.sendTitle(p, 10, 40, 20, "§a人員部屬階段", "§e遊戲即將開始");
            }

            if (!teamManager.isInTeam(p, teamManager.getTeam("spectator")))
                gameStateManager.getAttackTeam().getPlayers().forEach(uuid -> {
                    if (p != Bukkit.getPlayer(uuid))
                        p.hidePlayer(Bukkit.getPlayer(uuid));
                });
        });

        CompletableFuture<?> future = MCSchedulers.getGlobalScheduler().scheduleAtFixedRate(() -> {
            if (timer.isStop())
                return TaskResponse.failure("");

            Bukkit.getOnlinePlayers().forEach(p -> {
                if (timer.getTime() <= 5)
                    p.playSound(p.getLocation(), Sound.CLICK, 1, 0.75f);
            });

            return TaskResponse.continueTask();
        }, 0, 20, RepeatPredicate.length(Duration.ofSeconds(timer.getTime()))).getFuture();

        future.thenRun(() -> {
            Bukkit.getOnlinePlayers().forEach(player -> {
                gameStateManager.getAttackTeam().getPlayers().forEach(uuid -> {
                    player.showPlayer(Bukkit.getPlayer(uuid));
                });
            });
        });
    }

    public void fightingRunTime(Timer timer) {
        //make random player in attack team to be bomb keeper if no one claim the bomb
        if (!gameStateManager.getAttackTeam().getPlayers().isEmpty()) {
            List<UUID> attackPlayers = gameStateManager.getAttackTeam().getPlayers();
            if (gameStateManager.getBombClaimer() == null) {
                Random r = new Random();
                if (attackPlayers.size() == 1) {
                    Bukkit.dispatchCommand(Bukkit.getPlayer(attackPlayers.get(0)), "bomb claim");
                } else {
                    Bukkit.dispatchCommand(Bukkit.getPlayer(attackPlayers.get(r.nextInt(attackPlayers.size()))), "bomb claim");
                }
            }
        }

        Bukkit.getOnlinePlayers().forEach(p -> {
            ActionBar.clearActionBar(p);
            Titles.sendTitle(p, 10, 40, 20, "§a戰鬥開始", "§e部屬階段結束");
            p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 1, 1);
        });

        gameStateManager.setCurrentRoundState(RoundState.FIGHTING);

        CompletableFuture<?> future = MCSchedulers.getGlobalScheduler().scheduleAtFixedRate(() -> {
            if (timer.isStop())
                return TaskResponse.failure("");

            return TaskResponse.continueTask();
        }, 0, 1, RepeatPredicate.length(Duration.ofSeconds(timer.getTime()))).getFuture();

        future.thenRun(() -> {
            Bukkit.broadcastMessage("fighting end!");
        });
    }

    public void bombPlantedRunTime(Timer timer) {

        AtomicInteger o = new AtomicInteger();
        CompletableFuture<?> future = MCSchedulers.getGlobalScheduler().scheduleAtFixedRate(() -> {
            if (timer.isStop())
                return TaskResponse.failure("");

            int i;

            if (timer.getTime() > 20)
                i = 30;
            else if (timer.getTime() > 10)
                i = 20;
            else if (timer.getTime() > 5)
                i = 10;
            else
                i = 5;

            if (o.get() >= i) {
                World world = this.bomblocation.getWorld();
                world.playSound(this.bomblocation, Sound.NOTE_PLING, 2.5f, 1.5f);
                world.playSound(this.bomblocation, Sound.NOTE_BASS_DRUM, 2.5f, 0.5f);
                o.set(0);
            }

            o.getAndIncrement();
            return TaskResponse.continueTask();
        }, 0, 1, RepeatPredicate.length(Duration.ofSeconds(timer.getTime()))).getFuture();

        future.thenRun(() -> {
            Bukkit.broadcastMessage("Booom!");
        });
    }

    public void roundEndRunTime(Timer timer) {
        CompletableFuture<?> future = MCSchedulers.getGlobalScheduler().scheduleAtFixedRate(() -> {
            if (timer.isStop())
                return TaskResponse.failure("");

            return TaskResponse.continueTask();
        }, 0, 1, RepeatPredicate.length(Duration.ofSeconds(timer.getTime()))).getFuture();

        future.thenRun(() -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "admin nextRound");
        });
    }
}
