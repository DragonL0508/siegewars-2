package me.dragonl.siegewars.game.kit.allKits;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import me.dragonl.siegewars.game.kit.SiegeWarsAbstractKit;
import me.dragonl.siegewars.player.PlayerKitManager;
import me.dragonl.siegewars.team.TeamManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

public class KitAttacker extends SiegeWarsAbstractKit {
    public KitAttacker(PlayerKitManager playerKitManager, TeamManager teamManager) {
        super(playerKitManager, teamManager);
    }

    @Override
    public void useAbility(Player player) {

    }

    @Override
    protected void giveKitItems(Player player) {
        PlayerInventory inv = player.getInventory();
        inv.setHelmet(ItemBuilder.of(XMaterial.LEATHER_HELMET)
                .color(teamManager.getPlayerTeam(player).getBukkitColor())
                .enchantment(XEnchantment.PROTECTION_PROJECTILE, 2)
                .build());
        inv.setChestplate(ItemBuilder.of(XMaterial.LEATHER_CHESTPLATE)
                .build());
        inv.setBoots(ItemBuilder.of(XMaterial.LEATHER_BOOTS)
                .build());
        inv.setItem(0, ItemBuilder.of(XMaterial.IRON_SWORD)
                .build());
    }
}