package me.dragonl.siegewars.itemStack.items.gameplay;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.bukkit.util.BukkitPos;
import io.fairyproject.bukkit.util.items.FairyItem;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import me.dragonl.siegewars.game.mapSetup.SetupWandManager;
import me.dragonl.siegewars.itemStack.CustomItemFairy;
import me.dragonl.siegewars.itemStack.ItemListenerTemplate;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.omg.CORBA.PUBLIC_MEMBER;

@InjectableComponent
public class TNTItem extends CustomItemFairy {
    private final SetupWandManager setupWandManager;

    public TNTItem(SetupWandManager setupWandManager) {
        this.setupWandManager = setupWandManager;
    }

    @Override
    protected FairyItem register() {
        return FairyItem.builder("sw:tnt")
                .item(ItemBuilder.of(Material.TNT)
                        .name("§cT§fN§cT §f炸藥")
                        .build())
                .build();
    }

    @RegisterAsListener
    @InjectableComponent
    class TNTItemListener extends ItemListenerTemplate {

        public TNTItemListener(TNTItem customItem) {
            super(customItem);
        }

        @EventHandler
        public void onPlace(BlockPlaceEvent event) {
            Player p = event.getPlayer();
            if (isItem(event.getItemInHand())) {
                p.sendMessage("placed");
                if (setupWandManager.isDestroyableWall(BukkitPos.toMCPos(event.getBlockAgainst().getLocation()))) {
                    p.sendMessage("YES");
                }

                event.setCancelled(true);
            }
        }
    }
}
