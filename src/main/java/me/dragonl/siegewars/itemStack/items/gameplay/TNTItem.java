package me.dragonl.siegewars.itemStack.items.gameplay;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.bukkit.util.BukkitPos;
import io.fairyproject.bukkit.util.items.FairyItem;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.scheduler.MCSchedulers;
import io.fairyproject.mc.util.Position;
import io.fairyproject.scheduler.ScheduledTask;
import me.dragonl.siegewars.game.MapObjectDestroyer;
import me.dragonl.siegewars.game.mapSetup.SetupWandManager;
import me.dragonl.siegewars.itemStack.CustomItemFairy;
import me.dragonl.siegewars.itemStack.ItemListenerTemplate;
import me.dragonl.siegewars.yaml.MapConfig;
import me.dragonl.siegewars.yaml.element.DestroyableWallElement;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.omg.CORBA.PUBLIC_MEMBER;

@InjectableComponent
public class TNTItem extends CustomItemFairy {
    private final SetupWandManager setupWandManager;
    private final MapConfig mapConfig;
    private final MapObjectDestroyer mapObjectDestroyer;

    public TNTItem(SetupWandManager setupWandManager, MapConfig mapConfig, MapObjectDestroyer mapObjectDestroyer) {
        this.setupWandManager = setupWandManager;
        this.mapConfig = mapConfig;
        this.mapObjectDestroyer = mapObjectDestroyer;
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
            World w = event.getBlockPlaced().getWorld();
            Position blockPos = BukkitPos.toMCPos(event.getBlockAgainst().getLocation());

            if (isItem(event.getItemInHand())) {
                if (setupWandManager.isDestroyableWall(blockPos)) {
                    summonTNT(p, event.getBlockPlaced().getLocation(), mapConfig.getMaps().get(w.getName()).getWallAtPosition(blockPos));
                }
                event.setCancelled(true);
            }
        }

        private void summonTNT(Player player, Location location, DestroyableWallElement element) {
            World w = location.getWorld();
            TNTPrimed tnt = (TNTPrimed) location.getWorld().spawnEntity(location.add(0.5f, 0.5f, 0.5f), EntityType.PRIMED_TNT);
            tnt.setFuseTicks(120);

            MCSchedulers.getGlobalScheduler().schedule(() -> {
                Firework firework = (Firework) tnt.getWorld().spawnEntity(tnt.getLocation(), EntityType.FIREWORK);
                FireworkMeta fireworkMeta = firework.getFireworkMeta();
                FireworkEffect effect = FireworkEffect.builder()
                        .flicker(false)
                        .trail(false)
                        .with(FireworkEffect.Type.BALL_LARGE)
                        .withColor(Color.WHITE)
                        .withFade(Color.RED)
                        .build();
                fireworkMeta.addEffect(effect);
                fireworkMeta.setPower(0);
                firework.setFireworkMeta(fireworkMeta);

                w.spigot().playEffect(tnt.getLocation(), Effect.EXPLOSION_HUGE, 0, 0, 0, 0, 0, 1, 1, 64);
                w.playSound(tnt.getLocation(), Sound.EXPLODE, 2, 0.75f);
                w.playSound(tnt.getLocation(),Sound.ZOMBIE_WOODBREAK,1.5f,0.8f);
                tnt.remove();
                mapObjectDestroyer.destroyWall(element);

                MCSchedulers.getGlobalScheduler().schedule(firework::detonate, 1);
            }, 20 * 4);
        }
    }
}
