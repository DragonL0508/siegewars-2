package me.dragonl.siegewars.game.preparing;

import io.fairyproject.bootstrap.bukkit.BukkitPlugin;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.MCPlayer;
import me.dragonl.siegewars.game.GameState;
import me.dragonl.siegewars.game.GameStateManager;
import me.dragonl.siegewars.playsound.SoundPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@InjectableComponent
public class GameStartCountdown extends BukkitRunnable {
    private final SoundPlayer soundPlayer;
    private final GameStateManager gameStateManager;
    private int timer = 10;

    public GameStartCountdown(SoundPlayer soundPlayer, GameStateManager gameStateManager) {
        this.soundPlayer = soundPlayer;
        this.gameStateManager = gameStateManager;
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
            soundPlayer.playSound(uuids, Sound.ENDERDRAGON_GROWL, 1, 1);
            gameStateManager.setCurrentGameState(GameState.IN_GAME);
            this.cancel();
        }
        timer--;
    }
}
