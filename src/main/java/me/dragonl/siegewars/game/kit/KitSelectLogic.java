package me.dragonl.siegewars.game.kit;

import io.fairyproject.container.InjectableComponent;
import me.dragonl.siegewars.game.Kit;
import me.dragonl.siegewars.game.kit.allKits.*;
import me.dragonl.siegewars.itemStack.RemoveCustomItem;
import me.dragonl.siegewars.itemStack.items.ability.AttackerAbilityItem;
import me.dragonl.siegewars.player.PlayerKitManager;
import me.dragonl.siegewars.team.TeamManager;
import org.bukkit.entity.Player;

@InjectableComponent
public class KitSelectLogic {
    private final PlayerKitManager playerKitManager;
    private final TeamManager teamManager;
    private final AttackerAbilityItem attackerAbilityItem;
    private final RemoveCustomItem removeCustomItem;

    public KitSelectLogic(PlayerKitManager playerKitManager, TeamManager teamManager, AttackerAbilityItem attackerAbilityItem, RemoveCustomItem removeCustomItem) {
        this.playerKitManager = playerKitManager;
        this.teamManager = teamManager;
        this.attackerAbilityItem = attackerAbilityItem;
        this.removeCustomItem = removeCustomItem;
    }

    public void kitSelect(Player player, Kit kit) {
        SiegeWarsKit attacker = new KitAttacker(playerKitManager, teamManager, attackerAbilityItem, removeCustomItem);
        SiegeWarsKit archer = new KitArcher(playerKitManager, teamManager);
        SiegeWarsKit healer = new KitHealer(playerKitManager, teamManager);
        SiegeWarsKit special = new KitSpecial(playerKitManager, teamManager);
        SiegeWarsKit tank = new KitTank(playerKitManager, teamManager);
        SiegeWarsKit reaper = new KitReaper(playerKitManager, teamManager);
        switch (kit) {
            case ATTACKER -> attacker.selectThisKit(player, kit);
            case ARCHER -> archer.selectThisKit(player, kit);
            case HEALER -> healer.selectThisKit(player, kit);
            case SPECIAL -> special.selectThisKit(player, kit);
            case TANK -> tank.selectThisKit(player, kit);
            case REAPER -> reaper.selectThisKit(player, kit);
        }
    }
}
