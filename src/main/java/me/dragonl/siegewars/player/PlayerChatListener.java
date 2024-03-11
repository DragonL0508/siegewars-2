package me.dragonl.siegewars.player;


import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import me.dragonl.siegewars.team.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChannelEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

@InjectableComponent
@RegisterAsListener
public class PlayerChatListener implements Listener {
    private final NameGetter nameGetter;
    private final TeamManager teamManager;

    public PlayerChatListener(NameGetter nameGetter, TeamManager teamManager) {
        this.nameGetter = nameGetter;
        this.teamManager = teamManager;
    }

    @EventHandler
    public void onChat(PlayerChatEvent event) {

        event.setCancelled(true);

        Player player = event.getPlayer();
        if (teamManager.isInTeam(player)) {
            if(teamManager.getPlayerTeam(player).isPrivateChat()){
                for(UUID uuid : teamManager.getPlayerTeam(player).getPlayers()){
                    Player p = Bukkit.getPlayer(uuid);
                    p.sendMessage( ChatColor.YELLOW + "[隊伍頻道] " + nameGetter.getChatName(player) + ChatColor.DARK_GRAY + " : " + ChatColor.WHITE + event.getMessage());
                }
                return;
            }
        }
        Bukkit.getServer().broadcastMessage(nameGetter.getChatName(player) + ChatColor.DARK_GRAY + " : " + ChatColor.WHITE + event.getMessage());
    }
}
