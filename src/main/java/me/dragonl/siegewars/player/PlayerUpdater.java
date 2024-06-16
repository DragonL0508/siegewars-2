package me.dragonl.siegewars.player;

import io.fairyproject.bootstrap.bukkit.BukkitPlugin;
import io.fairyproject.bukkit.util.LegacyAdventureUtil;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.container.PostInitialize;
import io.fairyproject.mc.MCPlayer;
import io.fairyproject.mc.nametag.NameTagService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@InjectableComponent
public class PlayerUpdater extends BukkitRunnable {
    private final NameGetter nameGetter;

    public PlayerUpdater(NameGetter nameGetter) {
        this.nameGetter = nameGetter;
    }

    @PostInitialize
    public void init(){
        this.runTaskTimer(BukkitPlugin.INSTANCE,0,2);
    }

    @Override
    public void run() {
        for(Player player : Bukkit.getOnlinePlayers()){
            MCPlayer mcPlayer = MCPlayer.from(player);
            mcPlayer.setDisplayName(LegacyAdventureUtil.decode(nameGetter.getChatName(player)));
        }
    }
}
