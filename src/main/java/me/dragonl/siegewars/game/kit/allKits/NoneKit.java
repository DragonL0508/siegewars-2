package me.dragonl.siegewars.game.kit.allKits;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import me.dragonl.siegewars.game.kit.SiegeWarsKit;
import me.dragonl.siegewars.itemStack.items.ability.AttackerAbilityItem;
import me.dragonl.siegewars.team.TeamManager;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class NoneKit implements SiegeWarsKit {

    @Override
    public String getID() {
        return "none";
    }

    @Override
    public String getKitName() {
        return "無";
    }

    @Override
    public String getKitChar() {
        return "";
    }

    @Override
    public ItemStack getKitIcon() {
        return ItemBuilder.of(XMaterial.RED_STAINED_GLASS_PANE).build();
    }

    @Override
    public ItemStack[] getArmors(Player player) {
        return new ItemStack[]{
                ItemBuilder.of(XMaterial.AIR).build(),
                ItemBuilder.of(XMaterial.AIR).build(),
                ItemBuilder.of(XMaterial.AIR).build(),
                ItemBuilder.of(XMaterial.AIR).build()
        };
    }

    @Override
    public ItemStack[] getItems() {
        return new ItemStack[0];
    }

    @Override
    public ItemStack getAbilityItem(Player player) {
        return ItemBuilder.of(XMaterial.AIR).build();
    }

    @Override
    public Boolean useAbility(Player player) {
        player.sendMessage("§cYou don't have a kit!");
        return true;
    }
}
