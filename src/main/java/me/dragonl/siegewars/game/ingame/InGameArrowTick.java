package me.dragonl.siegewars.game.ingame;

import io.fairyproject.bootstrap.bukkit.BukkitPlugin;
import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.container.PostInitialize;
import me.dragonl.siegewars.team.Team;
import me.dragonl.siegewars.team.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@InjectableComponent
@RegisterAsListener
public class InGameArrowTick extends BukkitRunnable implements Listener {
    private final TeamManager teamManager;
    Map<Entity, UUID> arrows = new HashMap<>();

    public InGameArrowTick(TeamManager teamManager) {
        this.teamManager = teamManager;
    }

    @PostInitialize
    public void init(){
        this.runTaskTimer(BukkitPlugin.INSTANCE,0,1);
    }
    @Override
    public void run() {
        showArrowsParticle();
    }
    private void showArrowsParticle(){
        if(arrows.isEmpty()) return;
        arrows.forEach((e,uuid) -> {
            Player p = Bukkit.getPlayer(uuid);
            Team team = teamManager.getPlayerTeam(p);
            int R,G,B;
            R = team.getBukkitColor().getRed();
            G = team.getBukkitColor().getGreen();
            B = team.getBukkitColor().getBlue();
            R -= 255;

            if(e.isDead()) return;
            e.getWorld().spigot().playEffect(e.getLocation().add(0,0.2,0),Effect.COLOURED_DUST,0,1,(float) R/255,(float) G/255,(float) B/255,1,0,128);

            Arrow arrow = (Arrow) e;
            if(arrow.isOnGround()) arrow.remove();
        });
    }

    //events
    @EventHandler
    public void bowFire(EntityShootBowEvent event){
        Entity e = event.getEntity();
        if(e.getType() == EntityType.PLAYER){
            Player p = (Player) e;
            if(!teamManager.isInTeam(p)) return;
            arrows.put(event.getProjectile(), p.getUniqueId());
        }
    }
}
