package me.dragonl.siegewars.game.kit;

import com.cryptomorin.xseries.XMaterial;
import io.fairyproject.bukkit.util.items.ItemBuilder;
import io.fairyproject.container.InjectableComponent;
import me.dragonl.siegewars.game.Kit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@InjectableComponent
public class KitInfoGetter {

    public String getKitText(Kit kit) {
        switch (kit) {
            case NONE : return "";
            case ATTACKER : return "§c§l突";
            case ARCHER : return "§e§l弓";
            case TANK : return "§7§l坦";
            case HEALER : return "§a§l治";
            case SPECIAL : return "§9§l特";
            case REAPER : return "§8§l死";
        }
        return "Unknown";
    }
    public String getKitString(Kit kit){
        switch (kit) {
            case NONE : return "";
            case ATTACKER : return "突襲者";
            case ARCHER : return "弓箭手";
            case TANK : return "坦克";
            case HEALER : return "治療使者";
            case SPECIAL : return "特種兵";
            case REAPER : return "死神";
        }
        return "Unknown";
    }
    public ItemStack getKitIcon(Kit kit){
        switch (kit){
            case ATTACKER:
                return ItemBuilder.of(XMaterial.IRON_SWORD).build();
            case ARCHER:
                return ItemBuilder.of(XMaterial.BOW).build();
            case SPECIAL:
                return ItemBuilder.of(XMaterial.BLAZE_POWDER).build();
            case HEALER:
                return ItemBuilder.of(XMaterial.POTION).data(8261).build();
            case REAPER:
                return ItemBuilder.of(XMaterial.STONE_HOE).build();
            case TANK:
                return ItemBuilder.of(XMaterial.IRON_CHESTPLATE).build();
        }
        return ItemBuilder.of(XMaterial.STONE).build();
    }
}
