package me.dragonl.siegewars.game.preparing;

import com.cryptomorin.xseries.messages.Titles;
import io.fairyproject.bootstrap.bukkit.BukkitPlugin;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.container.PostInitialize;
import me.dragonl.siegewars.game.GameState;
import me.dragonl.siegewars.game.GameStateManager;
import me.dragonl.siegewars.SoundPlayer;
import me.dragonl.siegewars.team.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

@InjectableComponent
public class PlayerPreparingChecker extends BukkitRunnable {
    private final GameStateManager gameStateManager;
    private final PlayerPreparingManager playerPreparingManager;
    private final TeamManager teamManager;
    private final SoundPlayer soundPlayer;

    public PlayerPreparingChecker(GameStateManager gameStateManager, PlayerPreparingManager playerPreparingManager, TeamManager teamManager, SoundPlayer soundPlayer) {
        this.gameStateManager = gameStateManager;
        this.playerPreparingManager = playerPreparingManager;
        this.teamManager = teamManager;
        this.soundPlayer = soundPlayer;
    }

    @PostInitialize
    public void init() {
        this.runTaskTimer(BukkitPlugin.INSTANCE, 0, 1);
    }

    @Override
    public void run() {
        if (gameStateManager.isCurrentGameState(GameState.PREPARING)) {
            Bukkit.getOnlinePlayers().forEach(player -> {
                Titles.sendTitle(player, 0, 30, 0, "§a玩家準備中", "§7(點選綠寶石進行準備)");
            });
            if (!playerPreparingManager.playerPrepareMap.containsValue(false)) {
                teamManager.swTeamSplits(teamManager.getTeam("lobby").getOnlineBukkitPlayers());
                Bukkit.getOnlinePlayers().forEach(Titles::clearTitle);
                Bukkit.getOnlinePlayers().forEach(player -> {
                    player.getInventory().clear();
                });
                this.cancel();

                new GameStartCountdown(soundPlayer, gameStateManager, teamManager).runTaskTimer(BukkitPlugin.INSTANCE, 0, 20);
            }
        }
    }
}
