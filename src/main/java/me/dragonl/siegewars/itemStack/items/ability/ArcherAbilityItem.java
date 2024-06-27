package me.dragonl.siegewars.itemStack.items.ability;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.bukkit.util.items.FairyItem;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import me.dragonl.siegewars.game.GameState;
import me.dragonl.siegewars.game.GameStateManager;
import me.dragonl.siegewars.game.RoundState;
import me.dragonl.siegewars.itemStack.CustomItemFairy;
import me.dragonl.siegewars.itemStack.ItemListenerTemplate;
import me.dragonl.siegewars.itemStack.RemoveCustomItem;
import me.dragonl.siegewars.player.PlayerKitManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;

@InjectableComponent
public class ArcherAbilityItem extends CustomItemFairy {

    @Override
    protected FairyItem register() {
        return FairyItem.builder("archerAbility")
                .item(ItemBuilder.of(Material.GLOWSTONE_DUST)
                        .name("§e§l躍進 §7(右鍵使用)")
                        .lore()
                        .build())
                .build();
    }

    @RegisterAsListener
    @InjectableComponent
    class ArcherAbilityListener extends ItemListenerTemplate {
        private final RemoveCustomItem removeCustomItem;
        private final ArcherAbilityItem archerAbilityItem;
        private final PlayerKitManager kitManager;
        private final GameStateManager gameStateManager;

        public ArcherAbilityListener(ArcherAbilityItem customItem, RemoveCustomItem removeCustomItem, ArcherAbilityItem archerAbilityItem, PlayerKitManager kitManager, GameStateManager gameStateManager) {
            super(customItem);
            this.removeCustomItem = removeCustomItem;
            this.archerAbilityItem = archerAbilityItem;
            this.kitManager = kitManager;
            this.gameStateManager = gameStateManager;
        }

        @Override
        protected void onRightClickItem(PlayerInteractEvent event) {
            if (gameStateManager.isCurrentGameState(GameState.IN_GAME))
                if (gameStateManager.isCurrentRoundState(RoundState.PREPARING) || gameStateManager.isCurrentRoundState(RoundState.POSITION_CHOOSING))
                    return;

            Player player = event.getPlayer();
            kitManager.getPlayerKit(player).useAbility(player);
            removeCustomItem.removeCustomItem(player, Arrays.asList(archerAbilityItem));
        }

//        @EventHandler
//        public void onDrop(PlayerDropItemEvent event){
//            if(isItem(event.getItemDrop().getItemStack()))
//                event.getPlayer().sendMessage("DROPPED");
//        }
    }
}
