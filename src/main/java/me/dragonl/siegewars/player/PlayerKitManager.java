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

    public String getPlayerKitText(Player player) {
        Kit kit = getPlayerKit(player);
        return switch (kit) {
            case NONE -> "";
            case ATTACKER -> "§c§l突";
            case ARCHER -> "§e§l弓";
            case TANK -> "§7§l坦";
            case HEALER -> "§a§l治";
            case SPECIAL -> "§9§l特";
            case REAPER -> "§8§l死";
        };
    }

    public String getPlayerKitString(Player player) {
        Kit kit = getPlayerKit(player);
        return switch (kit) {
            case NONE -> "";
            case ATTACKER -> "突襲者";
            case ARCHER -> "弓箭手";
            case TANK -> "坦克";
            case HEALER -> "治療師";
            case SPECIAL -> "特種兵";
            case REAPER -> "死神";
        };
    }

    public String getKitString(Kit kit){
        return switch (kit) {
            case NONE -> "";
            case ATTACKER -> "突襲者";
            case ARCHER -> "弓箭手";
            case TANK -> "坦克";
            case HEALER -> "治療師";
            case SPECIAL -> "特種兵";
            case REAPER -> "死神";
        };
    }
}
