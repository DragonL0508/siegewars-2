package me.dragonl.siegewars.game.kit;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface SiegeWarsKit {
    String getID();
    String getKitName();
    String getKitChar();
    ItemStack getKitIcon();
    ItemStack[] getArmors(Player player);
    ItemStack[] getItems();
    ItemStack getAbilityItem(Player player);
    Boolean useAbility(Player player);
}
