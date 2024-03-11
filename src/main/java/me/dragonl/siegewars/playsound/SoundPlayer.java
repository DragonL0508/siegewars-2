package me.dragonl.siegewars.playsound;

import io.fairyproject.container.InjectableComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

@InjectableComponent
public class SoundPlayer {
    public void playSound(List<UUID> players, Sound sound, float volume, float pitch) {
        players.forEach(uuid -> {
            Player player = Bukkit.getPlayer(uuid);
            player.playSound(player.getLocation(), sound, volume, pitch);
        });
    }
}
