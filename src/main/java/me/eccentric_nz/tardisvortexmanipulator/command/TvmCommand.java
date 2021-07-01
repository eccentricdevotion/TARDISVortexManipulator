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

import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.tardisvortexmanipulator.TardisVortexManipulatorPlugin;
import me.eccentric_nz.tardisvortexmanipulator.TvmUtils;
import me.eccentric_nz.tardisvortexmanipulator.database.TvmQueryFactory;
import me.eccentric_nz.tardisvortexmanipulator.database.TvmResultSetManipulator;
import me.eccentric_nz.tardisvortexmanipulator.database.TvmResultSetWarpByName;
import me.eccentric_nz.tardisvortexmanipulator.gui.TvmGui;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TvmCommand implements CommandExecutor {

    private final TardisVortexManipulatorPlugin plugin;

    public TvmCommand(TardisVortexManipulatorPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
        if (command.getName().equalsIgnoreCase("vm")) {
            if (args.length > 0 && args[0].equalsIgnoreCase("help")) {
                plugin.getServer().dispatchCommand(sender, "vmh");
                return true;
            }
            Player player = null;
            if (sender instanceof Player) {
                player = (Player) sender;
            }
            if (player == null) {
                sender.sendMessage(plugin.getPluginName() + "Command can only be used by a player!");
                return true;
            }
            if (!player.hasPermission("vm.teleport")) {
                player.sendMessage(plugin.getPluginName() + "You don't have permission to use that command!");
                return true;
            }
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            if (itemStack.hasItemMeta() && Objects.requireNonNull(itemStack.getItemMeta()).hasDisplayName() && itemStack.getItemMeta().getDisplayName().equals("Vortex Manipulator")) {
                String uuid = player.getUniqueId().toString();
                if (args.length > 0 && args[0].equalsIgnoreCase("gui")) {
                    // get tachyon level
                    TvmResultSetManipulator resultSetManipulator = new TvmResultSetManipulator(plugin, uuid);
                    if (resultSetManipulator.resultSet()) {
                        // open gui
                        ItemStack[] gui = new TvmGui(plugin, resultSetManipulator.getTachyonLevel()).getGui();
                        Inventory vortexManipulatorGui = plugin.getServer().createInventory(player, 54, "§4Vortex Manipulator");
                        vortexManipulatorGui.setContents(gui);
                        player.openInventory(vortexManipulatorGui);
                        return true;
                    }
                }
                if (args.length > 0 && args[0].equalsIgnoreCase("go")) {
                    if (args.length < 2) {
                        player.sendMessage(plugin.getPluginName() + "You need to specify a save name!");
                        return true;
                    }
                    // check save exists
                    TvmResultSetWarpByName resultSetWarp = new TvmResultSetWarpByName(plugin, uuid, args[1]);
                    if (!resultSetWarp.resultSet()) {
                        player.sendMessage(plugin.getPluginName() + "Save does not exist!");
                        return true;
                    }
                    Location location = resultSetWarp.getWarp();
                    player.sendMessage(plugin.getPluginName() + "Standby for Vortex travel to " + args[1] + "...");
                    while (!location.getChunk().isLoaded()) {
                        location.getChunk().load();
                    }
                    List<Player> players = new ArrayList<>();
                    players.add(player);
                    if (plugin.getConfig().getBoolean("allow.multiple")) {
                        for (Entity entity : player.getNearbyEntities(0.5d, 0.5d, 0.5d)) {
                            if (entity instanceof Player && !entity.getUniqueId().equals(player.getUniqueId())) {
                                players.add((Player) entity);
                            }
                        }
                    }
                    int required = plugin.getConfig().getInt("tachyon_use.saved") * players.size();
                    if (!TvmUtils.checkTachyonLevel(uuid, required)) {
                        player.sendMessage(plugin.getPluginName() + "You need at least " + required + " tachyons to travel!");
                        return true;
                    }
                    TvmUtils.movePlayers(players, location, player.getLocation().getWorld());
                    // remove tachyons
                    new TvmQueryFactory(plugin).alterTachyons(uuid, -required);
                    return true;
                }

                Parameters params = new Parameters(player, TvmUtils.getProtectionFlags());
                int required;
                List<String> worlds = new ArrayList<>();
                Location location;
                switch (args.length) {
                    case 1, 2, 3 -> {
                        // check world is an actual world
                        if (plugin.getServer().getWorld(args[0]) == null) {
                            player.sendMessage(plugin.getPluginName() + "World does not exist!");
                            return true;
                        }
                        // check world is enabled for travel
                        if (!containsIgnoreCase(args[0], plugin.getTardisApi().getWorlds())) {
                            player.sendMessage(plugin.getPluginName() + "You cannot travel to this world using the Vortex Manipulator!");
                            return true;
                        }
                        required = plugin.getConfig().getInt("tachyon_use.travel.world");
                        // only world specified (or incomplete setting)
                        worlds.add(args[0]);
                        location = plugin.getTardisApi().getRandomLocation(worlds, null, params);
                    }
                    case 4 -> {
                        required = plugin.getConfig().getInt("tachyon_use.travel.coords");
                        // world, x, y, z specified
                        World world;
                        if (args[0].contains("~")) {
                            // relative location
                            world = player.getLocation().getWorld();
                        } else {
                            world = plugin.getServer().getWorld(args[0]);
                            if (world == null) {
                                player.sendMessage(plugin.getPluginName() + "World does not exist!");
                                return true;
                            }
                            // check world is enabled for travel
                            if (!containsIgnoreCase(args[0], plugin.getTardisApi().getWorlds())) {
                                player.sendMessage(plugin.getPluginName() + "You cannot travel to this world using the Vortex Manipulator!");
                                return true;
                            }
                        }
                        double x;
                        double y;
                        double z;
                        try {
                            if (args[1].startsWith("~")) {
                                // get players current location
                                Location playerLocation = player.getLocation();
                                double playerX = playerLocation.getX();
                                double playerY = playerLocation.getY();
                                double playerZ = playerLocation.getZ();
                                // strip off the initial "~" and add to current position
                                x = playerX + Double.parseDouble(args[1].substring(1));
                                y = playerY + Double.parseDouble(args[2].substring(1));
                                z = playerZ + Double.parseDouble(args[3].substring(1));
                            } else {
                                x = Double.parseDouble(args[1]);
                                y = Double.parseDouble(args[2]);
                                z = Double.parseDouble(args[3]);
                            }
                        } catch (NumberFormatException e) {
                            player.sendMessage(plugin.getPluginName() + "Could not parse coordinates!");
                            return true;
                        }
                        location = new Location(world, x, y, z);
                        // check block has space for player
                        if (!location.getBlock().getType().equals(Material.AIR)) {
                            player.sendMessage(plugin.getPluginName() + "Destination block is not AIR! Adjusting...");
                            // get highest block at these coords
                            int highest = Objects.requireNonNull(location.getWorld()).getHighestBlockYAt(location);
                            location.setY(highest);
                        }
                    }
                    default -> {
                        required = plugin.getConfig().getInt("tachyon_use.travel.random");
                        // random
                        location = plugin.getTardisApi().getRandomLocation(plugin.getTardisApi().getWorlds(), null, params);
                    }
                }
                List<Player> players = new ArrayList<>();
                players.add(player);
                if (plugin.getConfig().getBoolean("allow.multiple")) {
                    for (Entity entity : player.getNearbyEntities(0.5d, 0.5d, 0.5d)) {
                        if (entity instanceof Player && !entity.getUniqueId().equals(player.getUniqueId())) {
                            players.add((Player) entity);
                        }
                    }
                }
                int actual = required * players.size();
                if (!TvmUtils.checkTachyonLevel(uuid, actual)) {
                    player.sendMessage(plugin.getPluginName() + "You need at least " + actual + " tachyons to travel!");
                    return true;
                }
                if (location != null) {
                    player.sendMessage(plugin.getPluginName() + "Standby for Vortex travel...");
                    while (!location.getChunk().isLoaded()) {
                        location.getChunk().load();
                    }
                    TvmUtils.movePlayers(players, location, player.getLocation().getWorld());
                    // remove tachyons
                    new TvmQueryFactory(plugin).alterTachyons(uuid, -actual);
                } else {
                    //close(player);
                    player.sendMessage(plugin.getPluginName() + "No location could be found within those parameters.");
                }
                // do stuff
                return true;
            } else {
                player.sendMessage(plugin.getPluginName() + "You don't have a Vortex Manipulator in your hand!");
                return true;
            }
        }
        return false;
    }

    public boolean containsIgnoreCase(String key, List<String> list) {
        for (String string : list) {
            if (string.equalsIgnoreCase(key)) {
                return true;
            }
        }
        return false;
    }
}
