package me.dragonl.siegewars.game.kit.allKits;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import me.dragonl.siegewars.game.kit.KitInfoGetter;
import me.dragonl.siegewars.game.kit.SiegeWarsAbstractKit;
import me.dragonl.siegewars.player.NameGetter;
import me.dragonl.siegewars.player.PlayerKitManager;
import me.dragonl.siegewars.team.TeamManager;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

@InjectableComponent
public class KitTank extends SiegeWarsAbstractKit {
    private final KitInfoGetter kitInfoGetter;
    public KitTank(
            PlayerKitManager playerKitManager
            , TeamManager teamManager
            , NameGetter nameGetter, KitInfoGetter kitInfoGetter) {
        super(playerKitManager, teamManager, nameGetter, kitInfoGetter);
        this.kitInfoGetter = kitInfoGetter;
    }

    @Override
    protected Boolean useAbility(Player player) {
        return true;
    }

    @Override
    protected void giveKitItems(Player player) {
        PlayerInventory inv = player.getInventory();
        inv.setHelmet(ItemBuilder.of(XMaterial.LEATHER_HELMET)
                .color(teamManager.getPlayerTeam(player).getBukkitColor())
                .enchantment(XEnchantment.PROTECTION_PROJECTILE, 2)
                .editMeta(m -> {
                    m.spigot().setUnbreakable(true);
                })
                .build());
        inv.setChestplate(ItemBuilder.of(XMaterial.IRON_CHESTPLATE)
                .editMeta(m -> {
                    m.spigot().setUnbreakable(true);
                })
                .build());
        inv.setLeggings(ItemBuilder.of(XMaterial.LEATHER_LEGGINGS)
                .editMeta(m -> {
                    m.spigot().setUnbreakable(true);
                })
                .color(Color.GRAY)
                .build());
        inv.setBoots(ItemBuilder.of(XMaterial.LEATHER_BOOTS)
                .editMeta(m -> {
                    m.spigot().setUnbreakable(true);
                })
                .color(Color.WHITE)
                .build());
        inv.setItem(0, ItemBuilder.of(XMaterial.GOLDEN_PICKAXE)
                .editMeta(m -> {
                    m.spigot().setUnbreakable(true);
                })
                .enchantment(XEnchantment.KNOCKBACK, 1)
                .build());
        inv.setItem(1, ItemBuilder.of(XMaterial.FISHING_ROD)
                .editMeta(m -> {
                    m.spigot().setUnbreakable(true);
                })
                .build());
        inv.setItem(2, ItemBuilder.of(XMaterial.BOW)
                .editMeta(m -> {
                    m.spigot().setUnbreakable(true);
                })
                .build());
        inv.setItem(9, ItemBuilder.of(XMaterial.ARROW)
                .amount(8)
                .build());
    }
}
