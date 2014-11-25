/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator.gui;

import java.util.Arrays;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author eccentric_nz
 */
public class TVMMessageGUI {

    private final int start, finish;
    private final ItemStack[] gui;

    public TVMMessageGUI(int start, int finish) {
        this.start = start;
        this.finish = finish;
        this.gui = getItemStack();
    }

    /**
     * Constructs an inventory for the Vortex Manipulator GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        // message
        ItemStack mess = new ItemStack(Material.WOOL, 1, (byte) 5);
        ItemMeta age = mess.getItemMeta();
        age.setDisplayName("#");
        age.setLore(Arrays.asList("From: ", "Date: ", "ID: "));
        mess.setItemMeta(age);

        // read
        ItemStack read = new ItemStack(Material.BOWL, 1);
        ItemMeta daer = read.getItemMeta();
        daer.setDisplayName("Read");
        read.setItemMeta(daer);
        // delete
        ItemStack del = new ItemStack(Material.BOWL, 1);
        ItemMeta ete = del.getItemMeta();
        ete.setDisplayName("Delete");
        del.setItemMeta(ete);
        // close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta win = close.getItemMeta();
        win.setDisplayName("Close");
        close.setItemMeta(win);
        // next screen
        ItemStack next = new ItemStack(Material.BOWL, 1);
        ItemMeta scr = next.getItemMeta();
        scr.setDisplayName("Next Page");
        next.setItemMeta(scr);
        // previous screen
        ItemStack prev = new ItemStack(Material.BOWL, 1);
        ItemMeta een = prev.getItemMeta();
        een.setDisplayName("Previous Page");
        prev.setItemMeta(een);

        ItemStack[] is = {
            null, null, null, null, mess, null, null, null, null,
            null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null,
            close, null, prev, next, null, read, null, null, del
        };
        return is;
    }

    public ItemStack[] getGUI() {
        return gui;
    }
}
