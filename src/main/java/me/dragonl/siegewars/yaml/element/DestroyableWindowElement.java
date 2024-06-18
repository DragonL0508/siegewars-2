package me.dragonl.siegewars.yaml.element;

import io.fairyproject.config.annotation.ConfigurationElement;
import io.fairyproject.mc.util.Position;

@ConfigurationElement
public class DestroyableWindowElement {
    private Position position1 = new Position();
    private Position position2 = new Position();
    public DestroyableWindowElement(){

    }
    public DestroyableWindowElement(Position position1, Position position2){
        this.position1 = position1;
        this.position2 = position2;
    }

    public Position getPosition1() {
        return position1;
    }

    public void setPosition1(Position position1) {
        this.position1 = position1;
    }

    public Position getPosition2() {
        return position2;
    }

    public void setPosition2(Position position2) {
        this.position2 = position2;
    }
}
