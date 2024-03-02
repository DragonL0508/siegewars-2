package me.dragonl.siegewars.itemStack;

import io.fairyproject.container.InjectableComponent;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

@InjectableComponent
public class RemoveCustomItem {
    public void removeCustomItem(Player player, List<CustomItem> list){
        Arrays.stream(player.getInventory().getContents()).toList().forEach(itemStack -> {
            list.forEach(customItem -> {
                if(customItem.isSimilar(itemStack))
                    player.getInventory().remove(itemStack);
            });
        });
    }
}
