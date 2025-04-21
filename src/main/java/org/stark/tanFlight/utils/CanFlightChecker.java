package org.stark.tanFlight.utils;

import org.bukkit.entity.Player;
import org.tan.api.interfaces.TanPlayer;
import org.tan.api.interfaces.TanTerritory;

import static org.stark.tanFlight.utils.FlightPermissionChecker.*;

public class CanFlightChecker {

    public static boolean canPlayerFlyInTerritory(Player player, TanPlayer tanPlayer, TanTerritory territory) {
        boolean canFly = false;

        if (player.hasPermission(PermissionNodes.FLY)) {
            canFly = isPlayerInOwnTownOrRegion(tanPlayer, territory);
        } else if (player.hasPermission(PermissionNodes.FLY_REGION)) {
            canFly = isPlayerInOwnRegion(tanPlayer, territory);
        } else if (player.hasPermission(PermissionNodes.FLY_TOWN)) {
            canFly = isPlayerInOwnTown(tanPlayer, territory);
        } else if (player.isOp()) {
            canFly = true;
        }

        return canFly;
    }
}
