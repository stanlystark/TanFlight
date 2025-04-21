package org.stark.tanFlight;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.stark.tanFlight.commands.TflyCommand;
import org.stark.tanFlight.listeners.FlightRegionListener;
import org.stark.tanFlight.utils.CommandRegistrar;
import java.util.logging.Logger;

public final class TanFlight extends JavaPlugin {

    public static final Logger LOGGER = Logger.getLogger(TanFlight.class.getName());

    private static TanFlight instance;

    @Override
    public void onEnable() {
        if (isTanPluginMissing()) {
            LOGGER.severe("Cannot find Towns and Nations...");
            setEnabled(false);
            return;
        }

        instance = this;
        saveDefaultConfig();

        LOGGER.info("Plugin enabled.");

        CommandRegistrar.registerCommand(this, "tanfly", new TflyCommand(), "Flight", null, new TflyCommand());
        getServer().getPluginManager().registerEvents(new FlightRegionListener(), this);
    }

    @Override
    public void onDisable() {
        LOGGER.info("Plugin disabled.");
    }

    public static TanFlight getInstance() {
        return instance;
    }

    private boolean isTanPluginMissing() {
        Plugin tanPlugin = getServer().getPluginManager().getPlugin("TownsAndNations");
        return tanPlugin == null || !tanPlugin.isEnabled();
    }
}
