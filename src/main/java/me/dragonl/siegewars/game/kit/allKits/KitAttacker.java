package me.dragonl.siegewars.game.kit.allKits;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import me.dragonl.siegewars.game.kit.KitInfoGetter;
import me.dragonl.siegewars.game.kit.SiegeWarsAbstractKit;
import me.dragonl.siegewars.itemStack.RemoveCustomItem;
import me.dragonl.siegewars.itemStack.items.ability.AttackerAbilityItem;
import me.dragonl.siegewars.player.NameGetter;
import me.dragonl.siegewars.player.PlayerKitManager;
import me.dragonl.siegewars.team.TeamManager;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

@InjectableComponent
public class KitAttacker extends SiegeWarsAbstractKit {
    private final AttackerAbilityItem attackerAbilityItem;
    private final KitInfoGetter kitInfoGetter;

    public KitAttacker(
            PlayerKitManager playerKitManager
            , TeamManager teamManager
            , AttackerAbilityItem attackerAbilityItem
            , NameGetter nameGetter, KitInfoGetter kitInfoGetter) {
        super(playerKitManager, teamManager, nameGetter, kitInfoGetter);
        this.attackerAbilityItem = attackerAbilityItem;
        this.kitInfoGetter = kitInfoGetter;
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
        inv.setChestplate(ItemBuilder.of(XMaterial.LEATHER_CHESTPLATE)
                .editMeta(m -> {
                    m.spigot().setUnbreakable(true);
                })
                .build());
        inv.setBoots(ItemBuilder.of(XMaterial.LEATHER_BOOTS)
                .editMeta(m -> {
                    m.spigot().setUnbreakable(true);
                })
                .build());
        inv.setItem(0, ItemBuilder.of(XMaterial.IRON_SWORD)
                .editMeta(m -> {
                    m.spigot().setUnbreakable(true);
                })
                .build());
        inv.setItem(1, ItemBuilder.of(XMaterial.FISHING_ROD)
                .editMeta(m -> {
                    m.spigot().setUnbreakable(true);
                })
                .build());
        inv.setItem(2, ItemBuilder.of(XMaterial.BOW)
                .editMeta(m -> {
                    m.spigot().setUnbreakable(true);
                })
                .build());
        inv.setItem(8, attackerAbilityItem.get(player));
        inv.setItem(9, ItemBuilder.of(XMaterial.ARROW)
                .amount(8)
                .build());
    }
}