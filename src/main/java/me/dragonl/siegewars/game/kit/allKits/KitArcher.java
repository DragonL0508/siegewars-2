package me.dragonl.siegewars.game.kit.allKits;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.Fairy;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import me.dragonl.siegewars.game.kit.SiegeWarsAbstractKit;
import me.dragonl.siegewars.itemStack.RemoveCustomItem;
import me.dragonl.siegewars.itemStack.items.ability.ArcherAbilityItem;
import me.dragonl.siegewars.player.NameGetter;
import me.dragonl.siegewars.player.PlayerKitManager;
import me.dragonl.siegewars.team.TeamManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;

import java.util.Arrays;

@InjectableComponent
public class KitArcher extends SiegeWarsAbstractKit {
    private final ArcherAbilityItem archerAbilityItem;
    public KitArcher(
            PlayerKitManager playerKitManager
            , TeamManager teamManager
            , NameGetter nameGetter
            , ArcherAbilityItem archerAbilityItem) {
        super(playerKitManager, teamManager, nameGetter);
        this.archerAbilityItem = archerAbilityItem;
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
        inv.setItem(8, archerAbilityItem.get(player));
        inv.setItem(9, ItemBuilder.of(XMaterial.ARROW)
                .amount(24)
                .build());
    }

    @Override
    public void useAbility(Player player) {
        player.setVelocity(player.getVelocity().setY(1));
        inventoryItemsUpdate(player);
    }

    private void inventoryItemsUpdate(Player player){
        PlayerInventory inv = player.getInventory();
        Arrays.stream(inv.getContents()).forEach(itemStack -> {
            if(itemStack != null && itemStack.getType() == Material.BOW)
                itemStack.setItemMeta(ItemBuilder.of(itemStack)
                        .enchantment(XEnchantment.ARROW_DAMAGE)
                        .build().getItemMeta());
            if(itemStack != null && itemStack.getType() == Material.ARROW)
                inv.remove(itemStack);
        });
        inv.addItem(ItemBuilder.of(XMaterial.ARROW)
                .amount(8)
                .build());
    }
}
