package me.dragonl.siegewars.game.kit.allKits;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bootstrap.bukkit.BukkitPlugin;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.mc.scheduler.MCSchedulers;
import io.fairyproject.scheduler.ScheduledTask;
import io.fairyproject.scheduler.repeat.RepeatPredicate;
import me.dragonl.siegewars.game.kit.SiegeWarsKit;
import me.dragonl.siegewars.itemStack.items.ability.HealerAbilityItem;
import me.dragonl.siegewars.itemStack.items.ability.ReaperAbilityItem;
import me.dragonl.siegewars.team.TeamManager;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public class ReaperKit implements SiegeWarsKit {
    private final TeamManager teamManager;
    private final ReaperAbilityItem reaperAbilityItem;

    public ReaperKit(TeamManager teamManager, ReaperAbilityItem reaperAbilityItem) {
        this.teamManager = teamManager;
        this.reaperAbilityItem = reaperAbilityItem;
    }

    @Override
    public String getID() {
        return "reaper";
    }

    @Override
    public String getKitName() {
        return "死神";
    }

    @Override
    public String getKitChar() {
        return "§8§l死";
    }

    @Override
    public ItemStack getKitIcon() {
        return ItemBuilder.of(XMaterial.STONE_HOE).build();
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "&8先鋒"
                , ""
                , "&7傷害 &c||||||||||&7||||||||||||||||||||"
                , "&7防禦 &6|||||||||||||||&7|||||||||||||||"
                , ""
                , "&e死神是一個適合擾亂敵方節奏的角色"
                , "&e身為先鋒，你可以最先探勘敵情"
                , "&e並通知隊友敵方的狀態"
                , "&e無論是進攻或防守都有很大的發揮空間"
                , ""
                , "&7點擊選擇"
        };
    }

    @Override
    public ItemStack[] getArmors(Player player) {
        return new ItemStack[]{
                ItemBuilder.of(XMaterial.LEATHER_HELMET)
                        .color(teamManager.getPlayerTeam(player).getBukkitColor())
                        .enchantment(XEnchantment.PROTECTION_PROJECTILE, 2)
                        .editMeta(m -> {
                            m.spigot().setUnbreakable(true);
                        })
                        .build(),
                ItemBuilder.of(XMaterial.CHAINMAIL_CHESTPLATE)
                        .editMeta(m -> {
                            m.spigot().setUnbreakable(true);
                        })
                        .build(),
                ItemBuilder.of(XMaterial.LEATHER_LEGGINGS)
                        .editMeta(m -> {
                            m.spigot().setUnbreakable(true);
                        })
                        .color(Color.BLACK)
                        .build(),
                ItemBuilder.of(XMaterial.LEATHER_BOOTS)
                        .editMeta(m -> {
                            m.spigot().setUnbreakable(true);
                        })
                        .build()
        };
    }

    @Override
    public ItemStack[] getItems() {
        return new ItemStack[]{
                ItemBuilder.of(XMaterial.WOODEN_SWORD)
                        .editMeta(m -> {
                            m.spigot().setUnbreakable(true);
                        })
                        .build(),
                ItemBuilder.of(XMaterial.FISHING_ROD)
                        .editMeta(m -> {
                            m.spigot().setUnbreakable(true);
                        })
                        .build(),
                ItemBuilder.of(XMaterial.BOW)
                        .editMeta(m -> {
                            m.spigot().setUnbreakable(true);
                        })
                        .build(),
                ItemBuilder.of(XMaterial.ARROW)
                        .amount(8)
                        .build()
        };
    }

    @Override
    public ItemStack getAbilityItem(Player player) {
        return reaperAbilityItem.get(player);
    }

    @Override
    public Boolean useAbility(Player player) {
        PotionEffect
                resistance = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 140, 1, false, false),
                invisibility = new PotionEffect(PotionEffectType.INVISIBILITY, 140, 0, false, false),
                weekness = new PotionEffect(PotionEffectType.WEAKNESS, 140, 1, false, false),
                speed = new PotionEffect(PotionEffectType.SPEED, 140, 0, false, false),
                blind = new PotionEffect(PotionEffectType.BLINDNESS, 40, 0, false, false),
                slowness = new PotionEffect(PotionEffectType.SLOW, 1, 0, false, false),
                wither = new PotionEffect(PotionEffectType.WITHER, 20, 0, false, false);
        player.addPotionEffect(resistance);
        player.addPotionEffect(invisibility);
        player.addPotionEffect(speed);
        player.addPotionEffect(weekness);

        CompletableFuture<?> future = MCSchedulers.getGlobalScheduler().scheduleAtFixedRate(() -> {
            player.getWorld().getNearbyEntities(player.getLocation(), 2, 2, 2).forEach(e -> {
                if (e.getType() == EntityType.PLAYER) {
                    Player target = (Player) e;
                    if (teamManager.swGetAnotherTeam(player) == teamManager.getPlayerTeam(target)) {
                        target.addPotionEffect(blind);
                        target.addPotionEffect(slowness);
                        target.addPotionEffect(wither);
                    }
                }
            });
            //player visual effects
            player.getWorld().spigot().playEffect(player.getLocation().add(0, 1, 0), Effect.LARGE_SMOKE, 1, 0, 0.15f, 0.4f, 0.15f, 0, 10, 32);
        }, 0, 1, RepeatPredicate.length(Duration.ofSeconds(7))).getFuture();

        future.thenRun(() -> {
            player.getWorld().playSound(player.getLocation(), Sound.FIZZ, 1, 0.5f);
            player.getWorld().spigot().playEffect(player.getLocation().add(0, 1, 0), Effect.LARGE_SMOKE, 1, 0, 0.15f, 0.4f, 0.15f, 0.5f, 50, 32);
        });

        player.getWorld().playSound(player.getLocation(), Sound.ENDERMAN_SCREAM, 1, 0.35f);
        player.getWorld().playSound(player.getLocation(), Sound.PORTAL_TRIGGER, 1, 1.75f);

        return true;
    }
}
