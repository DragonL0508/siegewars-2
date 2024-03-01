package me.dragonl.siegewars;

import io.fairyproject.FairyLaunch;
import io.fairyproject.log.Log;
import io.fairyproject.plugin.Plugin;
import me.dragonl.siegewars.team.TeamManager;

@FairyLaunch
public class Siegewars extends Plugin {
    @Override
    public void onPluginEnable() {
        Log.info("Plugin Enabled.");
    }
}