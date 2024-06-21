package me.dragonl.siegewars.itemStack.items.ability;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.bukkit.util.items.FairyItem;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import me.dragonl.siegewars.itemStack.CustomItemFairy;
import me.dragonl.siegewars.itemStack.ItemListenerTemplate;
import me.dragonl.siegewars.itemStack.RemoveCustomItem;
import me.dragonl.siegewars.player.PlayerKitManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;

@InjectableComponent
public class HealerAbilityItem extends CustomItemFairy {

    @Override
    protected FairyItem register() {
        return FairyItem.builder("healerAbility")
                .item(ItemBuilder.of(Material.NETHER_WARTS)
                        .name("§e§l群體治癒 §7(右鍵使用)")
                        .lore()
                        .build())
                .build();
    }

    @RegisterAsListener
    @InjectableComponent
    class HealerAbilityListener extends ItemListenerTemplate {
        private final PlayerKitManager kitManager;
        private final RemoveCustomItem removeCustomItem;
        private final HealerAbilityItem healerAbilityItem;

        public HealerAbilityListener(HealerAbilityItem customItem, PlayerKitManager kitManager, RemoveCustomItem removeCustomItem, HealerAbilityItem healerAbilityItem) {
            super(customItem);
            this.kitManager = kitManager;
            this.removeCustomItem = removeCustomItem;
            this.healerAbilityItem = healerAbilityItem;
        }

        @Override
        protected void onRightClickItem(PlayerInteractEvent event) {
            Player player = event.getPlayer();
            if(kitManager.getPlayerKit(player).useAbility(player))
                removeCustomItem.removeCustomItem(player, Arrays.asList(healerAbilityItem));
            else {
                player.sendMessage("§e[技能] §c附近沒有同隊玩家!");
            }
        }

//        @EventHandler
//        public void onDrop(PlayerDropItemEvent event){
//            if(isItem(event.getItemDrop().getItemStack()))
//                event.getPlayer().sendMessage("DROPPED");
//        }
    }
}
