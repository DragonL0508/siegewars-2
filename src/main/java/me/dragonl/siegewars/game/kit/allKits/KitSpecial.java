package me.dragonl.siegewars.game.kit.allKits;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import me.dragonl.siegewars.game.kit.SiegeWarsAbstractKit;
import me.dragonl.siegewars.itemStack.items.ability.SpecialAbilityItem;
import me.dragonl.siegewars.player.NameGetter;
import me.dragonl.siegewars.player.PlayerKitManager;
import me.dragonl.siegewars.team.TeamManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

@InjectableComponent
public class KitSpecial extends SiegeWarsAbstractKit {
    private final SpecialAbilityItem specialAbilityItem;
    public KitSpecial(
            PlayerKitManager playerKitManager
            , TeamManager teamManager
            , NameGetter nameGetter
            , SpecialAbilityItem specialAbilityItem) {
        super(playerKitManager, teamManager, nameGetter);
        this.specialAbilityItem = specialAbilityItem;
    }

    @Override
    public void useAbility(Player player) {
        player.sendMessage("ability!");
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
        inv.setChestplate(ItemBuilder.of(XMaterial.CHAINMAIL_CHESTPLATE)
                .editMeta(m -> {
                    m.spigot().setUnbreakable(true);
                })
                .build());
        inv.setBoots(ItemBuilder.of(XMaterial.GOLDEN_BOOTS)
                .editMeta(m -> {
                    m.spigot().setUnbreakable(true);
                })
                .build());
        inv.setItem(0, ItemBuilder.of(XMaterial.BOW)
                .editMeta(m -> {
                    m.spigot().setUnbreakable(true);
                })
                .build());
        inv.setItem(1, ItemBuilder.of(XMaterial.FISHING_ROD)
                .editMeta(m -> {
                    m.spigot().setUnbreakable(true);
                })
                .build());
        inv.setItem(8, specialAbilityItem.get(player));
        inv.setItem(9, ItemBuilder.of(XMaterial.ARROW)
                .amount(24)
                .build());
    }
}
