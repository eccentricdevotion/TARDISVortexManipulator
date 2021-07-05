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

import me.eccentric_nz.tardisvortexmanipulator.TardisVortexManipulatorPlugin;
import me.eccentric_nz.tardisvortexmanipulator.database.TvmResultSetSaves;
import me.eccentric_nz.tardisvortexmanipulator.storage.TvmSave;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TvmSavesGui {

    private final TardisVortexManipulatorPlugin plugin;
    private final int start, finish;
    private final String uuid;
    private final ItemStack[] gui;
    private final HashMap<String, Material> blocks = new HashMap<>();

    public TvmSavesGui(TardisVortexManipulatorPlugin plugin, int start, int finish, String uuid) {
        this.plugin = plugin;
        this.start = start;
        this.finish = finish;
        this.uuid = uuid;
        blocks.put("NORMAL", Material.DIRT);
        blocks.put("NETHER", Material.NETHERRACK);
        blocks.put("THE_END", Material.END_STONE);
        gui = getItemStack();
    }

    /**
     * Constructs an inventory for the Vortex Manipulator Saves GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {

        ItemStack[] stack = new ItemStack[54];
        int i = 0;
        // get the player's messages
        TvmResultSetSaves resultSetSaves = new TvmResultSetSaves(plugin, uuid, start, 44);
        if (resultSetSaves.resultSet()) {
            List<TvmSave> saves = resultSetSaves.getSaves();
            for (TvmSave save : saves) {
                // save
                ItemStack saveItem = new ItemStack(blocks.get(save.getEnv()), 1);
                ItemMeta saveMeta = saveItem.getItemMeta();
                saveMeta.setDisplayName(ChatColor.RESET + save.getName());
                saveMeta.setLore(Arrays.asList("World: " + save.getWorld(), "x: " + oneDecimal(save.getX()), "y: " + save.getY(), "z: " + oneDecimal(save.getZ())));
                saveItem.setItemMeta(saveMeta);
                stack[i] = saveItem;
                i++;
            }
        }

        int n = start / 44 + 1;
        // page number
        ItemStack page = new ItemStack(Material.BOWL, 1);
        ItemMeta num = page.getItemMeta();
        num.setDisplayName(ChatColor.RESET + "Page " + n);
        num.setCustomModelData(119);
        page.setItemMeta(num);
        stack[45] = page;
        // close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta win = close.getItemMeta();
        win.setDisplayName(ChatColor.RESET + "Close");
        win.setCustomModelData(1);
        close.setItemMeta(win);
        stack[46] = close;
        // previous screen (only if needed)
        if (start > 0) {
            ItemStack prev = new ItemStack(Material.BOWL, 1);
            ItemMeta een = prev.getItemMeta();
            een.setDisplayName(ChatColor.RESET + "Previous Page");
            een.setCustomModelData(120);
            prev.setItemMeta(een);
            stack[48] = prev;
        }
        // next screen (only if needed)
        if (finish > 44) {
            ItemStack next = new ItemStack(Material.BOWL, 1);
            ItemMeta scr = next.getItemMeta();
            scr.setDisplayName(ChatColor.RESET + "Next Page");
            scr.setCustomModelData(116);
            next.setItemMeta(scr);
            stack[49] = next;
        }
        // delete
        ItemStack delete = new ItemStack(Material.BOWL, 1);
        ItemMeta deleteMeta = delete.getItemMeta();
        deleteMeta.setDisplayName(ChatColor.RESET + "Delete");
        deleteMeta.setCustomModelData(107);
        delete.setItemMeta(deleteMeta);
        stack[51] = delete;
        // warp
        ItemStack warp = new ItemStack(Material.BOWL, 1);
        ItemMeta to = warp.getItemMeta();
        to.setDisplayName(ChatColor.RESET + "Enter Vortex");
        to.setCustomModelData(127);
        warp.setItemMeta(to);
        stack[53] = warp;

        return stack;
    }

    public ItemStack[] getGui() {
        return gui;
    }

    private String oneDecimal(double d) {
        return String.format("%f.1", d);
    }
}
