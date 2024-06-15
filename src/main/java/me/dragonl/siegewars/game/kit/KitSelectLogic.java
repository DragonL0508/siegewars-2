package me.dragonl.siegewars.game.kit;

import io.fairyproject.container.InjectableComponent;
import me.dragonl.siegewars.game.Kit;
import me.dragonl.siegewars.game.kit.allKits.*;
import me.dragonl.siegewars.itemStack.RemoveCustomItem;
import me.dragonl.siegewars.itemStack.items.ability.ArcherAbilityItem;
import me.dragonl.siegewars.itemStack.items.ability.AttackerAbilityItem;
import me.dragonl.siegewars.itemStack.items.ability.SpecialAbilityItem;
import me.dragonl.siegewars.player.NameGetter;
import me.dragonl.siegewars.player.PlayerKitManager;
import me.dragonl.siegewars.team.TeamManager;
import org.bukkit.entity.Player;

@InjectableComponent
public class KitSelectLogic {
    private final PlayerKitManager playerKitManager;
    private final TeamManager teamManager;
    private final AttackerAbilityItem attackerAbilityItem;
    private final ArcherAbilityItem archerAbilityItem;
    private final SpecialAbilityItem specialAbilityItem;
    private final NameGetter nameGetter;

    public KitSelectLogic(
            PlayerKitManager playerKitManager
            , TeamManager teamManager
            , AttackerAbilityItem attackerAbilityItem
            , ArcherAbilityItem archerAbilityItem
            , SpecialAbilityItem specialAbilityItem
            , NameGetter nameGetter) {
        this.playerKitManager = playerKitManager;
        this.teamManager = teamManager;
        this.attackerAbilityItem = attackerAbilityItem;
        this.archerAbilityItem = archerAbilityItem;
        this.specialAbilityItem = specialAbilityItem;
        this.nameGetter = nameGetter;
    }

    public void kitSelect(Player player, Kit kit) {
        SiegeWarsKit attacker = new KitAttacker(playerKitManager, teamManager, attackerAbilityItem, nameGetter);
        SiegeWarsKit archer = new KitArcher(playerKitManager, teamManager, nameGetter, archerAbilityItem);
        SiegeWarsKit healer = new KitHealer(playerKitManager, teamManager, nameGetter);
        SiegeWarsKit special = new KitSpecial(playerKitManager, teamManager, nameGetter, specialAbilityItem);
        SiegeWarsKit tank = new KitTank(playerKitManager, teamManager, nameGetter);
        SiegeWarsKit reaper = new KitReaper(playerKitManager, teamManager, nameGetter);

        if(kit == Kit.ATTACKER) attacker.selectThisKit(player, kit);
        if(kit == Kit.ARCHER) archer.selectThisKit(player, kit);
        if(kit == Kit.HEALER) healer.selectThisKit(player, kit);
        if(kit == Kit.SPECIAL) special.selectThisKit(player, kit);
        if(kit == Kit.TANK) tank.selectThisKit(player, kit);
        if(kit == Kit.REAPER) reaper.selectThisKit(player, kit);
    }
}
