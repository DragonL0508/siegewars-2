package me.dragonl.siegewars.team;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Team {
    private List<UUID> players = new ArrayList<>();
    private String displayName;
    private String prefix = "";
    private String suffix = "";
    private ChatColor color = ChatColor.RESET;
    private boolean friendlyFire = true;
    private NametagVisibility nametagVisibility = NametagVisibility.always;
    private boolean privateChat = false;
    private String groupID = "";
    private boolean showNameTagToClicker = false;

    public boolean isShowNameTagToClicker() {
        return showNameTagToClicker;
    }

    public void setShowNameTagToClicker(boolean showNameTagToClicker) {
        this.showNameTagToClicker = showNameTagToClicker;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public boolean isPrivateChat() {
        return privateChat;
    }

    public void setPrivateChat(boolean privateChat) {
        this.privateChat = privateChat;
    }

    public List<UUID> getPlayers() {
        return players;
    }

    public List<UUID> getOnlinePlayers() {
        List<UUID> players = new ArrayList<>();
        this.players.forEach(uuid -> {
            if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(uuid)))
                players.add(uuid);
        });
        return players;
    }

    public List<Player> getOnlineBukkitPlayers() {
        List<Player> players = new ArrayList<>();
        this.players.forEach(uuid -> {
            if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(uuid)))
                players.add(Bukkit.getPlayer(uuid));
        });
        return players;
    }

    public List<Player> getBukkitPlayers() {
        List<Player> players = new ArrayList<>();
        this.players.forEach(uuid -> {
                players.add(Bukkit.getPlayer(uuid));
        });
        return players;
    }

    public void setPlayers(List<UUID> players) {
        this.players = players;
    }

    public void addPlayer(Player player) {
        players.add(player.getUniqueId());
    }

    public void removePlayer(Player player) {
        players.remove(player.getUniqueId());
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public ChatColor getColor() {
        return color;
    }

    public Color getBukkitColor(){
        return translateChatColorToColor(color);
    }

    public void setColor(ChatColor color) {
        this.color = color;
    }

    public boolean isFriendlyFire() {
        return friendlyFire;
    }

    public void setFriendlyFire(boolean friendlyFire) {
        this.friendlyFire = friendlyFire;
    }

    public NametagVisibility getNametagVisibility() {
        return nametagVisibility;
    }

    public void setNametagVisibility(NametagVisibility nametagVisibility) {
        this.nametagVisibility = nametagVisibility;
    }

    public Color translateChatColorToColor(ChatColor chatColor)
    {
        switch (chatColor) {
            case AQUA:
                return Color.AQUA;
            case BLACK:
                return Color.BLACK;
            case BLUE:
                return Color.BLUE;
            case DARK_AQUA:
                return Color.AQUA;
            case DARK_BLUE:
                return Color.BLUE;
            case DARK_GRAY:
                return Color.GRAY;
            case DARK_GREEN:
                return Color.GREEN;
            case DARK_PURPLE:
                return Color.PURPLE;
            case DARK_RED:
                return Color.RED;
            case GOLD:
                return Color.ORANGE;
            case GRAY:
                return Color.GRAY;
            case GREEN:
                return Color.GREEN;
            case LIGHT_PURPLE:
                return Color.PURPLE;
            case RED:
                return Color.RED;
            case WHITE:
                return Color.WHITE;
            case YELLOW:
                return Color.YELLOW;
            default:
                break;
        }

        return null;
    }
}
