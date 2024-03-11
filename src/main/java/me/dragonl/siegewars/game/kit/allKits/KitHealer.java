package me.dragonl.siegewars.game.kit.allKits;

import me.dragonl.siegewars.game.kit.SiegeWarsAbstractKit;
import me.dragonl.siegewars.player.PlayerKitManager;
import me.dragonl.siegewars.team.TeamManager;
import org.bukkit.entity.Player;

public class KitHealer extends SiegeWarsAbstractKit {
    public KitHealer(PlayerKitManager playerKitManager, TeamManager teamManager) {
        super(playerKitManager, teamManager);
    }

    @Override
    protected void giveKitItems(Player player) {

    }

    @Override
    public void useAbility(Player player) {

    }
}
