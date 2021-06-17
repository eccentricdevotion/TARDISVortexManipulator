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
import me.eccentric_nz.tardisvortexmanipulator.database.TvmQueryFactory;
import me.eccentric_nz.tardisvortexmanipulator.database.TvmResultSetManipulator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.HashMap;
import java.util.Objects;

/**
 * @author eccentric_nz
 */
public class TvmCraftListener implements Listener {

    private final TardisVortexManipulatorPlugin plugin;

    public TvmCraftListener(TardisVortexManipulatorPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCraftManipulator(CraftItemEvent event) {
        Recipe recipe = event.getRecipe();
        ItemStack itemStack = recipe.getResult();
        if (itemStack.getType().equals(Material.CLOCK) && itemStack.hasItemMeta() && Objects.requireNonNull(itemStack.getItemMeta()).hasDisplayName() && itemStack.getItemMeta().getDisplayName().equals("Vortex Manipulator")) {
            Player player = (Player) event.getWhoClicked();
            String uuid = player.getUniqueId().toString();
            // check if they have a manipulator record
            TvmResultSetManipulator resultSetManipulator = new TvmResultSetManipulator(plugin, uuid);
            if (!resultSetManipulator.resultSet()) {
                // make a record
                HashMap<String, Object> set = new HashMap<>();
                set.put("uuid", uuid);
                new TvmQueryFactory(plugin).doInsert("manipulator", set);
            }
        }
    }
}
