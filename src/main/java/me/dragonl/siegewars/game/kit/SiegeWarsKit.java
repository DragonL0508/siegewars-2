package me.dragonl.siegewars.game.kit;

import me.dragonl.siegewars.game.Kit;
import me.dragonl.siegewars.player.PlayerKitManager;
import org.bukkit.entity.Player;

public interface SiegeWarsKit {
    void selectThisKit(Player player, Kit kit);
}
