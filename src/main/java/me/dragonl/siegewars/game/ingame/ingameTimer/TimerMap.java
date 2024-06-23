package me.dragonl.siegewars.game.ingame.ingameTimer;

public class TimerMap {
    String key;
    Timer value;

    public TimerMap(String key, Timer value){
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Timer getValue() {
        return value;
    }

    public void setValue(Timer value) {
        this.value = value;
    }
}
