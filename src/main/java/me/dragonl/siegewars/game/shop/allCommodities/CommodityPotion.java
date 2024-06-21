package me.dragonl.siegewars.game.shop.allCommodities;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import me.dragonl.siegewars.game.shop.Commodity;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class CommodityPotion implements Commodity {
    @Override
    public String getID() {
        return "potion";
    }

    @Override
    public String getName() {
        return "§e治療藥水 §c2❤";
    }

    @Override
    public int getPrice() {
        return 500;
    }

    @Override
    public ItemStack getShopIcon() {
        return ItemBuilder.of(XMaterial.POTION)
                .name(getName())
                .lore("&e售價: &a" + getPrice() + "$"
                        , ""
                        , "§7重要的回血道具"
                        , "§7也許是逆轉戰局的關鍵"
                        , ""
                        , "")
                .data(16453)
                .itemFlag(ItemFlag.HIDE_POTION_EFFECTS)
                .build();
    }

    @Override
    public ItemStack getItemStack() {
        return ItemBuilder.of(XMaterial.POTION)
                .name(getName())
                .data(16453)
                .build();
    }
}
