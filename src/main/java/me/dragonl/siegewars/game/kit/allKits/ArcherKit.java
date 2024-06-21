package me.dragonl.siegewars.game.kit.allKits;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import me.dragonl.siegewars.game.kit.SiegeWarsKit;
import me.dragonl.siegewars.itemStack.items.ability.ArcherAbilityItem;
import me.dragonl.siegewars.itemStack.items.ability.AttackerAbilityItem;
import me.dragonl.siegewars.team.TeamManager;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public class ArcherKit implements SiegeWarsKit {
    private final TeamManager teamManager;
    private final ArcherAbilityItem archerAbilityItem;

    public ArcherKit(TeamManager teamManager, ArcherAbilityItem archerAbilityItem) {
        this.teamManager = teamManager;
        this.archerAbilityItem = archerAbilityItem;
    }

    @Override
    public String getID() {
        return "archer";
    }

    @Override
    public String getKitName() {
        return "弓箭手";
    }

    @Override
    public String getKitChar() {
        return "§e§l弓";
    }

    @Override
    public ItemStack getKitIcon() {
        return ItemBuilder.of(XMaterial.BOW).build();
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "&8輸出"
                , ""
                , "&7傷害 &a||||||||||||||||||||&7||||||||||"
                , "&7防禦 &6||||||||||&7||||||||||||||||||||"
                , ""
                , "&e弓箭手是一位強力的遠程角色"
                , "&e你必須善用地形的優勢"
                , "&e盡快在遠處擊殺敵人"
                , "&e因為只要戰線被拉進就會對你十分不利"
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
                ItemBuilder.of(XMaterial.BOW)
                        .editMeta(m -> {
                            m.spigot().setUnbreakable(true);
                        })
                        .build(),
                ItemBuilder.of(XMaterial.FISHING_ROD)
                        .editMeta(m -> {
                            m.spigot().setUnbreakable(true);
                        })
                        .build(),
                ItemBuilder.of(XMaterial.ARROW)
                        .amount(24)
                        .build()
        };
    }

    @Override
    public ItemStack getAbilityItem(Player player) {
        return archerAbilityItem.get(player);
    }

    @Override
    public Boolean useAbility(Player player) {
        player.setVelocity(player.getVelocity().setY(1.15));
        player.getWorld().spigot().playEffect(player.getLocation().add(0,0.1,0), Effect.CLOUD, 1, 0, 0.05f, 0.05f, 0.05f, 0.1f, 15, 32);
        player.getWorld().playSound(player.getLocation(), Sound.HORSE_ARMOR, 1, 1.25f);
        inventoryItemsUpdate(player);
        return true;
    }

    private void inventoryItemsUpdate(Player player){
        PlayerInventory inv = player.getInventory();
        Arrays.stream(inv.getContents()).forEach(itemStack -> {
            if(itemStack != null && itemStack.getType() == Material.BOW)
                itemStack.setItemMeta(ItemBuilder.of(itemStack)
                        .enchantment(XEnchantment.ARROW_DAMAGE)
                        .build().getItemMeta());
            if(itemStack != null && itemStack.getType() == Material.ARROW)
                inv.remove(itemStack);
        });
        inv.addItem(ItemBuilder.of(XMaterial.ARROW)
                .amount(8)
                .build());
    }
}
