package me.dragonl.siegewars.game.ingame;

import com.cryptomorin.xseries.messages.Titles;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.scheduler.MCSchedulers;
import io.fairyproject.scheduler.repeat.RepeatPredicate;
import io.fairyproject.scheduler.response.TaskResponse;
import me.dragonl.siegewars.game.ingame.ingameTimer.Timer;
import me.dragonl.siegewars.team.TeamManager;
import org.bukkit.Bukkit;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@InjectableComponent
public class InGameRunTime {
    private final TeamManager teamManager;
    Map<UUID, Boolean> preparingPlayers = new HashMap<>();

    public InGameRunTime(TeamManager teamManager) {
        this.teamManager = teamManager;
    }

    public Map<UUID, Boolean> getPreparingPlayers() {
        return preparingPlayers;
    }

    public void preparingRunTime(Timer timer) {
        preparingPlayers.clear();
        teamManager.getTeam("A").getPlayers().forEach(uuid -> {
            preparingPlayers.put(uuid, false);
        });
        teamManager.getTeam("B").getPlayers().forEach(uuid -> {
            preparingPlayers.put(uuid, false);
        });

        CompletableFuture<?> future = MCSchedulers.getGlobalScheduler().scheduleAtFixedRate(() -> {
            if (!preparingPlayers.containsValue(false))
                return TaskResponse.success("");
            else {
                preparingPlayers.forEach((k, v) -> {
                    if (v) {
                        Titles.sendTitle(Bukkit.getPlayer(k), 0, 20, 0, "§e玩家備戰中...", "§6♦♦ §7| §b♦♦");
                    }
                });
            }

            Bukkit.getOnlinePlayers().forEach(p -> {
                if (teamManager.isInTeam(p, teamManager.getTeam("spectator")))
                    Titles.sendTitle(p, 0, 20, 0, "§e玩家備戰中...", "§6♦♦ §7| §b♦♦");
            });

            return TaskResponse.continueTask();
        }, 0, 1, RepeatPredicate.length(Duration.ofSeconds(timer.getTime()))).getFuture();

        future.thenRun(() -> {
            timer.setTime(0);
            Bukkit.getOnlinePlayers().forEach(Titles::clearTitle);
            Bukkit.broadcastMessage("Preparing Done!");
        });
    }

    public void positionChoosingRunTime(Timer timer) {
        MCSchedulers.getGlobalScheduler().schedule(() -> {
            Bukkit.broadcastMessage("Position Choosing Done!");
        }, timer.getTime() * 20);
    }
}
