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
package me.eccentric_nz.tardisvortexmanipulator;

import me.eccentric_nz.TARDIS.enumeration.Flag;
import me.eccentric_nz.tardisvortexmanipulator.database.TvmResultSetInbox;
import me.eccentric_nz.tardisvortexmanipulator.database.TvmResultSetManipulator;
import me.eccentric_nz.tardisvortexmanipulator.database.TvmResultSetOutbox;
import me.eccentric_nz.tardisvortexmanipulator.database.TvmResultSetSaves;
import me.eccentric_nz.tardisvortexmanipulator.storage.TvmMessage;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TvmUtils {

    public static void movePlayers(List<Player> players, Location location, World from) {

        // try loading chunk
        World world = location.getWorld();
        Chunk chunk = world.getChunkAt(location);
        while (!world.isChunkLoaded(chunk)) {
            world.loadChunk(chunk);
        }
        // set location to centre of block
        location.setX(location.getBlockX() + 0.5);
        location.setY(location.getY() + 0.2);
        location.setZ(location.getBlockZ() + 0.5);
        World to = location.getWorld();
        boolean crossWorlds = from != to;

        players.stream().peek((player) -> {
            TardisVortexManipulatorPlugin.plugin.getTravellers().add(player.getUniqueId());
            boolean allowFlight = player.getAllowFlight();
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(TardisVortexManipulatorPlugin.plugin, () -> {
                player.teleport(location);
                player.getWorld().playSound(location, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
            }, 10L);
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(TardisVortexManipulatorPlugin.plugin, () -> {
                player.teleport(location);
                player.setNoDamageTicks(200);
                if (player.getGameMode() == GameMode.CREATIVE || (allowFlight && crossWorlds)) {
                    player.setAllowFlight(true);
                }
            }, 15L);
        }).forEachOrdered((thePlayer) -> Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(TardisVortexManipulatorPlugin.plugin, () -> TardisVortexManipulatorPlugin.plugin.getTravellers().remove(thePlayer.getUniqueId()), 100L));
    }

    /**
     * Get world protection flags
     *
     * @return List of flags with parameters
     */
    public static List<Flag> getProtectionFlags() {
        List<Flag> flags = new ArrayList<>();
        flags.add(Flag.PERMS_AREA);
        flags.add(Flag.PERMS_NETHER);
        flags.add(Flag.PERMS_THEEND);
        flags.add(Flag.PERMS_WORLD);
        if (TardisVortexManipulatorPlugin.plugin.getConfig().getBoolean("respect.factions")) {
            flags.add(Flag.RESPECT_FACTIONS);
        }
        if (TardisVortexManipulatorPlugin.plugin.getConfig().getBoolean("respect.griefprevention")) {
            flags.add(Flag.RESPECT_GRIEFPREVENTION);
        }
        if (TardisVortexManipulatorPlugin.plugin.getConfig().getBoolean("respect.towny")) {
            flags.add(Flag.RESPECT_TOWNY);
        }
        if (TardisVortexManipulatorPlugin.plugin.getConfig().getBoolean("respect.worldborder")) {
            flags.add(Flag.RESPECT_WORLDBORDER);
        }
        if (TardisVortexManipulatorPlugin.plugin.getConfig().getBoolean("respect.worldguard")) {
            flags.add(Flag.RESPECT_WORLDGUARD);
        }
        return flags;
    }

    /**
     * Check they have enough tachyons.
     *
     * @param uuid     the String UUID of the player to check
     * @param required the minimum amount of Tachyon required
     * @return true if the player has enough energy
     */
    public static boolean checkTachyonLevel(String uuid, int required) {
        TvmResultSetManipulator resultSetManipulator = new TvmResultSetManipulator(TardisVortexManipulatorPlugin.plugin, uuid);
        if (!resultSetManipulator.resultSet()) {
            return false;
        }
        return resultSetManipulator.getTachyonLevel() >= required;
    }

    /**
     * Send a list of saves to a player.
     *
     * @param player         the player to message
     * @param resultSetSaves the ResultSet containing the save information
     * @param page           the page number of this list
     */
    public static void sendSaveList(Player player, TvmResultSetSaves resultSetSaves, int page) {
        player.sendMessage(TardisVortexManipulatorPlugin.plugin.getPluginName() + ChatColor.AQUA + "Saves (page " + page + ":");
        resultSetSaves.getSaves().forEach((s) -> player.sendMessage(s.getName() + " - " + s.getWorld() + ":" + s.getX() + ":" + s.getY() + ":" + s.getZ()));
    }

    /**
     * Send a list of received messages to a player.
     *
     * @param player         the player to message
     * @param resultSetInbox the ResultSet containing the message information
     * @param page           the page number of this list
     */
    public static void sendInboxList(Player player, TvmResultSetInbox resultSetInbox, int page) {
        player.sendMessage(TardisVortexManipulatorPlugin.plugin.getPluginName() + ChatColor.AQUA + "Inbox (page " + page + "):");
        resultSetInbox.getMail().forEach((tvmMessage) -> {
            ChatColor colour = (tvmMessage.isRead()) ? ChatColor.DARK_GRAY : ChatColor.GRAY;
            player.sendMessage(colour + "" + tvmMessage.getId() + ": " + tvmMessage.getDate() + " - " + tvmMessage.getMessage().substring(0, 12));
        });
    }

    /**
     * Send a list of sent messages to a player.
     *
     * @param player          the player to message
     * @param resultSetOutbox the ResultSet containing the message information
     * @param page            the page number of this list
     */
    public static void sendOutboxList(Player player, TvmResultSetOutbox resultSetOutbox, int page) {
        player.sendMessage(TardisVortexManipulatorPlugin.plugin.getPluginName() + ChatColor.AQUA + "Outbox (page " + page + "):");
        resultSetOutbox.getMail().forEach((tvmMessage) -> player.sendMessage(tvmMessage.getId() + " - " + tvmMessage.getDate() + " - " + tvmMessage.getMessage().substring(0, 12)));
    }

    /**
     * Show a message to a player.
     *
     * @param player     the player to message
     * @param tvmMessage the message to read
     */
    public static void readMessage(Player player, TvmMessage tvmMessage) {
        player.sendMessage(TardisVortexManipulatorPlugin.plugin.getPluginName() + ChatColor.AQUA + TardisVortexManipulatorPlugin.plugin.getServer().getOfflinePlayer(tvmMessage.getWho()).getName() + " - " + tvmMessage.getDate());
        player.sendMessage(tvmMessage.getMessage());
    }

    /**
     * Convert ticks to human readable time.
     *
     * @param time the time in ticks to convert
     * @return the human readable time
     */
    public static String convertTicksToTime(int time) {
        // convert to seconds
        int seconds = time / 20;
        int hour = seconds / 3600;
        int remainder = seconds - (hour * 3600);
        int minute = remainder / 60;
        int second = remainder - (minute * 60);
        String gHour = (hour > 1 || hour == 0) ? " hours " : " hour "; // TODO Figure out what the heck the "g" stands for.
        String gMinute = (minute > 1 || minute == 0) ? " minutes " : " minute ";
        String gSecond = (second > 1 || second == 0) ? " seconds" : " second";
        return hour + gHour + minute + gMinute + second + gSecond;
    }
}
