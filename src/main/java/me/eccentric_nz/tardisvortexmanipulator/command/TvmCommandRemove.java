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
import me.eccentric_nz.tardisvortexmanipulator.database.TvmQueryFactory;
import me.eccentric_nz.tardisvortexmanipulator.database.TvmResultSetWarpByName;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

public class TvmCommandRemove implements CommandExecutor {

    private final TardisVortexManipulatorPlugin plugin;

    public TvmCommandRemove(TardisVortexManipulatorPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
        if (command.getName().equalsIgnoreCase("vmr")) {
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
            if (itemStack.hasItemMeta() && Objects.requireNonNull(itemStack.getItemMeta()).hasDisplayName() && itemStack.getItemMeta().getDisplayName().equals("Vortex Manipulator")) {
                if (args.length < 1) {
                    player.sendMessage(plugin.getPluginName() + "You need to specify a save name!");
                    return true;
                }
                String uuid = player.getUniqueId().toString();
                // check for existing save
                TvmResultSetWarpByName resultSetWarp = new TvmResultSetWarpByName(plugin, uuid, args[0]);
                if (resultSetWarp.resultSet()) {
                    player.sendMessage(plugin.getPluginName() + "No save with that name exists! Try using /vms to list saves.");
                    return true;
                }
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", uuid);
                where.put("save_name", args[0]);
                new TvmQueryFactory(plugin).doDelete("saves", where);
                sender.sendMessage(plugin.getPluginName() + "Vortex Manipulator location (" + args[0] + ") removed!");
                return true;
            } else {
                player.sendMessage(plugin.getPluginName() + "You don't have a Vortex Manipulator in your hand!");
                return true;
            }
        }
        return false;
    }
}
