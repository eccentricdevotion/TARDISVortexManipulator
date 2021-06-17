/*
 * Copyright (C) 2021 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardisvortexmanipulator.listeners;

import me.eccentric_nz.tardisvortexmanipulator.TardisVortexManipulatorPlugin;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TvmDeathListener implements Listener {

    private final TardisVortexManipulatorPlugin plugin;

    public TvmDeathListener(TardisVortexManipulatorPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerFallIntoVoid(EntityDamageEvent event) {
        if (!event.getCause().equals(DamageCause.VOID) && !event.getCause().equals(DamageCause.SUFFOCATION)) {
            return;
        }
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        plugin.debug("Damage: " + event.getCause());
        UUID uuid = player.getUniqueId();
        if (plugin.getTravellers().contains(uuid)) {
            Location location = player.getLocation();
            World world = location.getWorld();
            assert world != null;
            if (!world.getEnvironment().equals(Environment.NETHER)) {
                plugin.debug("Highest block");
                double y = world.getHighestBlockYAt(location);
                location.setY(y);
            } else {
                location = world.getSpawnLocation();
                plugin.debug("Nether spawn location");
            }
            player.teleport(location);
            plugin.getTravellers().remove(uuid);
        }
    }
}
