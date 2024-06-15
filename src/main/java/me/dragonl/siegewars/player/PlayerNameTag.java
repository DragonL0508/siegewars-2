package me.dragonl.siegewars.player;

import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams;
import io.fairyproject.bukkit.util.LegacyAdventureUtil;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.MCPlayer;
import io.fairyproject.mc.nametag.NameTag;
import io.fairyproject.mc.nametag.NameTagAdapter;
import me.dragonl.siegewars.team.NametagVisibility;
import me.dragonl.siegewars.team.Team;
import me.dragonl.siegewars.team.TeamManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@InjectableComponent
public class PlayerNameTag extends NameTagAdapter {

    private final TeamManager teamManager;

    public PlayerNameTag(TeamManager teamManager) {
        super("nametag", 0);
        this.teamManager = teamManager;
    }

    @Override
    public NameTag fetch(MCPlayer player, MCPlayer target) {
        Player bukkitTarget = target.as(Player.class);
        Player bukkitPlayer = player.as(Player.class);
        WrapperPlayServerTeams.NameTagVisibility nameTagVisibility = WrapperPlayServerTeams.NameTagVisibility.ALWAYS;
        //nametag visibility option
        if (teamManager.getPlayerTeam(bukkitPlayer) == teamManager.getPlayerTeam(bukkitTarget) && teamManager.getPlayerTeam(bukkitTarget).getNametagVisibility() == NametagVisibility.hideForOwnTeams)
            nameTagVisibility = WrapperPlayServerTeams.NameTagVisibility.NEVER;
        else if (teamManager.getPlayerTeam(bukkitPlayer) != teamManager.getPlayerTeam(bukkitTarget) && teamManager.getPlayerTeam(bukkitTarget).getNametagVisibility() == NametagVisibility.hideForOtherTeams)
            nameTagVisibility = WrapperPlayServerTeams.NameTagVisibility.NEVER;

        //output
        if (teamManager.isInTeam(bukkitTarget)) {
            Team team = teamManager.getPlayerTeam(bukkitTarget);
            Component prefix = LegacyAdventureUtil.decode(team.getPrefix());
            Component suffix = LegacyAdventureUtil.decode(team.getSuffix());
            TextColor color = LegacyComponentSerializer.parseChar(team.getColor().getChar()).color();
            return new NameTag(prefix, suffix, color, nameTagVisibility);
        }

        return new NameTag(LegacyAdventureUtil.decode("NoTeam "), Component.empty(), TextColor.color(255, 255, 255), nameTagVisibility);
    }
}