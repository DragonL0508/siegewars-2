package me.dragonl.siegewars.itemStack.items.gameplay;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.bukkit.util.BukkitPos;
import io.fairyproject.bukkit.util.items.FairyItem;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.scheduler.MCSchedulers;
import io.fairyproject.mc.util.Position;
import io.fairyproject.scheduler.response.TaskResponse;
import me.dragonl.siegewars.game.MapObjectCatcher;
import me.dragonl.siegewars.itemStack.CustomItemFairy;
import me.dragonl.siegewars.itemStack.ItemListenerTemplate;
import me.dragonl.siegewars.itemStack.items.ItemRemover;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@InjectableComponent
public class BaffleItem extends CustomItemFairy {
    private final MapObjectCatcher mapObjectCatcher;

    public BaffleItem(MapObjectCatcher mapObjectCatcher) {
        this.mapObjectCatcher = mapObjectCatcher;
    }

    @Override
    protected FairyItem register() {
        return FairyItem.builder("sw:baffle")
                .item(ItemBuilder.of(XMaterial.PLAYER_HEAD)
                        .name("§6可放式擋板")
                        .skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjUyZDUxNDhhMTA3MzAxYzkzZTMwM2UxZjUzYjI4NjM1YTMxOTIyNGE4MTNlZWRjMGE5OWNiYWZjMGRlYjVjMSJ9fX0=")
                        .build())
                .build();
    }

    @RegisterAsListener
    @InjectableComponent
    class BaffleItemListener extends ItemListenerTemplate {
        private final ItemRemover itemRemover;

        public BaffleItemListener(BaffleItem customItem, ItemRemover itemRemover) {
            super(customItem);
            this.itemRemover = itemRemover;
        }

        @EventHandler
        public void onPlace(BlockPlaceEvent event) {
            Position blockPos = BukkitPos.toMCPos(event.getBlockPlaced().getLocation());
            if (isItem(event.getItemInHand())) {
                event.setCancelled(true);

                if (event.getBlockAgainst().getType() == XMaterial.YELLOW_TERRACOTTA.parseMaterial()) {
                    placeBaffle(event.getPlayer(), BukkitPos.toBukkitLocation(blockPos));
                    itemRemover.decreaseItemInHand(event.getPlayer(), 1);
                    mapObjectCatcher.getBafflePlaced().add(event.getBlockPlaced().getLocation());
                }
            }
        }

        private void placeBaffle(Player player, Location location) {
            Location blockPlaced = location.clone();
            PotionEffect slowness = new PotionEffect(PotionEffectType.SLOW, 12, 3, false, false);
            player.addPotionEffect(slowness);
            location.getWorld().spigot().playEffect(blockPlaced, Effect.STEP_SOUND, Material.ACACIA_FENCE.getId(), 0, 0.25F, 0.25F, 0.25F, 0, 3, 16);

            MCSchedulers.getGlobalScheduler().schedule(() -> {
                blockPlaced.getBlock().setType(Material.ACACIA_FENCE);
            }, 1);

            MCSchedulers.getGlobalScheduler().scheduleAtFixedRate(() -> {
                if (location.add(0, 1, 0).getBlock().getType() == Material.AIR) {
                    player.addPotionEffect(slowness);
                    location.getBlock().setType(Material.ACACIA_FENCE);
                    location.getWorld().spigot().playEffect(location, Effect.STEP_SOUND, Material.ACACIA_FENCE.getId(), 0, 0.25F, 0.25F, 0.25F, 0, 3, 16);
                } else
                    return TaskResponse.success(null);

                return TaskResponse.continueTask();
            }, 11, 10);
        }
    }
}
