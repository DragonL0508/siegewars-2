package me.dragonl.siegewars.itemStack.items;

import io.fairyproject.container.InjectableComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@InjectableComponent
public class ItemRemover {
    public void decreaseItemInHand (Player player, int amount){
        ItemStack item = player.getItemInHand();
        item.setAmount(item.getAmount() - amount);
        if(item.getAmount() <= 0) item.setType(Material.AIR);
        player.setItemInHand(item);
        player.updateInventory();
    }
}
