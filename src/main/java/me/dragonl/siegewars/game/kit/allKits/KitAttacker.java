package me.dragonl.siegewars.game.kit.allKits;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import me.dragonl.siegewars.game.kit.SiegeWarsAbstractKit;
import me.dragonl.siegewars.itemStack.RemoveCustomItem;
import me.dragonl.siegewars.itemStack.items.ability.AttackerAbilityItem;
import me.dragonl.siegewars.player.PlayerKitManager;
import me.dragonl.siegewars.team.TeamManager;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

@InjectableComponent
public class KitAttacker extends SiegeWarsAbstractKit {
    private final AttackerAbilityItem attackerAbilityItem;
    private final RemoveCustomItem removeCustomItem;

    public KitAttacker(PlayerKitManager playerKitManager, TeamManager teamManager, AttackerAbilityItem attackerAbilityItem, RemoveCustomItem removeCustomItem) {
        super(playerKitManager, teamManager);
        this.attackerAbilityItem = attackerAbilityItem;
        this.removeCustomItem = removeCustomItem;
    }

    @Override
    public void useAbility(Player player) {
        World world = player.getWorld();
        PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, 60, 3, false, false);
        PotionEffect blind = new PotionEffect(PotionEffectType.BLINDNESS, 50, 0, false, false);
        player.addPotionEffect(speed);
        player.addPotionEffect(blind);
        world.playSound(player.getLocation(), Sound.WITHER_SHOOT, 1, 1.35f);

        removeCustomItem.removeCustomItem(player, Arrays.asList(attackerAbilityItem));
    }

    @Override
    protected void giveKitItems(Player player) {
        PlayerInventory inv = player.getInventory();
        inv.setHelmet(ItemBuilder.of(XMaterial.LEATHER_HELMET)
                .color(teamManager.getPlayerTeam(player).getBukkitColor())
                .enchantment(XEnchantment.PROTECTION_PROJECTILE, 2)
                .build());
        inv.setChestplate(ItemBuilder.of(XMaterial.LEATHER_CHESTPLATE)
                .build());
        inv.setBoots(ItemBuilder.of(XMaterial.LEATHER_BOOTS)
                .build());
        inv.setItem(0, ItemBuilder.of(XMaterial.IRON_SWORD)
                .build());
        inv.setItem(1, ItemBuilder.of(XMaterial.FISHING_ROD)
                .build());
        inv.setItem(8, attackerAbilityItem.get(player));
    }
}