/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator;

import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetManipulator;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TVMUtils {

    public static void movePlayer(Player p, Location l, World from) {

        final Player thePlayer = p;
        TARDISVortexManipulator.plugin.getTravellers().add(p.getUniqueId());
        // set location to centre of block
        l.setX(l.getBlockX() + 0.5);
        l.setY(l.getY() + 0.2);
        l.setZ(l.getBlockZ() + 0.5);
        final Location theLocation = l;

        final World to = theLocation.getWorld();
        final boolean allowFlight = thePlayer.getAllowFlight();
        final boolean crossWorlds = from != to;

        // try loading chunk
        World world = l.getWorld();
        Chunk chunk = world.getChunkAt(l);
        while (!world.isChunkLoaded(chunk)) {
            world.loadChunk(chunk);
        }

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(TARDISVortexManipulator.plugin, new Runnable() {
            @Override
            public void run() {
                thePlayer.teleport(theLocation);
                thePlayer.getWorld().playSound(theLocation, Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
            }
        }, 10L);
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(TARDISVortexManipulator.plugin, new Runnable() {
            @Override
            public void run() {
                thePlayer.teleport(theLocation);
                if (TARDISVortexManipulator.plugin.getConfig().getBoolean("no_damage")) {
                    thePlayer.setNoDamageTicks(TARDISVortexManipulator.plugin.getConfig().getInt("no_damage_time") * 20);
                }
                if (thePlayer.getGameMode() == GameMode.CREATIVE || (allowFlight && crossWorlds)) {
                    thePlayer.setAllowFlight(true);
                }
            }
        }, 15L);
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(TARDISVortexManipulator.plugin, new Runnable() {
            @Override
            public void run() {
                if (TARDISVortexManipulator.plugin.getTravellers().contains(thePlayer.getUniqueId())) {
                    TARDISVortexManipulator.plugin.getTravellers().remove(thePlayer.getUniqueId());
                }
            }
        }, 100L);
    }

    /**
     * Check they have enough tachyons.
     *
     * @param uuid the String UUID of the player to check
     * @param required the minimum amount of Tachyon required
     */
    public static boolean checkTachyonLevel(String uuid, int required) {
        TVMResultSetManipulator rs = new TVMResultSetManipulator(TARDISVortexManipulator.plugin, uuid);
        if (!rs.resultSet()) {
            return false;
        }
        return rs.getTachyonLevel() >= required;
    }
}
