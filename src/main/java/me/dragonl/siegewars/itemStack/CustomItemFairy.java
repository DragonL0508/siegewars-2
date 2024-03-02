package me.dragonl.siegewars.itemStack;

import io.fairyproject.bukkit.util.items.FairyItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class CustomItemFairy implements CustomItem {

    private FairyItem fairyItem;

    @Override
    public ItemStack get(Player player) {
        return getFairyItem().provideItemStack(player);
    }

    @Override
    public boolean isSimilar(ItemStack itemStack) {
        return itemStack != null && getFairyItem().isSimilar(itemStack);
    }

    private FairyItem getFairyItem() {
        if (fairyItem == null)
            fairyItem = register();

        return fairyItem;
    }

    protected abstract FairyItem register();
}
