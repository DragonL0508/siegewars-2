package me.dragonl.siegewars.player;

import io.fairyproject.bootstrap.bukkit.BukkitPlugin;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.MCPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

@InjectableComponent
public class NameTagTemporaryManager {
    private Map<Player, Set<Player>> playerSetMap = new HashMap<>();

    public Map<Player, Set<Player>> getPlayerSetMap() {
        return playerSetMap;
    }

    public void setPlayerSetMap(Map<Player, Set<Player>> playerSetMap) {
        this.playerSetMap = playerSetMap;
    }

    public void startDisplayToPlayer(Player target, Player player){
        if(playerSetMap.containsKey(target)){
            playerSetMap.get(target).add(player);
        }
        else{
            playerSetMap.put(target, new HashSet<>(Arrays.asList(player)));
        }
    }
}
