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
public class AttackerAbilityItem extends CustomItemFairy {
    @Override
    protected FairyItem register() {
        return FairyItem.builder("attackerAbility")
                .item(ItemBuilder.of(Material.SUGAR)
                        .name("§e§l突進 §7(右鍵使用)")
                        .lore()
                        .build())
                .build();
    }

    @RegisterAsListener
    @InjectableComponent
    class AttackAbilityListener extends ItemListenerTemplate {
        private final PlayerKitManager kitManager;
        private final RemoveCustomItem removeCustomItem;
        private final AttackerAbilityItem attackerAbilityItem;

        public AttackAbilityListener(AttackerAbilityItem customItem, PlayerKitManager kitManager, RemoveCustomItem removeCustomItem, AttackerAbilityItem attackerAbilityItem) {
            super(customItem);
            this.kitManager = kitManager;
            this.removeCustomItem = removeCustomItem;
            this.attackerAbilityItem = attackerAbilityItem;
        }

        @Override
        protected void onRightClickItem(PlayerInteractEvent event) {
            Player player = event.getPlayer();
            kitManager.getPlayerKit(player).useAbility(player);
            removeCustomItem.removeCustomItem(player, Arrays.asList(attackerAbilityItem));
        }

//        @EventHandler
//        public void onDrop(PlayerDropItemEvent event){
//            if(isItem(event.getItemDrop().getItemStack()))
//                event.getPlayer().sendMessage("DROPPED");
//        }
    }
}
