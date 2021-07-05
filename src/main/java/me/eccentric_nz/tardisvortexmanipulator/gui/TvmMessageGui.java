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
import me.eccentric_nz.tardisvortexmanipulator.database.TvmResultSetInbox;
import me.eccentric_nz.tardisvortexmanipulator.storage.TvmMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TvmMessageGui {

    private final TardisVortexManipulatorPlugin plugin;
    private final int start, finish;
    private final String uuid;
    private final ItemStack[] gui;

    public TvmMessageGui(TardisVortexManipulatorPlugin plugin, int start, int finish, String uuid) {
        this.plugin = plugin;
        this.start = start;
        this.finish = finish;
        this.uuid = uuid;
        gui = getItemStack();
    }

    /**
     * Constructs an inventory for the Vortex Manipulator Messages GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {

        ItemStack[] stack = new ItemStack[54];
        int i = 0;
        // get the player's messages
        TvmResultSetInbox resultSetInbox = new TvmResultSetInbox(plugin, uuid, start, 44);
        if (resultSetInbox.resultSet()) {
            List<TvmMessage> messages = resultSetInbox.getMail();
            for (TvmMessage message : messages) {
                // message
                ItemStack messageItem;
                if (message.isRead()) {
                    messageItem = new ItemStack(Material.BOOK, 1);
                } else {
                    messageItem = new ItemStack(Material.WRITABLE_BOOK, 1);
                }
                ItemMeta messageMeta = messageItem.getItemMeta();
                messageMeta.setDisplayName(ChatColor.RESET + "#" + (i + start + 1));
                String from = plugin.getServer().getOfflinePlayer(message.getWho()).getName();
                messageMeta.setLore(Arrays.asList(ChatColor.GRAY + "From: " + from, ChatColor.GRAY + "Date: " + message.getDate(), ChatColor.GRAY + "" + message.getId()));
                messageItem.setItemMeta(messageMeta);
                stack[i] = messageItem;
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
        // read
        ItemStack read = new ItemStack(Material.BOWL, 1);
        ItemMeta daer = read.getItemMeta();
        daer.setDisplayName(ChatColor.RESET + "Read");
        daer.setCustomModelData(121);
        read.setItemMeta(daer);
        stack[51] = read;
        // delete
        ItemStack delete = new ItemStack(Material.BOWL, 1);
        ItemMeta deleteMeta = delete.getItemMeta();
        deleteMeta.setDisplayName(ChatColor.RESET + "Delete");
        deleteMeta.setCustomModelData(107);
        delete.setItemMeta(deleteMeta);
        stack[53] = delete;

        return stack;
    }

    public ItemStack[] getGui() {
        return gui;
    }
}
