package me.dragonl.siegewars.yaml.element;

import io.fairyproject.bukkit.util.BukkitPos;
import io.fairyproject.config.annotation.ConfigurationElement;
import io.fairyproject.config.annotation.ElementType;
import io.fairyproject.mc.util.Position;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@ConfigurationElement
public class MapConfigElement {
    private String mapName = "&e未知";
    private Position specSpawn = new Position();

    @ElementType(Position.class)
    private List<Position> attackSpawn = new ArrayList<>();
    @ElementType(Position.class)
    private List<Position> defendSpawn = new ArrayList<>();
    @ElementType(BombSiteElement.class)
    private List<BombSiteElement> bombSiteList = new ArrayList<>();
    @ElementType(DestroyableWallElement.class)
    private List<DestroyableWallElement> destroyableWallList = new ArrayList<>();
    @ElementType(DestroyableWindowElement.class)
    private List<DestroyableWindowElement> destroyableWindowList = new ArrayList<>();

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public List<BombSiteElement> getBombSiteList() {
        return bombSiteList;
    }

    public void setBombSiteList(List<BombSiteElement> bombSiteList) {
        this.bombSiteList = bombSiteList;
    }

    public List<Position> getAttackSpawn() {
        return attackSpawn;
    }

    public void setAttackSpawn(List<Position> attackSpawn) {
        this.attackSpawn = attackSpawn;
    }

    public List<Position> getDefendSpawn() {
        return defendSpawn;
    }

    public void setDefendSpawn(List<Position> defendSpawn) {
        this.defendSpawn = defendSpawn;
    }

    public List<DestroyableWindowElement> getDestroyableWindowList() {
        return destroyableWindowList;
    }

    public void setDestroyableWindowList(List<DestroyableWindowElement> destroyableWindowList) {
        this.destroyableWindowList = destroyableWindowList;
    }

    public List<DestroyableWallElement> getDestroyableWallList() {
        return destroyableWallList;
    }

    public void setDestroyableWallList(List<DestroyableWallElement> destroyableWallList) {
        this.destroyableWallList = destroyableWallList;
    }

    public MapConfigElement() {

    }

    public MapConfigElement(String mapName) {
        this.mapName = mapName;
    }

    public Position getSpecSpawn() {
        return specSpawn;
    }

    public void setSpecSpawn(Position specSpawn) {
        this.specSpawn = specSpawn;
    }

    public DestroyableWallElement getWallAtPosition(Position position) {
        AtomicReference<DestroyableWallElement> element = new AtomicReference<>(new DestroyableWallElement());

        destroyableWallList.forEach(wall -> {
            int minX = wall.getPosition2().getBlockX();
            int maxX = wall.getPosition1().getBlockX();
            int minY = wall.getPosition2().getBlockY();
            int maxY = wall.getPosition1().getBlockY();
            int minZ = wall.getPosition2().getBlockZ();
            int maxZ = wall.getPosition1().getBlockZ();

            if (position.getBlockX() >= minX && position.getBlockX() <= maxX
                    && position.getBlockY() >= minY && position.getBlockY() <= maxY
                    && position.getBlockZ() >= minZ && position.getBlockZ() <= maxZ)
                element.set(wall);

        });
        return element.get();
    }

    public DestroyableWindowElement getWindowAtPosition(Position position) {
        AtomicReference<DestroyableWindowElement> element = new AtomicReference<>(new DestroyableWindowElement());

        destroyableWindowList.forEach(window -> {
            int minX = window.getPosition2().getBlockX();
            int maxX = window.getPosition1().getBlockX();
            int minY = window.getPosition2().getBlockY();
            int maxY = window.getPosition1().getBlockY();
            int minZ = window.getPosition2().getBlockZ();
            int maxZ = window.getPosition1().getBlockZ();

            if (position.getBlockX() >= minX && position.getBlockX() <= maxX
                    && position.getBlockY() >= minY && position.getBlockY() <= maxY
                    && position.getBlockZ() >= minZ && position.getBlockZ() <= maxZ)
                element.set(window);

        });
        return element.get();
    }

    public Location getNextAttackSpawn(Location location) {
        AtomicInteger i = new AtomicInteger();
        attackSpawn.forEach(pos -> {
            Location thisLoc = BukkitPos.toBukkitLocation(pos);
            Block thisBlock = thisLoc.getBlock();
            Block inputBlock = location.getBlock();
            if (thisBlock.getX() == inputBlock.getX()
                    && thisBlock.getY() == inputBlock.getY()
                    && thisBlock.getZ() == inputBlock.getZ()) {
                i.set(attackSpawn.indexOf(pos));
            }
        });

        i.set(i.get() + 1);
        if (i.get() == attackSpawn.size())
            i.set(0);

        return BukkitPos.toBukkitLocation(attackSpawn.get(i.get()));
    }

    public Location getPreviousAttackSpawn(Location location) {
        AtomicInteger i = new AtomicInteger();
        attackSpawn.forEach(pos -> {
            Location thisLoc = BukkitPos.toBukkitLocation(pos);
            Block thisBlock = thisLoc.getBlock();
            Block inputBlock = location.getBlock();
            if (thisBlock.getX() == inputBlock.getX()
                    && thisBlock.getY() == inputBlock.getY()
                    && thisBlock.getZ() == inputBlock.getZ()) {
                i.set(attackSpawn.indexOf(pos));
            }
        });

        i.set(i.get() - 1);
        if (i.get() < 0)
            i.set(attackSpawn.size() - 1);

        return BukkitPos.toBukkitLocation(attackSpawn.get(i.get()));
    }
}
