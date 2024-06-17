package me.dragonl.siegewars.game.kit;

import io.fairyproject.container.InjectableComponent;
import me.dragonl.siegewars.game.Kit;
import me.dragonl.siegewars.game.events.PlayerSelectKitEvent;
import me.dragonl.siegewars.game.kit.allKits.*;
import me.dragonl.siegewars.itemStack.RemoveCustomItem;
import me.dragonl.siegewars.itemStack.items.ability.*;
import me.dragonl.siegewars.player.NameGetter;
import me.dragonl.siegewars.player.PlayerKitManager;
import me.dragonl.siegewars.team.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@InjectableComponent
public class KitSelectLogic {
    private final PlayerKitManager playerKitManager;
    private final TeamManager teamManager;
    private final AttackerAbilityItem attackerAbilityItem;
    private final ArcherAbilityItem archerAbilityItem;
    private final SpecialAbilityItem specialAbilityItem;
    private final HealerAbilityItem healerAbilityItem;
    private final ReaperAbilityItem reaperAbilityItem;
    private final NameGetter nameGetter;
    private final KitInfoGetter kitInfoGetter;

    public KitSelectLogic(
            PlayerKitManager playerKitManager
            , TeamManager teamManager
            , AttackerAbilityItem attackerAbilityItem
            , ArcherAbilityItem archerAbilityItem
            , SpecialAbilityItem specialAbilityItem
            , HealerAbilityItem healerAbilityItem
            , ReaperAbilityItem reaperAbilityItem
            , NameGetter nameGetter, KitInfoGetter kitInfoGetter) {
        this.playerKitManager = playerKitManager;
        this.teamManager = teamManager;
        this.attackerAbilityItem = attackerAbilityItem;
        this.archerAbilityItem = archerAbilityItem;
        this.specialAbilityItem = specialAbilityItem;
        this.healerAbilityItem = healerAbilityItem;
        this.reaperAbilityItem = reaperAbilityItem;
        this.nameGetter = nameGetter;
        this.kitInfoGetter = kitInfoGetter;
    }

    public void kitSelect(Player player, Kit kit) {
        SiegeWarsKit attacker = new KitAttacker(playerKitManager, teamManager, attackerAbilityItem, nameGetter, kitInfoGetter);
        SiegeWarsKit archer = new KitArcher(playerKitManager, teamManager, nameGetter, archerAbilityItem, kitInfoGetter);
        SiegeWarsKit healer = new KitHealer(playerKitManager, teamManager, nameGetter, healerAbilityItem, kitInfoGetter);
        SiegeWarsKit special = new KitSpecial(playerKitManager, teamManager, nameGetter, specialAbilityItem, kitInfoGetter);
        SiegeWarsKit tank = new KitTank(playerKitManager, teamManager, nameGetter, kitInfoGetter);
        SiegeWarsKit reaper = new KitReaper(playerKitManager, teamManager, nameGetter, reaperAbilityItem, kitInfoGetter);

        if(kit == Kit.ATTACKER) attacker.selectThisKit(player, kit);
        if(kit == Kit.ARCHER) archer.selectThisKit(player, kit);
        if(kit == Kit.HEALER) healer.selectThisKit(player, kit);
        if(kit == Kit.SPECIAL) special.selectThisKit(player, kit);
        if(kit == Kit.TANK) tank.selectThisKit(player, kit);
        if(kit == Kit.REAPER) reaper.selectThisKit(player, kit);

        Bukkit.getPluginManager().callEvent(new PlayerSelectKitEvent(player, kit));
    }
}
