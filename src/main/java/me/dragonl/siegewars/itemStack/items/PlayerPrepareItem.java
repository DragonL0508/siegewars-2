package me.dragonl.siegewars.itemStack.items;

import com.google.common.collect.Lists;
import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.bukkit.util.items.FairyItem;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import me.dragonl.siegewars.game.preparing.PlayerPreparingManager;
import me.dragonl.siegewars.itemStack.CustomItem;
import me.dragonl.siegewars.itemStack.CustomItemFairy;
import me.dragonl.siegewars.itemStack.ItemListenerTemplate;
import me.dragonl.siegewars.itemStack.RemoveCustomItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

@InjectableComponent
public class PlayerPrepareItem extends CustomItemFairy {

    @Override
    protected FairyItem register() {
        return FairyItem.builder("sw:prepare")
                .item(ItemBuilder.of(Material.EMERALD)
                        .name("§e準備 §7(右鍵點擊)")
                        .build())
                .build();
    }

    @InjectableComponent
    @RegisterAsListener
    class PlayerPrepareItemListener extends ItemListenerTemplate {
        private final RemoveCustomItem removeCustomItem;
        private final CustomItem itemToRemove;
        private final PlayerPreparingManager playerPreparingManager;

        public PlayerPrepareItemListener(PlayerPrepareItem customItem, RemoveCustomItem removeCustomItem, PlayerPreparingManager playerPreparingManager) {
            super(customItem);
            this.removeCustomItem = removeCustomItem;
            this.itemToRemove = customItem;
            this.playerPreparingManager = playerPreparingManager;
        }

        @Override
        protected void onRightClickItem(PlayerInteractEvent event) {
            Player player = event.getPlayer();
            player.sendMessage("§e" + player.getName() + " §a已準備!");
            player.playSound(player.getLocation(), Sound.LEVEL_UP,1,1);
            playerPreparingManager.setPlayerPreparedMap(player, true);
            removeCustomItem.removeCustomItem(player, Lists.newArrayList(itemToRemove));
        }
    }
}
