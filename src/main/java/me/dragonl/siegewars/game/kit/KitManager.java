package me.dragonl.siegewars.game.kit;

import io.fairyproject.container.InjectableComponent;
import me.dragonl.siegewars.game.kit.allKits.*;
import me.dragonl.siegewars.itemStack.items.ability.*;
import me.dragonl.siegewars.team.TeamManager;

import java.util.HashMap;
import java.util.Map;

@InjectableComponent
public class KitManager {
    private Map<String, SiegeWarsKit> kits = new HashMap<>();

    public Map<String, SiegeWarsKit> getKits() {
        return kits;
    }

    public KitManager(TeamManager teamManager, AttackerAbilityItem attackerAbilityItem, ArcherAbilityItem archerAbilityItem, HealerAbilityItem healerAbilityItem, SpecialAbilityItem specialAbilityItem, ReaperAbilityItem reaperAbilityItem) {

        //register kits
        registerKit(new AttackerKit(teamManager, attackerAbilityItem));
        registerKit(new ArcherKit(teamManager, archerAbilityItem));
        registerKit(new HealerKit(teamManager, healerAbilityItem));
        registerKit(new SpecialKit(teamManager, specialAbilityItem));
        registerKit(new ReaperKit(teamManager, reaperAbilityItem));
        registerKit(new TankKit(teamManager));
        registerKit(new NoneKit());
    }

    public void registerKit(SiegeWarsKit kit) {
        kits.put(kit.getID(), kit);
    }

    public SiegeWarsKit getKit(String kitID) {
        return kits.get(kitID);
    }
}
