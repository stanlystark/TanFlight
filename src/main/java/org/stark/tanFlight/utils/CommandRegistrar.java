package org.stark.tanFlight.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.List;

import static org.stark.tanFlight.TanFlight.LOGGER;

public class CommandRegistrar {

    public static void registerCommand(
            Plugin plugin,
            String name,
            CommandExecutor executor,
            String description,
            @Nullable String permission
    ) {
        registerCommand(plugin, name, executor, description, permission,
                (executor instanceof TabCompleter tabCompleter) ? tabCompleter : null);
    }
    public static void registerCommand(Plugin plugin, String name, CommandExecutor executor, String description, @Nullable String permission, @Nullable TabCompleter tabCompleter) {
        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());

            Command command = new BukkitCommand(name) {
                @Override
                public boolean execute(CommandSender sender, String label, String[] args) {
                    return executor.onCommand(sender, this, label, args);
                }

                @Override
                public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
                    return tabCompleter != null
                            ? tabCompleter.onTabComplete(sender, this, alias, args)
                            : super.tabComplete(sender, alias, args);
                }
            };

            if (permission != null) command.setPermission(permission);
            command.setDescription(description);
            command.setUsage("/" + name);

            commandMap.register(plugin.getDescription().getName(), command);
            LOGGER.info("Command /" + name + " successfully registered.");

        } catch (Exception e) {
            LOGGER.severe("Failed to register the command /" + name + ": " + e.getMessage());
        }
    }
}
