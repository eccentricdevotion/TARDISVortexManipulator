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
import me.eccentric_nz.tardisvortexmanipulator.TvmUtils;
import me.eccentric_nz.tardisvortexmanipulator.database.TvmQueryFactory;
import me.eccentric_nz.tardisvortexmanipulator.database.TvmResultSetSaves;
import me.eccentric_nz.tardisvortexmanipulator.database.TvmResultSetWarpByName;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class TvmCommandSave implements CommandExecutor {

    private final TardisVortexManipulatorPlugin plugin;

    public TvmCommandSave(TardisVortexManipulatorPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
        if (command.getName().equalsIgnoreCase("vms")) {
            Player player = null;
            if (sender instanceof Player) {
                player = (Player) sender;
            }
            if (player == null) {
                sender.sendMessage(plugin.getPluginName() + "That command cannot be used from the console!");
                return true;
            }
            if (!player.hasPermission("vm.teleport")) {
                player.sendMessage(plugin.getPluginName() + "You don't have permission to use that command!");
                return true;
            }
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            if (itemStack.hasItemMeta() && itemStack.getItemMeta().getPersistentDataContainer().has(TardisVortexManipulatorPlugin.plugin.getItemKey(), PersistentDataType.STRING) && itemStack.getItemMeta().getPersistentDataContainer().get(TardisVortexManipulatorPlugin.plugin.getItemKey(), PersistentDataType.STRING).equals("vortex_manipulator")) {
                String uuid = player.getUniqueId().toString();
                if (args.length == 0) {
                    // list saves
                    TvmResultSetSaves resultSetSaves = new TvmResultSetSaves(plugin, uuid, 0, 10);
                    if (resultSetSaves.resultSet()) {
                        TvmUtils.sendSaveList(player, resultSetSaves, 1);
                    }
                }
                if (args.length < 1) {
                    player.sendMessage(plugin.getPluginName() + "You need to specify a save name or page number!");
                    return true;
                }
                try {
                    int page = Integer.parseInt(args[0]);
                    if (page <= 0) {
                        player.sendMessage(plugin.getPluginName() + "Invalid page number!");
                        return true;
                    }
                    int start = (page * 10) - 10;
                    int limit = page * 10;
                    TvmResultSetSaves rss = new TvmResultSetSaves(plugin, uuid, start, limit);
                    if (rss.resultSet()) {
                        TvmUtils.sendSaveList(player, rss, page);
                    }
                } catch (NumberFormatException e) {
                    plugin.debug("Wasn't a page number...");
                    // check for existing save
                    TvmResultSetWarpByName resultSetWarp = new TvmResultSetWarpByName(plugin, uuid, args[0]);
                    if (resultSetWarp.resultSet()) {
                        player.sendMessage(plugin.getPluginName() + "You already have a save with that name!");
                        return true;
                    }
                    Location location = player.getLocation();
                    HashMap<String, Object> set = new HashMap<>();
                    set.put("uuid", uuid);
                    set.put("save_name", args[0]);
                    set.put("world", location.getWorld().getName());
                    set.put("x", location.getX());
                    set.put("y", location.getY());
                    set.put("z", location.getZ());
                    set.put("yaw", location.getYaw());
                    set.put("pitch", location.getPitch());
                    new TvmQueryFactory(plugin).doInsert("saves", set);
                    sender.sendMessage(plugin.getPluginName() + "Vortex Manipulator location (" + args[0] + ") saved!");
                    return true;
                }
            } else {
                player.sendMessage(plugin.getPluginName() + "You don't have a Vortex Manipulator in your hand!");
                return true;
            }
        }
        return false;
    }
}
