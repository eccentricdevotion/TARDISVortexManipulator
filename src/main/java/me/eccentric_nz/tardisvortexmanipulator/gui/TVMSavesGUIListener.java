/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator.gui;

import java.util.UUID;
import me.eccentric_nz.tardisvortexmanipulator.TARDISVortexManipulator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

/**
 *
 * @author eccentric_nz
 */
public class TVMSavesGUIListener implements Listener {

    private final TARDISVortexManipulator plugin;
    String selected = "";

    public TVMSavesGUIListener(TARDISVortexManipulator plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onGUIClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        String name = inv.getTitle();
        if (name.equals("§4VM Saves")) {
            event.setCancelled(true);
            final Player player = (Player) event.getWhoClicked();
            UUID uuid = player.getUniqueId();
            int slot = event.getRawSlot();
            if (slot >= 0 && slot < 54) {
                if (inv.getItem(slot) != null) {
                    switch (slot) {
                        case 45:
                            // close
                            close(player);
                            break;
                        case 47:
                            // previous page

                            break;
                        case 48:
                            // next page

                            break;
                        case 53:
                            // delete save
                            delete(player, selected);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    private void delete(Player p, String name) {

    }

    /**
     * Closes the inventory.
     *
     * @param p the player using the GUI
     */
    public void close(final Player p) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                p.closeInventory();
            }
        }, 1L);
    }
}
