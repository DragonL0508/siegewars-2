package me.dragonl.siegewars.game.shop;

import io.fairyproject.container.InjectableComponent;
import me.dragonl.siegewars.game.kit.SiegeWarsKit;
import me.dragonl.siegewars.game.shop.allCommodities.CommodityPotion;

import java.util.HashMap;
import java.util.Map;

@InjectableComponent
public class CommodityManager {
    private Map<String, Commodity> commodities = new HashMap<>();

    public Map<String, Commodity> getCommodities() {
        return commodities;
    }

    public CommodityManager(){
        registerCommodity(new CommodityPotion());
    }

    public void registerCommodity(Commodity item) {
        commodities.put(item.getID(), item);
    }
}
