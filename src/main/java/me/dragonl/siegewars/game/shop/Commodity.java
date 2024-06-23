package me.dragonl.siegewars.game.shop;

import org.bukkit.inventory.ItemStack;

public interface Commodity {
    String getID();
    String getName();
    int getPrice();
    int getBuyLimit();
    ItemStack getShopIcon();
    ItemStack getItemStack();
}
