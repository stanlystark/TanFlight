package org.stark.tanFlight.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.stark.tanFlight.TanFlight;
import org.stark.tanFlight.utils.PermissionNodes;
import org.tan.api.TanAPI;
import org.tan.api.getters.TanClaimManager;
import org.tan.api.getters.TanPlayerManager;
import org.tan.api.interfaces.TanPlayer;
import org.tan.api.interfaces.TanTerritory;

import static org.stark.tanFlight.TanFlight.LOGGER;
import static org.stark.tanFlight.utils.CanFlightChecker.*;
import static org.stark.tanFlight.utils.FlightMessage.getMessage;
import static org.stark.tanFlight.utils.PermissionNodes.FLY_RELOAD;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public class TflyCommand implements CommandExecutor, TabCompleter {

    private final TanPlayerManager playerManager;
    private final TanClaimManager claimManager;

    public TflyCommand() {
        this.playerManager = TanAPI.getInstance().getPlayerManager();
        this.claimManager = TanAPI.getInstance().getClaimManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            // Проверка прав на перезагрузку конфига
            if (sender.hasPermission(FLY_RELOAD) || sender.isOp()) {
                TanFlight.getInstance().reloadConfig();
                sender.sendMessage(getMessage("configReloaded"));
            } else {
                sender.sendMessage(getMessage("noPermissions"));
            }
            return true;
        }

        if (!sender.hasPermission(PermissionNodes.FLY_COMMAND) || !sender.isOp()) {
            sender.sendMessage(getMessage("noPermissions"));
            return true;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage(getMessage("notPlayer"));
            return true;
        }

        UUID uuid = player.getUniqueId();
        Location loc = player.getLocation();

        // Получаем данные игрока и территории
        Optional<TanPlayer> optPlayer = playerManager.get(uuid);
        if (optPlayer.isEmpty()) {
            sender.sendMessage(getMessage("playerNotFound"));
            LOGGER.warning("Player with UUID " + uuid + " not found in TanPlayerManager.");
            return true;
        }

        Optional<TanTerritory> optTerritory = claimManager.getTerritoryOfBlock(loc.getBlock());
        if (optTerritory.isEmpty()) {
            sender.sendMessage(getMessage("notInTerritory"));
            return true;
        }

        TanTerritory territory = optTerritory.get();
        TanPlayer tanPlayer = optPlayer.get();

        boolean canFly = canPlayerFlyInTerritory(player, tanPlayer, territory);

        if (canFly) {
            toggleFlight(player);
            if (player.getAllowFlight()) {
                player.sendMessage(getMessage("flightEnabled"));
            } else {
                player.sendMessage(getMessage("flightDisabled"));
            }
        } else {
            player.sendMessage(getMessage("cannotFlyHere"));
        }

        return true;
    }

    private void toggleFlight(Player player) {
        boolean enableFly = !player.getAllowFlight();
        player.setAllowFlight(enableFly);
        player.setFlying(enableFly);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> suggestions = new ArrayList<>();

        // Если это игрок с правами на reload, показываем опцию "reload"
        if (args.length == 1 && (sender.hasPermission("tanfly.reload") || sender.isOp())) {
            suggestions.add("reload");
        }

        return suggestions;
    }
}
