package me.dragonl.siegewars.itemStack.items.ability;

import com.cryptomorin.xseries.XMaterial;
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
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;

@InjectableComponent
public class ReaperAbilityItem extends CustomItemFairy {
    @Override
    protected FairyItem register() {
        return FairyItem.builder("reaperAbility")
                .item(ItemBuilder.of(XMaterial.GUNPOWDER)
                        .name("§e§l詛咒者 §7(右鍵使用)")
                        .lore()
                        .build())
                .build();
    }

    @RegisterAsListener
    @InjectableComponent
    class ReaperAbilityListener extends ItemListenerTemplate {
        private final RemoveCustomItem removeCustomItem;
        private final ReaperAbilityItem reaperAbilityItem;
        private final PlayerKitManager kitManager;
        private final GameStateManager gameStateManager;

        public ReaperAbilityListener(ReaperAbilityItem customItem, RemoveCustomItem removeCustomItem, ReaperAbilityItem reaperAbilityItem, PlayerKitManager kitManager, GameStateManager gameStateManager) {
            super(customItem);
            this.removeCustomItem = removeCustomItem;
            this.reaperAbilityItem = reaperAbilityItem;
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
            removeCustomItem.removeCustomItem(player, Arrays.asList(reaperAbilityItem));
        }

//        @EventHandler
//        public void onDrop(PlayerDropItemEvent event){
//            if(isItem(event.getItemDrop().getItemStack()))
//                event.getPlayer().sendMessage("DROPPED");
//        }
    }
}
