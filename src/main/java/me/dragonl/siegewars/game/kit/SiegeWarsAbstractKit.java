package me.dragonl.siegewars.game.kit;

import me.dragonl.siegewars.game.Kit;
import me.dragonl.siegewars.player.PlayerKitManager;
import me.dragonl.siegewars.team.TeamManager;
import org.bukkit.Bukkit;
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
        teamManager.getPlayerTeam(player).getPlayers().forEach(uuid -> {
            Player p = Bukkit.getPlayer(uuid);
            p.sendMessage(player.getName() + " §a選擇了職業: §e" + playerKitManager.getKitString(kit));
            p.playSound(p.getLocation(), Sound.NOTE_PIANO, 1, 1.2f);
        });
        playerKitManager.setPlayerKit(player, kit);
        giveKitItems(player);
    }

    protected abstract void useAbility(Player player);
    protected abstract void giveKitItems(Player player);
}