package me.dragonl.siegewars.itemStack.items;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.bukkit.util.items.FairyItem;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import me.dragonl.siegewars.itemStack.CustomItem;
import me.dragonl.siegewars.itemStack.CustomItemFairy;
import me.dragonl.siegewars.itemStack.ItemListenerTemplate;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

@InjectableComponent
public class SelectTeamItem extends CustomItemFairy {
    @Override
    protected FairyItem register() {
        return FairyItem.builder("sw:teamSelector")
                .item(ItemBuilder.of(Material.NAME_TAG)
                        .name("§e選擇隊伍 §7(右鍵點擊)")
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
            Player player = event.getPlayer();
            player.performCommand("ts menu");
            player.playSound(player.getLocation(), Sound.HORSE_ARMOR, 1, 0.5f);
        }
    }
}
