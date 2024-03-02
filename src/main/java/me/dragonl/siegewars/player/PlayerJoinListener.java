package me.dragonl.siegewars.player;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.bukkit.util.LegacyAdventureUtil;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.MCPlayer;
import me.dragonl.siegewars.player.data.PlayerData;
import me.dragonl.siegewars.player.data.PlayerDataManager;
import me.dragonl.siegewars.team.TeamManager;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

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
