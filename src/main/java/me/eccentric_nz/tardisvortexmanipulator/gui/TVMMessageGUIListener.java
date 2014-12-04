/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator.gui;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.tardisvortexmanipulator.TARDISVortexManipulator;
import me.eccentric_nz.tardisvortexmanipulator.TVMUtils;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMQueryFactory;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetMessageById;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author eccentric_nz
 */
public class TVMMessageGUIListener implements Listener {

    private final TARDISVortexManipulator plugin;
    int selectedSlot = -1;

    public TVMMessageGUIListener(TARDISVortexManipulator plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onMessageGUIClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        String name = inv.getTitle();
        if (name.equals("§4VM Messages")) {
            event.setCancelled(true);
            final Player player = (Player) event.getWhoClicked();
            UUID uuid = player.getUniqueId();
            int slot = event.getRawSlot();
            if (slot >= 0 && slot < 54) {
                switch (slot) {
                    case 45:
                        break;
                    case 46:
                        // close
                        close(player);
                        break;
                    case 48:
                        // previous page
                        doPrev(inv, player);
                        break;
                    case 49:
                        // next page
                        doNext(inv, player);
                        break;
                    case 51:
                        // read
                        doRead(inv, player);
                        break;
                    case 53:
                        // delete
                        doDelete(inv, player);
                        break;
                    default:
                        // select a message
                        selectedSlot = slot;
                        break;
                }
            }
        }
    }

    private void doPrev(Inventory inv, final Player p) {
        int page = getPageNumber(inv);
        if (page > 1) {
            final int start = (page * 44) - 44;
            close(p);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    TVMMessageGUI tvmm = new TVMMessageGUI(plugin, start, start + 44, p.getUniqueId().toString(), false);
                    ItemStack[] gui = tvmm.getGUI();
                    Inventory vmg = plugin.getServer().createInventory(p, 54, "§4VM Messages");
                    vmg.setContents(gui);
                    p.openInventory(vmg);
                }
            }, 2L);
        }
    }

    private void doNext(Inventory inv, final Player p) {
        int page = getPageNumber(inv);
        final int start = (page * 44) + 44;
        close(p);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                TVMMessageGUI tvmm = new TVMMessageGUI(plugin, start, start + 44, p.getUniqueId().toString(), false);
                ItemStack[] gui = tvmm.getGUI();
                Inventory vmg = plugin.getServer().createInventory(p, 54, "§4VM Messages");
                vmg.setContents(gui);
                p.openInventory(vmg);
            }
        }, 2L);
    }

    private void doRead(Inventory inv, Player p) {
        if (selectedSlot != -1) {
            ItemStack is = inv.getItem(selectedSlot);
            ItemMeta im = is.getItemMeta();
            List<String> lore = im.getLore();
            int message_id = TARDIS.plugin.getUtils().parseInt(lore.get(2));
            TVMResultSetMessageById rsm = new TVMResultSetMessageById(plugin, message_id);
            if (rsm.resultSet()) {
                close(p);
                TVMUtils.readMessage(p, rsm.getMessage());
            }
        } else {
            p.sendMessage(plugin.getPluginName() + "Select a message!");
        }
    }

    private void doDelete(Inventory inv, Player p) {
        if (selectedSlot != -1) {
            ItemStack is = inv.getItem(selectedSlot);
            ItemMeta im = is.getItemMeta();
            List<String> lore = im.getLore();
            int message_id = TARDIS.plugin.getUtils().parseInt(lore.get(2));
            TVMResultSetMessageById rsm = new TVMResultSetMessageById(plugin, message_id);
            if (rsm.resultSet()) {
                close(p);
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("message_id", message_id);
                new TVMQueryFactory(plugin).doDelete("messages", where);
                p.sendMessage(plugin.getPluginName() + "Message deleted.");
            }
        } else {
            p.sendMessage(plugin.getPluginName() + "Select a message!");
        }
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

    private int getPageNumber(Inventory inv) {
        ItemStack is = inv.getItem(45);
        ItemMeta im = is.getItemMeta();
        String[] split = im.getDisplayName().split(" ");
        int page = TARDIS.plugin.getUtils().parseInt(split[1]);
        return page;
    }
}
