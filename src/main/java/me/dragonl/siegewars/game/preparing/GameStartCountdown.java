package me.dragonl.siegewars.game.preparing;

import com.cryptomorin.xseries.messages.Titles;
import io.fairyproject.bukkit.util.BukkitPos;
import io.fairyproject.container.InjectableComponent;
import me.dragonl.siegewars.game.GameState;
import me.dragonl.siegewars.game.GameStateManager;
import me.dragonl.siegewars.SoundPlayer;
import me.dragonl.siegewars.team.SiegeWarsTeam;
import me.dragonl.siegewars.team.TeamManager;
import me.dragonl.siegewars.yaml.element.MapConfigElement;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@InjectableComponent
public class GameStartCountdown extends BukkitRunnable {
    private final SoundPlayer soundPlayer;
    private final GameStateManager gameStateManager;
    private final TeamManager teamManager;
    private int timer = 10;

    public GameStartCountdown(SoundPlayer soundPlayer, GameStateManager gameStateManager, TeamManager teamManager) {
        this.soundPlayer = soundPlayer;
        this.gameStateManager = gameStateManager;
        this.teamManager = teamManager;
    }

    @Override
    public void run() {
        List<UUID> uuids = Bukkit.getOnlinePlayers().stream().map(Player::getUniqueId).collect(Collectors.toList());
        if (timer == 10) {
            Bukkit.getServer().broadcastMessage("§a玩家準備完成，遊戲即將開始...");
            soundPlayer.playSound(uuids, Sound.CLICK, 1, 1);
        } else if (timer > 0 && timer <= 5) {
            Bukkit.getServer().broadcastMessage("§e遊戲將在§c" + timer + "§e秒後開始!");
            soundPlayer.playSound(uuids, Sound.CLICK, 1, 0.5f);
        }
        if (timer == 0) {
            Bukkit.getServer().broadcastMessage("§a遊戲啟動中,請稍後...");
            gameStateManager.setCurrentGameState(GameState.IN_GAME);
            gameStartLogic();

            this.cancel();
        }
        timer--;
    }

    private void gameStartLogic() {
        //choose attack or defend
        if (Math.random() < 0.5)
            gameStateManager.setAttackTeam(teamManager.getTeam("A"));
        else
            gameStateManager.setAttackTeam(teamManager.getTeam("B"));

        //players loop
        //send players to map
        MapConfigElement element = gameStateManager.getSelectedMap();
        Bukkit.getOnlinePlayers().forEach(p -> {
            Random r = new Random();
            if (teamManager.getPlayerTeam(p) == gameStateManager.getAttackTeam()){
                Titles.sendTitle(p, 10, 70, 20, "§c攻擊方", "§e你的隊伍將先擔任");
                p.teleport(BukkitPos.toBukkitLocation(element.getAttackSpawn().get(r.nextInt(element.getAttackSpawn().size() - 1))));
            }
            else if (teamManager.swGetAnotherTeam(p) == gameStateManager.getAttackTeam()){
                Titles.sendTitle(p, 10, 70, 20, "§b防守方", "§e你的隊伍將先擔任");
                p.teleport(BukkitPos.toBukkitLocation(element.getDefendSpawn().get(r.nextInt(element.getDefendSpawn().size() - 1))));
            }
            else if (teamManager.isInTeam(p, teamManager.getTeam("spec"))){
                p.setGameMode(GameMode.SPECTATOR);
                p.teleport(BukkitPos.toBukkitLocation(element.getSpecSpawn()));
            }
        });
    }
}
