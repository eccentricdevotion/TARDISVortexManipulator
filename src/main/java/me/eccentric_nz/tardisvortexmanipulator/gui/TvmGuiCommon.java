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
package me.eccentric_nz.tardisvortexmanipulator.gui;

import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import me.eccentric_nz.tardisvortexmanipulator.TardisVortexManipulatorPlugin;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author eccentric_nz
 */
public class TvmGuiCommon {

    private final TardisVortexManipulatorPlugin plugin;

    public TvmGuiCommon(TardisVortexManipulatorPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Closes the inventory.
     *
     * @param player the player using the GUI
     */
    public void close(Player player) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, player::closeInventory, 1L);
    }

    public int getPageNumber(InventoryView view) {
        ItemStack itemStack = view.getItem(45);
        ItemMeta itemMeta = itemStack.getItemMeta();
        String[] split = itemMeta.getDisplayName().split(" ");
        return TARDISNumberParsers.parseInt(split[1]);
    }
}
