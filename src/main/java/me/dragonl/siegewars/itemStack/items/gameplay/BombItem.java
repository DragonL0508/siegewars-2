package me.dragonl.siegewars.itemStack.items.gameplay;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.messages.Titles;
import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.bukkit.util.items.FairyItem;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.scheduler.MCSchedulers;
import io.fairyproject.scheduler.repeat.RepeatPredicate;
import io.fairyproject.scheduler.response.TaskResponse;
import me.dragonl.siegewars.game.ingame.BombManager;
import me.dragonl.siegewars.itemStack.CustomItemFairy;
import me.dragonl.siegewars.itemStack.ItemListenerTemplate;
import me.dragonl.siegewars.itemStack.RemoveCustomItem;
import me.dragonl.siegewars.player.PlayerKitManager;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

@InjectableComponent
public class BombItem extends CustomItemFairy {

    @Override
    protected FairyItem register() {
        return FairyItem.builder("sw:bomb")
                .item(ItemBuilder.of(XMaterial.PLAYER_HEAD)
                        .name("§e炸藥包")
                        .lore()
                        .skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGY4NzRiYjViNmVmN2IzNDM0YTU4YjMxNDk2MjUyMTg2Mjk1YzM3OWJlOTk2OTM0ZDEwM2QxNWVhMjI1Y2JhMyJ9fX0=")
                        .build())
                .build();
    }

    @RegisterAsListener
    @InjectableComponent
    class BombItemListener extends ItemListenerTemplate {
        private final RemoveCustomItem removeCustomItem;
        private final BombItem bombItem;
        private final BombManager bombManager;

        public BombItemListener(BombItem customItem, RemoveCustomItem removeCustomItem, BombItem bombItem, BombManager bombManager) {
            super(customItem);
            this.removeCustomItem = removeCustomItem;
            this.bombItem = bombItem;
            this.bombManager = bombManager;
        }

        @EventHandler
        public void onPlaced(BlockPlaceEvent event) {
            if (isItem(event.getItemInHand()))
                startPlanting(event.getPlayer(), event.getBlockPlaced().getLocation());
        }

        private void startPlanting(Player player, Location location) {
            Location playerLoc = player.getLocation();
            Titles.sendTitle(player, 0, 1000, 0, "", "§e開始安裝炸藥包");
            playerLoc.getWorld().playSound(playerLoc, Sound.ORB_PICKUP, 3, 1.7f);
            playerLoc.getWorld().playSound(playerLoc, Sound.NOTE_PLING, 3, 1.7f);
            MCSchedulers.getGlobalScheduler().schedule(() -> {
                playerLoc.getWorld().playSound(playerLoc, Sound.ORB_PICKUP, 3, 1.7f);
            }, 4);

            CompletableFuture<?> future = MCSchedulers.getGlobalScheduler().scheduleAtFixedRate(() -> {
                if (!isItem(player.getItemInHand())) {
                    Titles.sendTitle(player, 0, 30, 10, "", "§c炸藥已取消安裝");
                    return TaskResponse.failure("");
                }

                player.teleport(playerLoc);

                return TaskResponse.continueTask();
            }, 0, 1, RepeatPredicate.length(Duration.ofSeconds(4))).getFuture();

            future.thenRun(() -> {
                removeCustomItem.removeCustomItem(player, Arrays.asList(bombItem));
                bombManager.plant(player, location);
            });
        }

        //        @EventHandler
//        public void onDrop(PlayerDropItemEvent event){
//            if(isItem(event.getItemDrop().getItemStack()))
//                event.getPlayer().sendMessage("DROPPED");
//        }
    }
}
