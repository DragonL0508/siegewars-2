package me.dragonl.siegewars.game.kit;

import io.fairyproject.mc.MCPlayer;
import me.dragonl.siegewars.game.Kit;
import me.dragonl.siegewars.player.PlayerKitManager;
import org.bukkit.entity.Player;

public class Attacker implements SiegeWarsKit{
    private final PlayerKitManager playerKitManager;

    public Attacker(PlayerKitManager playerKitManager) {
        this.playerKitManager = playerKitManager;
    }

    @Override
    public void selectThisKit(Player player) {
        player.sendMessage("¡±aYou selected the kit :¡±e attacker!");
        playerKitManager.setPlayerKit(player, Kit.ATTACKER);
    }

    @Override
    public void useAbility(Player player) {

    }
}
