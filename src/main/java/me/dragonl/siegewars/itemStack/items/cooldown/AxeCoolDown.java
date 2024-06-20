package me.dragonl.siegewars.itemStack.items.cooldown;

import java.util.UUID;

public class AxeCoolDown implements ItemCoolDown {
    private UUID playerUUID;
    private int coolDown;

    public AxeCoolDown(UUID playerUUID, int coolDown) {
        this.playerUUID = playerUUID;
        this.coolDown = coolDown;
    }

    @Override
    public UUID getPlayerUUID() {
        return playerUUID;
    }

    @Override
    public String getItemName() {
        return "Axe";
    }

    @Override
    public int getCoolDown() {
        return coolDown;
    }

    @Override
    public void setCoolDown(int coolDown) {
        this.coolDown = coolDown;
    }

    @Override
    public void reduceCoolDown() {
        if (coolDown > 0) {
            coolDown--;
        }
    }
}
