package me.dragonl.siegewars.player;

import io.fairyproject.container.InjectableComponent;
import me.dragonl.siegewars.game.Kit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@InjectableComponent
public class PlayerKitManager {
    private Map<UUID, Kit> playerKitMap = new HashMap<>();

    public void setPlayerKit(Player player, Kit kit) {
        playerKitMap.put(player.getUniqueId(), kit);
    }

    public Kit getPlayerKit(Player player) {
        return playerKitMap.getOrDefault(player.getUniqueId(), Kit.NONE);
    }
}
