package me.dragonl.siegewars.player;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.bukkit.util.LegacyAdventureUtil;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.MCPlayer;
import me.dragonl.siegewars.team.TeamManager;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@RegisterAsListener
@InjectableComponent
public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        //player must execute
        Player player = event.getPlayer();
        MCPlayer mcPlayer = MCPlayer.from(player);
        Component header = LegacyAdventureUtil.decode("&eSiege&6Wars &f&lII\n&7&m--------------------------------");
        Component footer = LegacyAdventureUtil.decode("&7&m-----------------------------------\n&r&7Made By DragonL");

        mcPlayer.sendPlayerListHeaderAndFooter(header, footer);

        //hunger & health reset
        player.setFoodLevel(20);
        player.setHealth(20);
    }
}
