package me.dragonl.siegewars.game.kit;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import me.dragonl.siegewars.game.Kit;
import me.dragonl.siegewars.player.NameGetter;
import me.dragonl.siegewars.player.PlayerKitManager;
import me.dragonl.siegewars.team.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public abstract class SiegeWarsAbstractKit implements SiegeWarsKit {
    protected final PlayerKitManager playerKitManager;
    protected final TeamManager teamManager;
    protected final NameGetter nameGetter;
    protected final KitInfoGetter kitInfoGetter;

    public SiegeWarsAbstractKit(PlayerKitManager playerKitManager, TeamManager teamManager, NameGetter nameGetter, KitInfoGetter kitInfoGetter) {
        this.playerKitManager = playerKitManager;
        this.teamManager = teamManager;
        this.nameGetter = nameGetter;
        this.kitInfoGetter = kitInfoGetter;
    }

    @Override
    public void selectThisKit(Player player, Kit kit) {
        teamManager.getPlayerTeam(player).getPlayers().forEach(uuid -> {
            Player p = Bukkit.getPlayer(uuid);
            p.sendMessage("§e[職業] " + nameGetter.getNameWithTeamColor(player) + " §a選擇了職業: §e" + kitInfoGetter.getKitString(kit));
            p.playSound(p.getLocation(), Sound.NOTE_PIANO, 1, 1.2f);
        });
        playerKitManager.setPlayerKit(player, kit);

        //items
        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[]{
                ItemBuilder.of(XMaterial.AIR).build(),
                ItemBuilder.of(XMaterial.AIR).build(),
                ItemBuilder.of(XMaterial.AIR).build(),
                ItemBuilder.of(XMaterial.AIR).build()
        });
        giveKitItems(player);
    }

    protected abstract Boolean useAbility(Player player);
    protected abstract void giveKitItems(Player player);
}