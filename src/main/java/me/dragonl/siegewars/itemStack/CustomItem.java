package me.dragonl.siegewars.itemStack;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface CustomItem {
    ItemStack get(Player player);
    boolean isSimilar(ItemStack itemStack);
}
