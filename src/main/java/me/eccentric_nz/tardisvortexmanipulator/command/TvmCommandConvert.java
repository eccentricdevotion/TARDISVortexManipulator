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

import me.eccentric_nz.tardisvortexmanipulator.TardisVortexManipulatorPlugin;
import me.eccentric_nz.tardisvortexmanipulator.database.Converter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class TvmCommandConvert implements CommandExecutor {

    private final TardisVortexManipulatorPlugin plugin;
    private final int full;

    public TvmCommandConvert(TardisVortexManipulatorPlugin plugin) {
        this.plugin = plugin;
        full = this.plugin.getConfig().getInt("tachyon_use.max");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
        if (command.getName().equalsIgnoreCase("vmd")) {
            if (!sender.hasPermission("tardis.admin")) {
                sender.sendMessage(plugin.getPluginName() + "You don't have permission to use that command!");
                return true;
            }
            if (args.length < 1 || !args[0].equalsIgnoreCase("convert_database")) {
                return false;
            }
            try {
                plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Converter(plugin, sender));
                return true;
            } catch (Exception e) {
                sender.sendMessage("Database conversion failed! " + e.getMessage());
                return true;
            }
        }
        return false;
    }
}
