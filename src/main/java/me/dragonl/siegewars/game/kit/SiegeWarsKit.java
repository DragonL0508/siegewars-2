package me.dragonl.siegewars.game.kit;

import me.dragonl.siegewars.player.PlayerKitManager;
import org.bukkit.entity.Player;

public interface SiegeWarsKit {
    void selectThisKit(Player player);
    void useAbility(Player player);
}
