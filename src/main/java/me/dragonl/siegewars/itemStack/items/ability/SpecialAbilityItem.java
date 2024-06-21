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
public class SpecialAbilityItem extends CustomItemFairy {
    @Override
    protected FairyItem register() {
        return FairyItem.builder("specialAbility")
                .item(ItemBuilder.of(Material.BLAZE_POWDER)
                        .name("§e探查 §7(右鍵使用)")
                        .lore()
                        .build())
                .build();
    }

    @RegisterAsListener
    @InjectableComponent
    class SpecialAbilityListener extends ItemListenerTemplate {
        private final PlayerKitManager kitManager;
        private final RemoveCustomItem removeCustomItem;
        private final SpecialAbilityItem specialAbilityItem;

        public SpecialAbilityListener(SpecialAbilityItem customItem, PlayerKitManager kitManager, RemoveCustomItem removeCustomItem, SpecialAbilityItem specialAbilityItem) {
            super(customItem);
            this.kitManager = kitManager;
            this.removeCustomItem = removeCustomItem;
            this.specialAbilityItem = specialAbilityItem;
        }

        @Override
        protected void onRightClickItem(PlayerInteractEvent event) {
            Player player = event.getPlayer();
            if(kitManager.getPlayerKit(player).useAbility(player))
                removeCustomItem.removeCustomItem(player, Arrays.asList(specialAbilityItem));
            else{
                player.sendMessage("§e[技能] §c敵隊目前已被探查!");
            }
        }

//        @EventHandler
//        public void onDrop(PlayerDropItemEvent event){
//            if(isItem(event.getItemDrop().getItemStack()))
//                event.getPlayer().sendMessage("DROPPED");
//        }
    }
}
