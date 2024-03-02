package me.dragonl.siegewars.itemStack.items;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.bukkit.util.items.FairyItem;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import me.dragonl.siegewars.itemStack.CustomItem;
import me.dragonl.siegewars.itemStack.CustomItemFairy;
import me.dragonl.siegewars.itemStack.ItemListenerTemplate;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

@InjectableComponent
public class SelectTeamItem extends CustomItemFairy {
    @Override
    protected FairyItem register() {
        return FairyItem.builder("sw:teamSelector")
                .item(ItemBuilder.of(Material.NAME_TAG)
                        .name("¡±eSelect Team ¡±7(Right Click)")
                        .build())
                .build();
    }

    @RegisterAsListener
    @InjectableComponent
    class SelectTeamItemListener extends ItemListenerTemplate {

        public SelectTeamItemListener(SelectTeamItem customItem) {
            super(customItem);
        }

        @Override
        protected void onRightClickItem(PlayerInteractEvent event) {
            event.getPlayer().performCommand("ts menu");
        }

//        @EventHandler
//        public void onDrop(PlayerDropItemEvent event){
//            if(isItem(event.getItemDrop().getItemStack()))
//                event.getPlayer().sendMessage("DROPPED");
//        }
    }
}
