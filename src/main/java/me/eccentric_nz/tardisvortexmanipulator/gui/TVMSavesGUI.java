/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator.gui;

import java.util.Arrays;
import java.util.List;
import me.eccentric_nz.tardisvortexmanipulator.TARDISVortexManipulator;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetSaves;
import me.eccentric_nz.tardisvortexmanipulator.storage.TVMSave;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author eccentric_nz
 */
public class TVMSavesGUI {

    private final TARDISVortexManipulator plugin;
    private final int start, finish;
    private final String uuid;
    private final ItemStack[] gui;

    public TVMSavesGUI(TARDISVortexManipulator plugin, int start, int finish, String uuid) {
        this.plugin = plugin;
        this.start = start;
        this.finish = finish;
        this.uuid = uuid;
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
        TVMResultSetSaves rs = new TVMResultSetSaves(plugin, uuid, start, 44);
        if (rs.resultSet()) {
            List<TVMSave> saves = rs.getSaves();
            for (TVMSave s : saves) {
                // save
                ItemStack save = new ItemStack(Material.WOOL, 1, (byte) 5);
                ItemMeta warp = save.getItemMeta();
                warp.setDisplayName(s.getName());
                warp.setLore(Arrays.asList("World: " + s.getWorld(), "x: " + s.getX(), "y: " + s.getY(), "z: " + s.getZ()));
                save.setItemMeta(warp);
                stack[i] = save;
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
