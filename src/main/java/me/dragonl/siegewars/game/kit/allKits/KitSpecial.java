package me.dragonl.siegewars.game.kit.allKits;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.FairyLaunch;
import io.fairyproject.bootstrap.bukkit.BukkitPlugin;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.scheduler.MCSchedulers;
import io.fairyproject.scheduler.repeat.RepeatPredicate;
import me.dragonl.siegewars.game.events.SpecialAbilityEndEvent;
import me.dragonl.siegewars.game.events.SpecialAbilityStartEvent;
import me.dragonl.siegewars.game.kit.KitInfoGetter;
import me.dragonl.siegewars.game.kit.SiegeWarsAbstractKit;
import me.dragonl.siegewars.itemStack.items.ability.SpecialAbilityItem;
import me.dragonl.siegewars.player.NameGetter;
import me.dragonl.siegewars.player.PlayerKitManager;
import me.dragonl.siegewars.team.NametagVisibility;
import me.dragonl.siegewars.team.Team;
import me.dragonl.siegewars.team.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@InjectableComponent
public class KitSpecial extends SiegeWarsAbstractKit {
    private final SpecialAbilityItem specialAbilityItem;
    private final KitInfoGetter kitInfoGetter;
    public KitSpecial(
            PlayerKitManager playerKitManager
            , TeamManager teamManager
            , NameGetter nameGetter
            , SpecialAbilityItem specialAbilityItem, KitInfoGetter kitInfoGetter) {
        super(playerKitManager, teamManager, nameGetter, kitInfoGetter);
        this.specialAbilityItem = specialAbilityItem;
        this.kitInfoGetter = kitInfoGetter;
    }

    @Override
    public Boolean useAbility(Player player) {
        Team targetTeam = teamManager.swGetAnotherTeam(player);
        if(targetTeam.getNametagVisibility() == NametagVisibility.always)
            return false;
        targetTeam.setNametagVisibility(NametagVisibility.always);

        // show target position
        CompletableFuture<?> future = MCSchedulers.getGlobalScheduler().scheduleAtFixedRate(() -> {
            targetTeam.getPlayers().forEach(uuid -> {
                Player target = Bukkit.getPlayer(uuid);
                target.getWorld().spigot().playEffect(target.getLocation().add(0, 1, 0), Effect.FLAME, 1, 0, 0, 0, 0, 0.25f, 25, 32);
                target.getWorld().playSound(target.getLocation(), Sound.FIRE_IGNITE, 1, 0.3f);
                target.getWorld().playSound(target.getLocation(), Sound.FIZZ, 1, 1.25f);
            });
        }, 0, 20, RepeatPredicate.length(Duration.ofSeconds(7))).getFuture();
        future.thenRun(() -> {
            targetTeam.setNametagVisibility(NametagVisibility.hideForOtherTeams);
            Bukkit.getPluginManager().callEvent(new SpecialAbilityEndEvent(player));
        });

        Bukkit.getPluginManager().callEvent(new SpecialAbilityStartEvent(player));
        return true;
    }

    @Override
    protected void giveKitItems(Player player) {
        PlayerInventory inv = player.getInventory();
        inv.setHelmet(ItemBuilder.of(XMaterial.LEATHER_HELMET)
                .color(teamManager.getPlayerTeam(player).getBukkitColor())
                .enchantment(XEnchantment.PROTECTION_PROJECTILE, 2)
                .editMeta(m -> {
                    m.spigot().setUnbreakable(true);
                })
                .build());
        inv.setChestplate(ItemBuilder.of(XMaterial.GOLDEN_CHESTPLATE)
                .editMeta(m -> {
                    m.spigot().setUnbreakable(true);
                })
                .build());
        inv.setBoots(ItemBuilder.of(XMaterial.GOLDEN_BOOTS)
                .editMeta(m -> {
                    m.spigot().setUnbreakable(true);
                })
                .build());
        inv.setItem(0, ItemBuilder.of(XMaterial.STONE_SWORD)
                .editMeta(m -> {
                    m.spigot().setUnbreakable(true);
                })
                .build());
        inv.setItem(2, ItemBuilder.of(XMaterial.BOW)
                .editMeta(m -> {
                    m.spigot().setUnbreakable(true);
                })
                .build());
        inv.setItem(1, ItemBuilder.of(XMaterial.FISHING_ROD)
                .editMeta(m -> {
                    m.spigot().setUnbreakable(true);
                })
                .build());
        inv.setItem(8, specialAbilityItem.get(player));
        inv.setItem(9, ItemBuilder.of(XMaterial.ARROW)
                .amount(8)
                .build());
    }
}
