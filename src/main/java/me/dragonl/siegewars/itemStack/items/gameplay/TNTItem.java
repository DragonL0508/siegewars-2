package me.dragonl.siegewars.itemStack.items.gameplay;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.bukkit.util.BukkitPos;
import io.fairyproject.bukkit.util.items.FairyItem;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.scheduler.MCSchedulers;
import io.fairyproject.mc.util.Position;
import me.dragonl.siegewars.game.MapObjectDestroyer;
import me.dragonl.siegewars.game.mapSetup.SetupWandManager;
import me.dragonl.siegewars.itemStack.CustomItemFairy;
import me.dragonl.siegewars.itemStack.ItemListenerTemplate;
import me.dragonl.siegewars.itemStack.items.ItemRemover;
import me.dragonl.siegewars.team.TeamManager;
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

@InjectableComponent
public class TNTItem extends CustomItemFairy {
    private final SetupWandManager setupWandManager;
    private final MapConfig mapConfig;
    private final MapObjectDestroyer mapObjectDestroyer;
    private final TeamManager teamManager;

    public TNTItem(SetupWandManager setupWandManager, MapConfig mapConfig, MapObjectDestroyer mapObjectDestroyer, TeamManager teamManager) {
        this.setupWandManager = setupWandManager;
        this.mapConfig = mapConfig;
        this.mapObjectDestroyer = mapObjectDestroyer;
        this.teamManager = teamManager;
    }

    @Override
    protected FairyItem register() {
        return FairyItem.builder("sw:tnt")
                .item(ItemBuilder.of(XMaterial.PLAYER_HEAD)
                        .name("§cT§fN§cT §f炸藥")
                        .skull("MHF_TNT2")
                        .build())
                .build();
    }

    @RegisterAsListener
    @InjectableComponent
    class TNTItemListener extends ItemListenerTemplate {
        private final ItemRemover itemRemover;

        public TNTItemListener(TNTItem customItem, ItemRemover itemRemover) {
            super(customItem);
            this.itemRemover = itemRemover;
        }

        @EventHandler
        public void onPlace(BlockPlaceEvent event) {
            Player p = event.getPlayer();
            World w = event.getBlockPlaced().getWorld();
            Position blockPos = BukkitPos.toMCPos(event.getBlockAgainst().getLocation());

            if (isItem(event.getItemInHand())) {
                if (setupWandManager.isDestroyableWall(blockPos)) {
                    summonTNT(p, event.getBlockPlaced().getLocation(), mapConfig.getMaps().get(w.getName()).getWallAtPosition(blockPos));
                    w.playSound(event.getBlockPlaced().getLocation(), Sound.FUSE, 1, 1);
                    itemRemover.decreaseItemInHand(p, 1);
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
                w.playSound(tnt.getLocation(), Sound.ZOMBIE_WOODBREAK, 1.5f, 0.8f);
                mapObjectDestroyer.destroyWall(element);

                //damage
                tnt.getNearbyEntities(3, 3, 3).forEach(e -> {
                    if (e.getType() != EntityType.PLAYER)
                        return;
                    Player target = (Player) e;
                    if (teamManager.isInTeam(target, teamManager.getPlayerTeam(player)))
                        target.damage(6);
                    if (teamManager.swGetAnotherTeam(player) == teamManager.getPlayerTeam(target))
                        target.damage(6, player);

                });

                tnt.remove();
                MCSchedulers.getGlobalScheduler().schedule(firework::detonate, 1);
            }, 20 * 4);
        }
    }
}
