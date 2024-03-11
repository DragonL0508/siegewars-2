package me.dragonl.siegewars.game.kit;

import me.dragonl.siegewars.game.Kit;
import me.dragonl.siegewars.player.PlayerKitManager;
import me.dragonl.siegewars.team.TeamManager;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public abstract class SiegeWarsAbstractKit implements SiegeWarsKit {
    protected final PlayerKitManager playerKitManager;
    protected final TeamManager teamManager;

    public SiegeWarsAbstractKit(PlayerKitManager playerKitManager, TeamManager teamManager) {
        this.playerKitManager = playerKitManager;
        this.teamManager = teamManager;
    }

    @Override
    public void selectThisKit(Player player, Kit kit) {
        player.sendMessage("§a你選擇了職業: §e" + playerKitManager.getKitString(kit));
        player.playSound(player.getLocation(), Sound.NOTE_PIANO, 1, 1.2f);
        playerKitManager.setPlayerKit(player, kit);
        giveKitItems(player);
    }

    protected abstract void useAbility(Player player);
    protected abstract void giveKitItems(Player player);
}