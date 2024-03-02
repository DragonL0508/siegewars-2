package me.dragonl.siegewars.game.kit;

import io.fairyproject.container.InjectableComponent;
import me.dragonl.siegewars.game.Kit;
import me.dragonl.siegewars.player.PlayerKitManager;
import org.bukkit.entity.Player;

@InjectableComponent
public class KitSelectLogic {
    private final PlayerKitManager playerKitManager;

    public KitSelectLogic(PlayerKitManager playerKitManager) {
        this.playerKitManager = playerKitManager;
    }
    public void kitSelect(Player player, Kit kit){
        SiegeWarsKit attacker = new Attacker(playerKitManager);

        if(kit == Kit.ATTACKER)
            attacker.selectThisKit(player);
    }
}
