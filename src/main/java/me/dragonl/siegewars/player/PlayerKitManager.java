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
        switch (kit) {
            case NONE : return "";
            case ATTACKER : return "§c§l突";
            case ARCHER : return "§e§l弓";
            case TANK : return "§7§l坦";
            case HEALER : return "§a§l治";
            case SPECIAL : return "§9§l特";
            case REAPER : return "§8§l死";
        }
        return null;
    }

    public String getPlayerKitString(Player player) {
        Kit kit = getPlayerKit(player);
        switch (kit) {
            case NONE : return "";
            case ATTACKER : return "突襲者";
            case ARCHER : return "弓箭手";
            case TANK : return "坦克";
            case HEALER : return "治療師";
            case SPECIAL : return "特種兵";
            case REAPER : return "死神";
        }
        return null;
    }

    public String getKitString(Kit kit){
        switch (kit) {
            case NONE : return "";
            case ATTACKER : return "突襲者";
            case ARCHER : return "弓箭手";
            case TANK : return "坦克";
            case HEALER : return "治療師";
            case SPECIAL : return "特種兵";
            case REAPER : return "死神";
        }
        return null;
    }
}
