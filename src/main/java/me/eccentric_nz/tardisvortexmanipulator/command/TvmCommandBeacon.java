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
import me.eccentric_nz.TARDIS.enumeration.Flag;
import me.eccentric_nz.tardisvortexmanipulator.TardisVortexManipulatorPlugin;
import me.eccentric_nz.tardisvortexmanipulator.TvmUtils;
import me.eccentric_nz.tardisvortexmanipulator.database.TvmQueryFactory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TvmCommandBeacon implements CommandExecutor {

    private final TardisVortexManipulatorPlugin plugin;

    public TvmCommandBeacon(TardisVortexManipulatorPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
        if (command.getName().equalsIgnoreCase("vmb")) {
            Player player = null;
            if (sender instanceof Player) {
                player = (Player) sender;
            }
            if (player == null) {
                sender.sendMessage(plugin.getPluginName() + "That command cannot be used from the console!");
                return true;
            }
            if (!player.hasPermission("vm.beacon")) {
                player.sendMessage(plugin.getPluginName() + "You don't have permission to use that command!");
                return true;
            }
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName() && itemStack.getItemMeta().getDisplayName().equals("Vortex Manipulator")) {
                int required = plugin.getConfig().getInt("tachyon_use.lifesigns");
                if (!TvmUtils.checkTachyonLevel(player.getUniqueId().toString(), required)) {
                    player.sendMessage(plugin.getPluginName() + "You don't have enough tachyons to set a beacon signal!");
                    return true;
                }
                UUID uuid = player.getUniqueId();
                String uuidString = uuid.toString();
                Location location = player.getLocation();
                // potential griefing, we need to check the location first!
                List<Flag> flags = new ArrayList<>();
                if (plugin.getConfig().getBoolean("respect.factions")) {
                    flags.add(Flag.RESPECT_FACTIONS);
                }
                if (plugin.getConfig().getBoolean("respect.griefprevention")) {
                    flags.add(Flag.RESPECT_GRIEFPREVENTION);
                }
                if (plugin.getConfig().getBoolean("respect.towny")) {
                    flags.add(Flag.RESPECT_TOWNY);
                }
                if (plugin.getConfig().getBoolean("respect.worldborder")) {
                    flags.add(Flag.RESPECT_WORLDBORDER);
                }
                if (plugin.getConfig().getBoolean("respect.worldguard")) {
                    flags.add(Flag.RESPECT_WORLDGUARD);
                }
                Parameters params = new Parameters(player, flags);
                if (!plugin.getTardisApi().getRespect().getRespect(location, params)) {
                    player.sendMessage(plugin.getPluginName() + "You are not permitted to set a beacon signal here!");
                    return true;
                }
                Block block = location.getBlock().getRelative(BlockFace.DOWN);
                TvmQueryFactory queryFactory = new TvmQueryFactory(plugin);
                queryFactory.saveBeaconBlock(uuidString, block);
                block.setBlockData(Material.BEACON.createBlockData());
                Block down = block.getRelative(BlockFace.DOWN);
                queryFactory.saveBeaconBlock(uuidString, down);
                BlockData iron = Material.IRON_BLOCK.createBlockData();
                down.setBlockData(iron);
                List<BlockFace> blockFaces = Arrays.asList(BlockFace.EAST, BlockFace.NORTH_EAST, BlockFace.NORTH, BlockFace.NORTH_WEST, BlockFace.WEST, BlockFace.SOUTH_WEST, BlockFace.SOUTH, BlockFace.SOUTH_EAST);
                blockFaces.forEach((blockFace) -> {
                    queryFactory.saveBeaconBlock(uuidString, down.getRelative(blockFace));
                    down.getRelative(blockFace).setBlockData(iron);
                });
                plugin.getBeaconSetters().add(uuid);
                // remove tachyons
                queryFactory.alterTachyons(uuidString, -required);
                player.sendMessage(plugin.getPluginName() + "Beacon signal set, don't move!");
                return true;
            } else {
                player.sendMessage(plugin.getPluginName() + "You don't have a Vortex Manipulator in your hand!");
                return true;
            }
        }
        return false;
    }
}
