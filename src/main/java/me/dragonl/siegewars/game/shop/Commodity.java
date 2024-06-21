package me.dragonl.siegewars.game.shop;

import org.bukkit.inventory.ItemStack;

public interface Commodity {
    String getID();
    String getName();
    int getPrice();
    ItemStack getShopIcon();
    ItemStack getItemStack();
}
