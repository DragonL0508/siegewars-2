package me.dragonl.siegewars.itemStack.items.gameplay;

import io.fairyproject.bukkit.events.player.PlayerDamageByPlayerEvent;
import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.bukkit.util.items.FairyItem;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import me.dragonl.siegewars.game.MapObjectCatcher;
import me.dragonl.siegewars.itemStack.CustomItemFairy;
import me.dragonl.siegewars.itemStack.ItemListenerTemplate;
import me.dragonl.siegewars.itemStack.items.cooldown.AxeCoolDown;
import me.dragonl.siegewars.itemStack.items.cooldown.ItemCoolDownManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemFlag;

@InjectableComponent
public class AxeItem extends CustomItemFairy {
    private final MapObjectCatcher mapObjectCatcher;

    public AxeItem(MapObjectCatcher mapObjectCatcher) {
        this.mapObjectCatcher = mapObjectCatcher;
    }

    @Override
    protected FairyItem register() {
        return FairyItem.builder("sw:axe")
                .item(ItemBuilder.of(Material.GOLD_AXE)
                        .name("§6擋板破壞斧")
                        .lore()
                        .editMeta(m -> {
                            m.spigot().setUnbreakable(true);
                        })
                        .itemFlag(ItemFlag.HIDE_ATTRIBUTES)
                        .itemFlag(ItemFlag.HIDE_UNBREAKABLE)
                        .build())
                .build();
    }

    @RegisterAsListener
    @InjectableComponent
    class AxeItemListener extends ItemListenerTemplate {
        private final ItemCoolDownManager itemCoolDownManager;
        public AxeItemListener(AxeItem customItem, ItemCoolDownManager itemCoolDownManager) {
            super(customItem);
            this.itemCoolDownManager = itemCoolDownManager;
        }

        @EventHandler
        public void attackPlayer(PlayerDamageByPlayerEvent event) {
            if (isItem(event.getDamager().getItemInHand()))
                event.setDamage(0);
        }

        @EventHandler
        public void axeBreakBaffle(BlockBreakEvent event) {
            Player p = event.getPlayer();
            if (isItem(p.getItemInHand()) && mapObjectCatcher.isBaffle(event.getBlock().getLocation())){
                p.playSound(p.getLocation(), Sound.ITEM_BREAK, 1, 0.75f);
                itemCoolDownManager.addCoolDown(new AxeCoolDown(p.getUniqueId(), 5));
            }
        }
    }
}
