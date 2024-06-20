package me.dragonl.siegewars.itemStack.items.cooldown;

import io.fairyproject.bootstrap.bukkit.BukkitPlugin;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.container.PostInitialize;
import io.fairyproject.mc.scheduler.MCSchedulers;
import me.dragonl.siegewars.itemStack.items.gameplay.AxeItem;
import org.bukkit.Bukkit;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

@InjectableComponent
public class ItemCoolDownManager extends BukkitRunnable {
    private final AxeItem axeItem;
    private Map<UUID, List<ItemCoolDown>> playerItemCoolDown = new HashMap<>();

    public ItemCoolDownManager(AxeItem axeItem) {
        this.axeItem = axeItem;
    }

    public Map<UUID, List<ItemCoolDown>> getPlayerItemCoolDown() {
        return playerItemCoolDown;
    }

    public void addCoolDown(ItemCoolDown itemCoolDown) {
        playerItemCoolDown.computeIfAbsent(itemCoolDown.getPlayerUUID(), k -> new ArrayList<>()).add(itemCoolDown);
    }

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(p -> {
            UUID uuid = p.getUniqueId();
            List<ItemCoolDown> coolDowns = playerItemCoolDown.get(uuid);

            if (coolDowns != null) {
                coolDowns.forEach(cd -> {
                    if (cd.getCoolDown() > 0)
                        cd.reduceCoolDown();
                });
            }
        });
    }

    @PostInitialize
    public void init() {
        this.runTaskTimer(BukkitPlugin.INSTANCE, 0, 20);

        MCSchedulers.getGlobalScheduler().scheduleAtFixedRate(() -> {
            //give player effects every tick
            Bukkit.getOnlinePlayers().forEach(p -> {
                if (!playerItemCoolDown.containsKey(p.getUniqueId()))
                    return;

                playerItemCoolDown.get(p.getUniqueId()).forEach(cd -> {
                    if (cd.getItemName() == "Axe" && axeItem.isSimilar(p.getItemInHand()) && cd.getCoolDown() > 0)
                        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 1, 100, false, false));
                });
            });

        }, 0, 1);
    }
}
