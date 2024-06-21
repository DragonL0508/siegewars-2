package me.dragonl.siegewars.game.kit.allKits;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import me.dragonl.siegewars.game.kit.SiegeWarsKit;
import me.dragonl.siegewars.itemStack.items.ability.ArcherAbilityItem;
import me.dragonl.siegewars.itemStack.items.ability.HealerAbilityItem;
import me.dragonl.siegewars.team.TeamManager;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class HealerKit implements SiegeWarsKit {
    private final TeamManager teamManager;
    private final HealerAbilityItem healerAbilityItem;

    public HealerKit(TeamManager teamManager, HealerAbilityItem healerAbilityItem) {
        this.teamManager = teamManager;
        this.healerAbilityItem = healerAbilityItem;
    }

    @Override
    public String getID() {
        return "healer";
    }

    @Override
    public String getKitName() {
        return "治癒使者";
    }

    @Override
    public String getKitChar() {
        return "§a§l治";
    }

    @Override
    public ItemStack getKitIcon() {
        return ItemBuilder.of(XMaterial.POTION)
                .data(8261)
                .build();
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
                ItemBuilder.of(XMaterial.LEATHER_CHESTPLATE)
                        .editMeta(m -> {
                            m.spigot().setUnbreakable(true);
                        })
                        .color(Color.WHITE)
                        .enchantment(XEnchantment.PROTECTION_ENVIRONMENTAL,1)
                        .build(),
                ItemBuilder.of(XMaterial.LEATHER_LEGGINGS)
                        .editMeta(m -> {
                            m.spigot().setUnbreakable(true);
                        })
                        .color(Color.RED)
                        .build(),
                ItemBuilder.of(XMaterial.IRON_BOOTS)
                        .editMeta(m -> {
                            m.spigot().setUnbreakable(true);
                        })
                        .build()
        };
    }

    @Override
    public ItemStack[] getItems() {
        return new ItemStack[]{
                ItemBuilder.of(XMaterial.STONE_AXE)
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
        return healerAbilityItem.get(player);
    }

    @Override
    public Boolean useAbility(Player player) {
        List<Entity> healTargets = new ArrayList<>(player.getWorld().getNearbyEntities(player.getLocation(), 10, 10, 10));
        AtomicReference<Boolean> abilityUse = new AtomicReference<>(false);

        healTargets.forEach(e -> {
            if(e.getType() == EntityType.PLAYER){
                Player targetPlayer = (Player) e;
                if(teamManager.getPlayerTeam(player) == teamManager.getPlayerTeam(targetPlayer) && targetPlayer != player){
                    playerHeal(player, targetPlayer);
                    abilityUse.set(true);
                }
            }
        });
        //effects
        if(abilityUse.get()){
            player.getWorld().playSound(player.getLocation(), Sound.SPIDER_DEATH, 1.5f, 1f);
            player.getWorld().playSound(player.getLocation(), Sound.DRINK, 0.5f, 0.8f);
        }

        return abilityUse.get();
    }

    private void playerHeal(Player player, Player target){
        PotionEffect regen = new PotionEffect(PotionEffectType.REGENERATION, 320, 1, false, false);
        PotionEffect absorption = new PotionEffect(PotionEffectType.ABSORPTION, 400, 1, false, false);
        target.addPotionEffect(regen);
        target.addPotionEffect(absorption);
        target.getWorld().spigot().playEffect(target.getLocation().add(0,1.75,0), Effect.HEART, 1, 0, 0.5f, 0, 0.5f, 0, 3, 32);
    }
}
