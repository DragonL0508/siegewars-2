package me.dragonl.siegewars.player.data;

import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import io.fairyproject.container.InjectableComponent;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@InjectableComponent
public class PlayerDataManager {
    Map<UUID, PlayerData> playerDataMap = new HashMap<>();

    public PlayerData getPlayerData(Player player) {
        return playerDataMap.get(player.getUniqueId());
    }

    public void setPlayerData(Player player, PlayerData playerData) {
        playerDataMap.put(player.getUniqueId(), playerData);
    }

    public boolean hasData(Player player) {
        return playerDataMap.containsKey(player.getUniqueId());
    }
}
