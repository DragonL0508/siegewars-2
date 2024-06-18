package me.dragonl.siegewars.player;

import io.fairyproject.bootstrap.bukkit.BukkitPlugin;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.container.PostInitialize;
import io.fairyproject.mc.MCPlayer;
import io.fairyproject.mc.nametag.NameTagService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

@InjectableComponent
public class NameTagTemporaryManager extends BukkitRunnable {
    private final NameTagService nameTagService;
    private Map<UUID, Set<UUID>> playerSetMap = new HashMap<>();
    private Map<UUID, Integer> playerCountDown = new HashMap<>();

    public Map<UUID, Integer> getPlayerCountDown() {
        return playerCountDown;
    }

    public void setPlayerCountDown(Map<UUID, Integer> playerCountDown) {
        this.playerCountDown = playerCountDown;
    }

    public NameTagTemporaryManager(NameTagService nameTagService) {
        this.nameTagService = nameTagService;
    }

    public Map<UUID, Set<UUID>> getPlayerSetMap() {
        return playerSetMap;
    }

    public void setPlayerSetMap(Map<UUID, Set<UUID>> playerSetMap) {
        this.playerSetMap = playerSetMap;
    }

    public void startDisplayToPlayer(Player target, Player player){
        if(playerSetMap.containsKey(target.getUniqueId())){
            playerSetMap.get(target.getUniqueId()).add(player.getUniqueId());
        }
        else{
            playerSetMap.put(target.getUniqueId(), new HashSet<>(Arrays.asList(player.getUniqueId())));
        }

        playerCountDown.put(target.getUniqueId(), 25);
        nameTagService.update(MCPlayer.from(target));
    }

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            UUID uuid = player.getUniqueId();
            if(playerCountDown.containsKey(uuid)){
                if(playerCountDown.get(uuid) > 0)
                    playerCountDown.put(uuid, playerCountDown.get(uuid) - 1);

                if(playerCountDown.get(uuid) == 0){
                    playerSetMap.get(uuid).clear();
                    nameTagService.update(MCPlayer.from(player));
                    playerCountDown.put(uuid, -1);
                }
            }
            else {
                playerCountDown.put(uuid, -1);
            }
        });
    }

    @PostInitialize
    public void init(){
        this.runTaskTimer(BukkitPlugin.INSTANCE, 0, 1);
    }
}
