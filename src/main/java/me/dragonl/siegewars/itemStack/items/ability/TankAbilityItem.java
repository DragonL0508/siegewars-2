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
public class TankAbilityItem extends CustomItemFairy {
    @Override
    protected FairyItem register() {
        return FairyItem.builder("tankAbility")
                .item(ItemBuilder.of(Material.FIREWORK_CHARGE)
                        .name("§e§l衝撞 §7(右鍵使用)")
                        .lore()
                        .build())
                .build();
    }

    @RegisterAsListener
    @InjectableComponent
    class TankAbilityListener extends ItemListenerTemplate {
        private final PlayerKitManager kitManager;
        private final RemoveCustomItem removeCustomItem;
        private final TankAbilityItem tankAbilityItem;
        private final GameStateManager gameStateManager;

        public TankAbilityListener(TankAbilityItem customItem, PlayerKitManager kitManager, RemoveCustomItem removeCustomItem, TankAbilityItem tankAbilityItem, GameStateManager gameStateManager) {
            super(customItem);
            this.kitManager = kitManager;
            this.removeCustomItem = removeCustomItem;
            this.tankAbilityItem = tankAbilityItem;
            this.gameStateManager = gameStateManager;
        }

        @Override
        protected void onRightClickItem(PlayerInteractEvent event) {
            if (gameStateManager.isCurrentGameState(GameState.IN_GAME))
                if (gameStateManager.isCurrentRoundState(RoundState.PREPARING) || gameStateManager.isCurrentRoundState(RoundState.POSITION_CHOOSING))
                    return;

            Player player = event.getPlayer();
            kitManager.getPlayerKit(player).useAbility(player);
            removeCustomItem.removeCustomItem(player, Arrays.asList(tankAbilityItem));
        }
    }
}
