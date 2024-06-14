package me.dragonl.siegewars.game.kit.allKits;

import me.dragonl.siegewars.game.kit.SiegeWarsAbstractKit;
import me.dragonl.siegewars.player.NameGetter;
import me.dragonl.siegewars.player.PlayerKitManager;
import me.dragonl.siegewars.team.TeamManager;
import org.bukkit.entity.Player;

public class KitSpecial extends SiegeWarsAbstractKit {
    public KitSpecial(
            PlayerKitManager playerKitManager
            , TeamManager teamManager
            , NameGetter nameGetter) {
        super(playerKitManager, teamManager, nameGetter);
    }

    @Override
    protected void useAbility(Player player) {

    }

    @Override
    protected void giveKitItems(Player player) {

    }
}
