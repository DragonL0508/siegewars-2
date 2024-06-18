package me.dragonl.siegewars.itemStack.items;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.bukkit.util.items.FairyItem;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import me.dragonl.siegewars.game.mapSetup.SetupWandManager;
import me.dragonl.siegewars.itemStack.CustomItemFairy;
import me.dragonl.siegewars.itemStack.ItemListenerTemplate;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;

@InjectableComponent
public class SetupWand extends CustomItemFairy {
    private final SetupWandManager setupWandManager;

    public SetupWand(SetupWandManager setupWandManager) {
        this.setupWandManager = setupWandManager;
    }

    @Override
    protected FairyItem register() {
        return FairyItem.builder("sw:setupWand")
                .item(ItemBuilder.of(Material.BLAZE_ROD)
                        .name("&6Siegewars 區域圈選器")
                        .build())
                .build();
    }

    @RegisterAsListener
    @InjectableComponent
    class SetupWandListener extends ItemListenerTemplate {

        public SetupWandListener(SetupWand customItem) {
            super(customItem);
        }

        @Override
        protected void onClickBlock(PlayerInteractEvent event) {
            Player p = event.getPlayer();
            int setPos = 1;
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
                setPos = 2;

            int x = event.getClickedBlock().getX();
            int y = event.getClickedBlock().getY();
            int z = event.getClickedBlock().getZ();

            p.sendMessage("§a已選取第 §e" + setPos + " §a點座標! §7(" + x + ", " + y + ", " + z + ")");

            switch (event.getAction()) {
                case LEFT_CLICK_BLOCK: {
                    setupWandManager.getPlayerSelection1().put(p.getUniqueId(), event.getClickedBlock().getLocation());
                    break;
                }
                case RIGHT_CLICK_BLOCK: {
                    setupWandManager.getPlayerSelection2().put(p.getUniqueId(), event.getClickedBlock().getLocation());
                    break;
                }
            }

            event.setCancelled(true);
        }
    }
}
