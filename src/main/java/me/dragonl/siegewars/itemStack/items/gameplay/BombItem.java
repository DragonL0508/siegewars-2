package me.dragonl.siegewars.itemStack.items.gameplay;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.messages.Titles;
import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.bukkit.util.BukkitPos;
import io.fairyproject.bukkit.util.items.FairyItem;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.scheduler.MCSchedulers;
import io.fairyproject.mc.util.Position;
import io.fairyproject.scheduler.repeat.RepeatPredicate;
import io.fairyproject.scheduler.response.TaskResponse;
import me.dragonl.siegewars.game.MapObjectCatcher;
import me.dragonl.siegewars.game.ingame.BombManager;
import me.dragonl.siegewars.itemStack.CustomItemFairy;
import me.dragonl.siegewars.itemStack.ItemListenerTemplate;
import me.dragonl.siegewars.itemStack.RemoveCustomItem;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

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
        private final MapObjectCatcher mapObjectCatcher;

        public BombItemListener(BombItem customItem, RemoveCustomItem removeCustomItem, BombItem bombItem, BombManager bombManager, MapObjectCatcher mapObjectCatcher) {
            super(customItem);
            this.removeCustomItem = removeCustomItem;
            this.bombItem = bombItem;
            this.bombManager = bombManager;
            this.mapObjectCatcher = mapObjectCatcher;
        }

        @EventHandler
        public void onPlaced(BlockPlaceEvent event) {
            Position position = BukkitPos.toMCPos(event.getBlockAgainst().getLocation());
            if (isItem(event.getItemInHand()) && mapObjectCatcher.isBombSite(position) && !bombManager.getPlanting()) {
                if (event.getPlayer().isOnGround())
                    startPlanting(event.getPlayer(), event.getBlockPlaced().getLocation());
                else {
                    event.setCancelled(true);
                    Titles.sendTitle(event.getPlayer(), 0, 30, 10, "", "§c炸藥包只能在地面上放置");
                }
            }
        }

        private void startPlanting(Player player, Location location) {
            bombManager.setPlanting(true);

            Location playerLoc = player.getLocation();
            playerLoc.getWorld().playSound(playerLoc, Sound.ORB_PICKUP, 3, 1.7f);
            playerLoc.getWorld().playSound(playerLoc, Sound.NOTE_PLING, 3, 1.7f);
            MCSchedulers.getGlobalScheduler().schedule(() -> {
                playerLoc.getWorld().playSound(playerLoc, Sound.ORB_PICKUP, 3, 1.7f);
            }, 4);

            AtomicInteger i = new AtomicInteger();
            CompletableFuture<?> future = MCSchedulers.getGlobalScheduler().scheduleAtFixedRate(() -> {
                i.getAndIncrement();
                double progress = (double) i.get() / 80;
                Titles.sendTitle(player, 0, 1000, 0, "§e正在安裝炸藥包", createProgressBar(progress));

                if (!isItem(player.getItemInHand())) {
                    MCSchedulers.getGlobalScheduler().schedule(() -> {
                        Titles.sendTitle(player, 0, 30, 10, "", "§c炸藥已取消安裝");
                        bombManager.setPlanting(false);
                    }, 1);
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

        private String createProgressBar(double progress) {
            int totalBars = 20; // 进度条总长度
            int filledBars = (int) (progress * totalBars);
            int emptyBars = totalBars - filledBars;

            StringBuilder progressBar = new StringBuilder();
            progressBar.append("§a"); // 已填充部分的颜色
            for (int i = 0; i < filledBars; i++) {
                progressBar.append("|");
            }
            progressBar.append("§7"); // 未填充部分的颜色
            for (int i = 0; i < emptyBars; i++) {
                progressBar.append("|");
            }

            return progressBar.toString();
        }
    }
}
