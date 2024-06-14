package me.dragonl.siegewars.game.kit.allKits;

import me.dragonl.siegewars.game.kit.SiegeWarsAbstractKit;
import me.dragonl.siegewars.player.NameGetter;
import me.dragonl.siegewars.player.PlayerKitManager;
import me.dragonl.siegewars.team.TeamManager;
import org.bukkit.entity.Player;

public class KitArcher extends SiegeWarsAbstractKit {
    public KitArcher(
            PlayerKitManager playerKitManager
            , TeamManager teamManager
            , NameGetter nameGetter) {
        super(playerKitManager, teamManager, nameGetter);
    }

    @Override
    protected void giveKitItems(Player player) {

    }

    @Override
    public void useAbility(Player player) {

    }
}
