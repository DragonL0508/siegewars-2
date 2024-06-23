package me.dragonl.siegewars.game.ingame;

import io.fairyproject.bootstrap.bukkit.BukkitPlugin;
import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.bukkit.util.BukkitPos;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.container.PostInitialize;
import me.dragonl.siegewars.game.MapObjectCatcher;
import me.dragonl.siegewars.game.MapObjectDestroyer;
import me.dragonl.siegewars.game.mapSetup.SetupWandManager;
import me.dragonl.siegewars.team.Team;
import me.dragonl.siegewars.team.TeamManager;
import me.dragonl.siegewars.yaml.MapConfig;
import me.dragonl.siegewars.yaml.element.MapConfigElement;
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

import java.util.*;

@InjectableComponent
@RegisterAsListener
public class InGameArrowTick extends BukkitRunnable implements Listener {
    private final TeamManager teamManager;
    private Map<Entity, UUID> arrows = new HashMap<>();
    private final MapObjectDestroyer mapObjectDestroyer;
    private final MapObjectCatcher mapObjectCatcher;
    private final MapConfig mapConfig;
    private final SetupWandManager setupWandManager;

    public InGameArrowTick(TeamManager teamManager, MapObjectDestroyer mapObjectDestroyer, MapObjectCatcher mapObjectCatcher, MapConfig mapConfig, SetupWandManager setupWandManager) {
        this.teamManager = teamManager;
        this.mapObjectDestroyer = mapObjectDestroyer;
        this.mapObjectCatcher = mapObjectCatcher;
        this.mapConfig = mapConfig;
        this.setupWandManager = setupWandManager;
    }

    @PostInitialize
    public void init() {
        this.runTaskTimer(BukkitPlugin.INSTANCE, 0, 1);
    }

    @Override
    public void run() {
        if (!arrows.isEmpty())
            showArrowsParticle();
    }

    private void showArrowsParticle() {
        List<Entity> deadArrows = new ArrayList<>();

        arrows.forEach((e, uuid) -> {
            Player p = Bukkit.getPlayer(uuid);
            Team team = teamManager.getPlayerTeam(p);
            int R, G, B;
            R = team.getBukkitColor().getRed();
            G = team.getBukkitColor().getGreen();
            B = team.getBukkitColor().getBlue();
            R -= 255;

            e.getWorld().spigot().playEffect(e.getLocation().add(0, 0.2, 0), Effect.COLOURED_DUST, 0, 1, (float) R / 255, (float) G / 255, (float) B / 255, 1, 0, 128);

            Arrow arrow = (Arrow) e;

            if (arrow.isOnGround()) {
                deadArrows.add(e);
                arrow.remove();
            }
        });

        deadArrows.forEach(arrow -> {
            arrows.remove(arrow);

            if(!mapObjectCatcher.isDestroyableWindow(BukkitPos.toMCPos(arrow.getLocation())))
                return;

            MapConfigElement element = mapConfig.getMaps().get(arrow.getWorld().getName());
            mapObjectDestroyer.destroyWindow(element.getWindowAtPosition(BukkitPos.toMCPos(arrow.getLocation())));
        });

        deadArrows.clear();
    }

    //events
    @EventHandler
    public void bowFire(EntityShootBowEvent event) {
        Entity e = event.getEntity();
        if (e.getType() == EntityType.PLAYER) {
            Player p = (Player) e;
            if (!teamManager.isInTeam(p)) return;

            arrows.put(event.getProjectile(), p.getUniqueId());
        }
    }
}
