package me.dragonl.siegewars.yaml.element;

import io.fairyproject.config.annotation.ConfigurationElement;
import io.fairyproject.config.annotation.ElementType;
import io.fairyproject.container.InjectableComponent;
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

    public MapConfigElement(){

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
}
