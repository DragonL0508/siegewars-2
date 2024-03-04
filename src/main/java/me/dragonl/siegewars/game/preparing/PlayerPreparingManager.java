package me.dragonl.siegewars.game.preparing;

import io.fairyproject.container.InjectableComponent;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@InjectableComponent
public class PlayerPreparingManager {
    Map<UUID, Boolean> playerPrepareMap = new HashMap<>();
    public boolean isPlayerPrepared(Player player){
        return playerPrepareMap.get(player.getUniqueId());
    }

    public void setPlayerPreparedMap(Player player, Boolean isPrepared){
        playerPrepareMap.put(player.getUniqueId(), isPrepared);
    }
}
