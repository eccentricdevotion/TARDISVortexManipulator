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
package me.eccentric_nz.tardisvortexmanipulator.command;

import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import me.eccentric_nz.tardisvortexmanipulator.TardisVortexManipulatorPlugin;
import me.eccentric_nz.tardisvortexmanipulator.database.TvmQueryFactory;
import me.eccentric_nz.tardisvortexmanipulator.database.TvmResultSetManipulator;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class TvmCommandGive implements CommandExecutor {

    private final TardisVortexManipulatorPlugin plugin;
    private final int full;

    public TvmCommandGive(TardisVortexManipulatorPlugin plugin) {
        this.plugin = plugin;
        full = this.plugin.getConfig().getInt("tachyon_use.max");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
        if (command.getName().equalsIgnoreCase("vmg")) {
            if (!sender.hasPermission("tardis.admin")) {
                sender.sendMessage(plugin.getPluginName() + "You don't have permission to use that command!");
                return true;
            }
            if (args.length < 2) {
                sender.sendMessage(plugin.getPluginName() + "You need to specify a player and amount!");
                return true;
            }
            // Look up this player's UUID
            OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(args[0]);
            if (offlinePlayer != null) {
                UUID uuid = offlinePlayer.getUniqueId();
                plugin.getServer().dispatchCommand(sender, "vmg " + uuid + " " + args[1]);
            } else if (offlinePlayer == null || !offlinePlayer.isOnline()) {
                sender.sendMessage(plugin.getPluginName() + "Could not find player! Are they online?");
                return true;
            }
            // check for existing record
            TvmResultSetManipulator resultSetManipulator = new TvmResultSetManipulator(plugin, args[0]);
            if (resultSetManipulator.resultSet()) {
                int tachyonLevel = resultSetManipulator.getTachyonLevel();
                int amount;
                if (args[1].equalsIgnoreCase("full")) {
                    amount = full;
                } else if (args[1].equalsIgnoreCase("empty")) {
                    amount = 0;
                } else {
                    try {
                        amount = Integer.parseInt(args[1]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(plugin.getPluginName() + "The last argument must be a number, 'full' or 'empty'");
                        return true;
                    }
                    if (tachyonLevel + amount > full) {
                        amount = full;
                    } else {
                        amount += tachyonLevel;
                    }
                }
                HashMap<String, Object> set = new HashMap<>();
                set.put("tachyon_level", amount);
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", args[0]);
                new TvmQueryFactory(plugin).doUpdate("manipulator", set, where);
                sender.sendMessage(plugin.getPluginName() + "Tachyon level set to " + amount);
            } else {
                sender.sendMessage(plugin.getPluginName() + "Player does not have a Vortex Manipulator!");
            }
            return true;
        }
        return false;
    }
}
