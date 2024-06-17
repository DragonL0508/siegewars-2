package me.dragonl.siegewars.yaml.element;

import io.fairyproject.bukkit.util.BukkitPos;
import io.fairyproject.config.annotation.ConfigurationElement;
import io.fairyproject.config.annotation.ElementType;
import io.fairyproject.mc.util.Position;
import javafx.geometry.Pos;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ConfigurationElement
public class MapConfigElement {
    private Position attackSpawn = new Position();
    private Position defendSpawn = new Position();
    @ElementType(Position.class)
    private List<Position> attackSpot = Arrays.asList();
    @ElementType(Position.class)
    private List<Position> defendSpot = Arrays.asList();

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

    public MapConfigElement(Position attackSpawn, Position defendSpawn){
        this.attackSpawn = attackSpawn;
        this.defendSpawn = defendSpawn;
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
}
