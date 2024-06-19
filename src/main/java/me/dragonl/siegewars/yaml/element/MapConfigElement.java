package me.dragonl.siegewars.yaml.element;

import io.fairyproject.config.annotation.ConfigurationElement;
import io.fairyproject.config.annotation.ElementType;
import io.fairyproject.mc.util.Position;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@ConfigurationElement
public class MapConfigElement {
    private Position attackSpawn = new Position();
    private Position defendSpawn = new Position();
    private Position specSpawn = new Position();

    @ElementType(Position.class)
    private List<Position> attackSpot = Arrays.asList();
    @ElementType(Position.class)
    private List<Position> defendSpot = Arrays.asList();
    @ElementType(DestroyableWallElement.class)
    private List<DestroyableWallElement> destroyableWallList = new ArrayList<>();
    @ElementType(DestroyableWindowElement.class)
    private List<DestroyableWindowElement> destroyableWindowList = new ArrayList<>();

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

    public List<Position> getAttackSpot() {
        return attackSpot;
    }

    public void setAttackSpot(List<Position> attackSpot) {
        this.attackSpot = attackSpot;
    }

    public List<Position> getDefendSpot() {
        return defendSpot;
    }

    public void setDefendSpot(List<Position> defendSpot) {
        this.defendSpot = defendSpot;
    }

    public MapConfigElement() {

    }

    public Position getAttackSpawn() {
        return attackSpawn;
    }

    public void setAttackSpawn(Position attackSpawn) {
        this.attackSpawn = attackSpawn;
    }

    public Position getDefendSpawn() {
        return defendSpawn;
    }

    public void setDefendSpawn(Position defendSpawn) {
        this.defendSpawn = defendSpawn;
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
}
