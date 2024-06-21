package me.dragonl.siegewars.game.kit.allKits;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import me.dragonl.siegewars.game.kit.SiegeWarsKit;
import me.dragonl.siegewars.itemStack.items.ability.AttackerAbilityItem;
import me.dragonl.siegewars.team.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class AttackerKit implements SiegeWarsKit {
    private final TeamManager teamManager;
    private final AttackerAbilityItem attackerAbilityItem;

    public AttackerKit(TeamManager teamManager, AttackerAbilityItem attackerAbilityItem) {
        this.teamManager = teamManager;
        this.attackerAbilityItem = attackerAbilityItem;
    }

    @Override
    public String getID() {
        return "attacker";
    }

    @Override
    public String getKitName() {
        return "突襲者";
    }

    @Override
    public String getKitChar() {
        return "§c§l突";
    }

    @Override
    public ItemStack getKitIcon() {
        return ItemBuilder.of(XMaterial.IRON_SWORD).build();
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
                        .build(),
                ItemBuilder.of(XMaterial.AIR).build(),
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
                ItemBuilder.of(XMaterial.IRON_SWORD)
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
        return attackerAbilityItem.get(player);
    }

    @Override
    public Boolean useAbility(Player player) {
        World world = player.getWorld();
        PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, 60, 4, false, false);
        PotionEffect blind = new PotionEffect(PotionEffectType.BLINDNESS, 50, 0, false, false);
        player.addPotionEffect(speed);
        player.addPotionEffect(blind);
        world.playSound(player.getLocation(), Sound.WITHER_SHOOT, 1, 1.35f);
        return true;
    }
}
