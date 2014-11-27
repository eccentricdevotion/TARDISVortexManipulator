/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator.gui;

import java.util.Arrays;
import java.util.List;
import me.eccentric_nz.tardisvortexmanipulator.TARDISVortexManipulator;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetInbox;
import me.eccentric_nz.tardisvortexmanipulator.storage.TVMMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author eccentric_nz
 */
public class TVMMessageGUI {

    private final TARDISVortexManipulator plugin;
    private final int start, finish;
    private final String uuid;
    private final ItemStack[] gui;
    private final boolean box;

    public TVMMessageGUI(TARDISVortexManipulator plugin, int start, int finish, String uuid, boolean box) {
        this.plugin = plugin;
        this.start = start;
        this.finish = finish;
        this.uuid = uuid;
        this.box = box;
        this.gui = getItemStack();
    }

    /**
     * Constructs an inventory for the Vortex Manipulator GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {

        ItemStack[] stack = new ItemStack[54];
        int i = 0;
        // get the player's messages
        TVMResultSetInbox rs = new TVMResultSetInbox(plugin, uuid, box, start, 44);
        if (rs.resultSet()) {
            List<TVMMessage> messages = rs.getMail();
            for (TVMMessage m : messages) {
                // message
                ItemStack mess = new ItemStack(Material.WOOL, 1, (byte) 5);
                ItemMeta age = mess.getItemMeta();
                age.setDisplayName("#");
                age.setLore(Arrays.asList("From: ", "Date: ", "ID: "));
                mess.setItemMeta(age);
                stack[i] = mess;
                i++;
            }
        }

        // close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta win = close.getItemMeta();
        win.setDisplayName("Close");
        close.setItemMeta(win);
        stack[45] = close;
        // previous screen (only if needed)
        if (start > 0) {
            ItemStack prev = new ItemStack(Material.BOWL, 1);
            ItemMeta een = prev.getItemMeta();
            een.setDisplayName("Previous Page");
            prev.setItemMeta(een);
            stack[47] = prev;
        }
        // next screen (only if needed)
        if (finish > 44) {
            ItemStack next = new ItemStack(Material.BOWL, 1);
            ItemMeta scr = next.getItemMeta();
            scr.setDisplayName("Next Page");
            next.setItemMeta(scr);
            stack[48] = next;
        }
        // read
        ItemStack read = new ItemStack(Material.BOWL, 1);
        ItemMeta daer = read.getItemMeta();
        daer.setDisplayName("Read");
        read.setItemMeta(daer);
        stack[50] = read;
        // delete
        ItemStack del = new ItemStack(Material.BOWL, 1);
        ItemMeta ete = del.getItemMeta();
        ete.setDisplayName("Delete");
        del.setItemMeta(ete);
        stack[53] = del;

        return stack;
    }

    public ItemStack[] getGUI() {
        return gui;
    }
}