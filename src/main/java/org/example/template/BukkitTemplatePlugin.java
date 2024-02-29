package org.example.template;

import io.fairyproject.FairyLaunch;
import io.fairyproject.log.Log;
import io.fairyproject.plugin.Plugin;

@FairyLaunch
public class BukkitTemplatePlugin extends Plugin {

    @Override
    public void onPluginEnable() {
        Log.info("Plugin Enabled.");
    }

}
