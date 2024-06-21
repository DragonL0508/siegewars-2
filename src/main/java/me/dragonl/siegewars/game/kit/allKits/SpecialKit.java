package me.dragonl.siegewars.game.kit.allKits;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.mc.scheduler.MCSchedulers;
import io.fairyproject.scheduler.repeat.RepeatPredicate;
import me.dragonl.siegewars.game.events.SpecialAbilityEndEvent;
import me.dragonl.siegewars.game.events.SpecialAbilityStartEvent;
import me.dragonl.siegewars.game.kit.SiegeWarsKit;
import me.dragonl.siegewars.itemStack.items.ability.AttackerAbilityItem;
import me.dragonl.siegewars.itemStack.items.ability.SpecialAbilityItem;
import me.dragonl.siegewars.team.NametagVisibility;
import me.dragonl.siegewars.team.Team;
import me.dragonl.siegewars.team.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class SpecialKit implements SiegeWarsKit {
    private final TeamManager teamManager;
    private final SpecialAbilityItem specialAbilityItem;

    public SpecialKit(TeamManager teamManager, SpecialAbilityItem specialAbilityItem) {
        this.teamManager = teamManager;
        this.specialAbilityItem = specialAbilityItem;
    }

    @Override
    public String getID() {
        return "special";
    }

    @Override
    public String getKitName() {
        return "特種兵";
    }

    @Override
    public String getKitChar() {
        return "§9§l特";
    }

    @Override
    public ItemStack getKitIcon() {
        return ItemBuilder.of(XMaterial.BLAZE_POWDER).build();
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "&8先鋒"
                , ""
                , "&7傷害 &c||||||||||&7||||||||||||||||||||"
                , "&7防禦 &a||||||||||||||||||||&7||||||||||"
                , ""
                , "&e特種兵的能力值很平均"
                , "&e在探勘與實戰中很具優勢"
                , "&e在大多數的情況下能夠自由穿梭戰場"
                , "&e並給與隊友戰術協助"
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
                ItemBuilder.of(XMaterial.GOLDEN_CHESTPLATE)
                        .editMeta(m -> {
                            m.spigot().setUnbreakable(true);
                        })
                        .build(),
                ItemBuilder.of(XMaterial.AIR).build(),
                ItemBuilder.of(XMaterial.GOLDEN_BOOTS)
                        .editMeta(m -> {
                            m.spigot().setUnbreakable(true);
                        })
                        .build()
        };
    }

    @Override
    public ItemStack[] getItems() {
        return new ItemStack[]{
                ItemBuilder.of(XMaterial.STONE_SWORD)
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
        return specialAbilityItem.get(player);
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
}
