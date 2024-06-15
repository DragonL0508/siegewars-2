package me.dragonl.siegewars.itemStack.items.ability;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.bukkit.util.items.FairyItem;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import me.dragonl.siegewars.game.kit.allKits.KitArcher;
import me.dragonl.siegewars.game.kit.allKits.KitSpecial;
import me.dragonl.siegewars.itemStack.CustomItemFairy;
import me.dragonl.siegewars.itemStack.ItemListenerTemplate;
import me.dragonl.siegewars.itemStack.RemoveCustomItem;
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
        private final KitSpecial kitSpecial;
        private final RemoveCustomItem removeCustomItem;
        private final SpecialAbilityItem specialAbilityItem;

        public SpecialAbilityListener(SpecialAbilityItem customItem, KitSpecial kitSpecial, RemoveCustomItem removeCustomItem, SpecialAbilityItem specialAbilityItem) {
            super(customItem);
            this.kitSpecial = kitSpecial;
            this.removeCustomItem = removeCustomItem;
            this.specialAbilityItem = specialAbilityItem;
        }

        @Override
        protected void onRightClickItem(PlayerInteractEvent event) {
            Player player = event.getPlayer();
            if(kitSpecial.useAbility(player))
                removeCustomItem.removeCustomItem(player, Arrays.asList(specialAbilityItem));
        }

//        @EventHandler
//        public void onDrop(PlayerDropItemEvent event){
//            if(isItem(event.getItemDrop().getItemStack()))
//                event.getPlayer().sendMessage("DROPPED");
//        }
    }
}
