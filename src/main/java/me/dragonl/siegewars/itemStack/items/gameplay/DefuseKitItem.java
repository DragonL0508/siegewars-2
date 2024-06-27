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
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

@InjectableComponent
public class DefuseKitItem extends CustomItemFairy {

    @Override
    protected FairyItem register() {
        return FairyItem.builder("sw:defuse_kit")
                .item(ItemBuilder.of(XMaterial.SHEARS)
                        .name("§e拆彈器")
                        .lore()
                        .build())
                .build();
    }

    @RegisterAsListener
    @InjectableComponent
    class BombItemListener extends ItemListenerTemplate {
        private final BombManager bombManager;

        public BombItemListener(DefuseKitItem customItem, BombManager bombManager) {
            super(customItem);
            this.bombManager = bombManager;
        }

        @Override
        protected void onRightClickBlock(PlayerInteractEvent event) {
            if (event.getClickedBlock().getX() == bombManager.getBombLocation().getBlockX()
                    && event.getClickedBlock().getY() == bombManager.getBombLocation().getBlockY()
                    && event.getClickedBlock().getZ() == bombManager.getBombLocation().getBlockZ()
                    && !bombManager.getDefusing()) {
                Player player = event.getPlayer();
                if (player.isOnGround())
                    startDefusing(player);
                else {
                    player.sendMessage("§e[系統] §c站在地面上才能拆除炸彈");
                    player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1, 1);
                }
            }
        }

        private void startDefusing(Player player) {
            bombManager.setDefusing(true);
            Location playerLocation = player.getLocation();

            playerLocation.getWorld().playSound(playerLocation, Sound.NOTE_PLING, 2.0f, 0.75f);
            playerLocation.getWorld().playSound(playerLocation, Sound.NOTE_PLING, 2.0f, 1.25f);

            MCSchedulers.getGlobalScheduler().schedule(() -> {
                playerLocation.getWorld().playSound(playerLocation, Sound.NOTE_PLING, 2.0f, 0.75f);
            }, 4);

            AtomicInteger i = new AtomicInteger();
            CompletableFuture<?> future = MCSchedulers.getGlobalScheduler().scheduleAtFixedRate(() -> {
                player.teleport(playerLocation);

                i.getAndIncrement();
                double progress = (double) i.get() / 160;
                Titles.sendTitle(player, 0, 1000, 0, "§e正在拆除炸藥包", createProgressBar(progress));

                if (!isItem(player.getItemInHand())) {
                    MCSchedulers.getGlobalScheduler().schedule(() -> {
                        Titles.sendTitle(player, 0, 30, 10, "", "§c拆彈已取消");
                        bombManager.setDefusing(false);
                    }, 1);
                    return TaskResponse.failure("");
                }

                return TaskResponse.continueTask();
            }, 0, 1, RepeatPredicate.length(Duration.ofSeconds(8))).getFuture();

            future.thenRun(() -> {
                bombManager.defuse(player);
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
