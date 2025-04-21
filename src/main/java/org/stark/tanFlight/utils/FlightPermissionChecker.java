package org.stark.tanFlight.utils;

import org.tan.api.interfaces.TanPlayer;
import org.tan.api.interfaces.TanTerritory;

public class FlightPermissionChecker {

    public static  boolean isPlayerInOwnTown(TanPlayer player, TanTerritory territory) {
        return player.getTown()
                .map(town -> town.getID().equals(territory.getID()))
                .orElse(false);
    }

    public static boolean isPlayerInOwnRegion(TanPlayer player, TanTerritory territory) {
        if (!player.hasRegion()) return false;
        return player.getRegion().getID().equals(territory.getID());
    }

    public static boolean isPlayerInOwnTownOrRegion(TanPlayer player, TanTerritory territory) {
        return isPlayerInOwnTown(player, territory) || isPlayerInOwnRegion(player, territory);
    }
}
