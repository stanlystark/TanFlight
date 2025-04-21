package org.stark.tanFlight.listeners;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.tan.api.TanAPI;
import org.tan.api.getters.TanClaimManager;
import org.tan.api.getters.TanPlayerManager;
import org.tan.api.interfaces.TanPlayer;
import org.tan.api.interfaces.TanTerritory;

import static org.stark.tanFlight.TanFlight.LOGGER;
import static org.stark.tanFlight.utils.CanFlightChecker.*;
import static org.stark.tanFlight.utils.FlightMessage.getMessage;

import java.util.Optional;

public class FlightRegionListener implements Listener {

    private final TanPlayerManager playerManager = TanAPI.getInstance().getPlayerManager();
    private final TanClaimManager claimManager = TanAPI.getInstance().getClaimManager();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getTo() == null || event.getFrom().getChunk().equals(event.getTo().getChunk())) {
            return;
        }

        Player player = event.getPlayer();

        if (!player.getAllowFlight() || !player.getGameMode().equals(GameMode.SURVIVAL)) return;

        Location to = event.getTo();
        if (to == null) return;

        Optional<TanTerritory> optTerritory = claimManager.getTerritoryOfBlock(to.getBlock());
        Optional<TanPlayer> optTanPlayer = playerManager.get(player.getUniqueId());

        // Проверка на отсутствие игрока в системе
        if (optTanPlayer.isEmpty()) {
            LOGGER.warning("Player " + player.getName() + " not found in TanPlayerManager.");
            return;
        }

        TanPlayer tanPlayer = optTanPlayer.get();
        TanTerritory territory = optTerritory.orElse(null);

        boolean canFly = territory != null && canPlayerFlyInTerritory(player, tanPlayer, territory);

        if (!canFly) {
            player.setAllowFlight(false);
            player.setFlying(false);
            player.sendMessage(getMessage("leaveFromTerritory"));
        }
    }
}
