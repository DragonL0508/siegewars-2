package me.dragonl.siegewars.player;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import me.dragonl.siegewars.yaml.MainConfig;
import me.dragonl.siegewars.player.data.PlayerData;
import me.dragonl.siegewars.player.data.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.nio.Buffer;

@RegisterAsListener
@InjectableComponent
public class PlayerJoinListener implements Listener {
    private final PlayerDataManager playerDataManager;

    public PlayerJoinListener(PlayerDataManager playerDataManager) {
        this.playerDataManager = playerDataManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        //player must execute
        Player player = event.getPlayer();

        //hunger & health reset
        player.setFoodLevel(20);
        player.setHealth(20);

        //player Default Data
        if(!playerDataManager.hasData(player))
            playerDataManager.setPlayerData(player, new PlayerData());

    }
}
