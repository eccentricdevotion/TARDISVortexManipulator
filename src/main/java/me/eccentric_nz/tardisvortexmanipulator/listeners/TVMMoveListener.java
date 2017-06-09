package me.eccentric_nz.tardisvortexmanipulator.listeners;

import java.util.HashMap;
import java.util.UUID;
import me.eccentric_nz.tardisvortexmanipulator.TARDISVortexManipulator;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMQueryFactory;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetBlock;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class TVMMoveListener implements Listener {

    private final TARDISVortexManipulator plugin;

    public TVMMoveListener(TARDISVortexManipulator plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    @SuppressWarnings("deprecation")
    public void onPlayerMove(PlayerMoveEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        if (!plugin.getBeaconSetters().contains(uuid)) {
            return;
        }
        if (!event.getTo().getBlock().getType().equals(Material.BEACON)) {
            plugin.getBeaconSetters().remove(uuid);
            // remove beacon
            TVMResultSetBlock rs = new TVMResultSetBlock(plugin, uuid.toString());
            if (rs.resultSet()) {
                rs.getBlocks().forEach((tvmb) -> {
                    tvmb.getBlock().setType(tvmb.getType());
                    tvmb.getBlock().setData(tvmb.getData());
                    // remove protection
                    plugin.getBlocks().remove(tvmb.getBlock().getLocation());
                });
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", uuid.toString());
                new TVMQueryFactory(plugin).doDelete("beacons", where);
            }
        }
    }
}
