package me.dragonl.siegewars.game.kit.allKits;

import me.dragonl.siegewars.game.kit.SiegeWarsAbstractKit;
import me.dragonl.siegewars.player.PlayerKitManager;
import me.dragonl.siegewars.team.TeamManager;
import org.bukkit.entity.Player;

public class KitReaper extends SiegeWarsAbstractKit {
    public KitReaper(PlayerKitManager playerKitManager, TeamManager teamManager) {
        super(playerKitManager, teamManager);
    }

    @Override
    protected void useAbility(Player player) {

    }

    @Override
    protected void giveKitItems(Player player) {

    }
}
