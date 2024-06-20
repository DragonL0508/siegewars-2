package me.dragonl.siegewars.itemStack.items.cooldown;

import java.util.UUID;

public interface ItemCoolDown {
    UUID getPlayerUUID();
    String getItemName();
    int getCoolDown();
    void setCoolDown(int coolDown);
    void reduceCoolDown();
}
