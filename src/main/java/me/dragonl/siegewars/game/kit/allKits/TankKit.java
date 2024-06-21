package me.dragonl.siegewars.game.kit.allKits;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import me.dragonl.siegewars.game.kit.SiegeWarsKit;
import me.dragonl.siegewars.itemStack.items.ability.ArcherAbilityItem;
import me.dragonl.siegewars.team.TeamManager;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Arrays;

public class TankKit implements SiegeWarsKit {
    private final TeamManager teamManager;

    public TankKit(TeamManager teamManager) {
        this.teamManager = teamManager;
    }

    @Override
    public String getID() {
        return "tank";
    }

    @Override
    public String getKitName() {
        return "衝撞者";
    }

    @Override
    public String getKitChar() {
        return "§7§l坦";
    }

    @Override
    public ItemStack getKitIcon() {
        return ItemBuilder.of(XMaterial.IRON_CHESTPLATE).build();
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "&8輔助"
                , ""
                , "&7傷害 &c|||||&7|||||||||||||||||||||||||"
                , "&7防禦 &a||||||||||||||||||||&7||||||||||"
                , ""
                , "&e身為團戰最堅強的後盾"
                , "&e你必須跟緊你的隊友並適時幫助他們"
                , "&e雖然沒辦法對敵人造成太多傷害"
                , "&e但使用恰當可以為團隊帶來莫大的利益"
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
                ItemBuilder.of(XMaterial.IRON_CHESTPLATE)
                        .editMeta(m -> {
                            m.spigot().setUnbreakable(true);
                        })
                        .build(),
                ItemBuilder.of(XMaterial.LEATHER_LEGGINGS)
                        .editMeta(m -> {
                            m.spigot().setUnbreakable(true);
                        })
                        .color(Color.GRAY)
                        .build(),
                ItemBuilder.of(XMaterial.LEATHER_BOOTS)
                        .editMeta(m -> {
                            m.spigot().setUnbreakable(true);
                        })
                        .color(Color.WHITE)
                        .build()
        };
    }

    @Override
    public ItemStack[] getItems() {
        return new ItemStack[]{
                ItemBuilder.of(XMaterial.GOLDEN_PICKAXE)
                        .editMeta(m -> {
                            m.spigot().setUnbreakable(true);
                        })
                        .enchantment(XEnchantment.KNOCKBACK, 1)
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
        return new ItemStack(Material.GLOWSTONE_DUST);
    }

    @Override
    public Boolean useAbility(Player player) {
        return true;
    }
}
