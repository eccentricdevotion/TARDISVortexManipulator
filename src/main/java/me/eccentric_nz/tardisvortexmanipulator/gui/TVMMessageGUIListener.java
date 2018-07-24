/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator.gui;

import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
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

import java.util.HashMap;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TVMMessageGUIListener extends TVMGUICommon implements Listener {

    private final TARDISVortexManipulator plugin;
    int selectedSlot = -1;

    public TVMMessageGUIListener(TARDISVortexManipulator plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onMessageGUIClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        String name = inv.getTitle();
        if (name.equals("§4VM Messages")) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
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

    private void doPrev(Inventory inv, Player p) {
        int page = getPageNumber(inv);
        if (page > 1) {
            int start = (page * 44) - 44;
            close(p);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                TVMMessageGUI tvmm = new TVMMessageGUI(plugin, start, start + 44, p.getUniqueId().toString());
                ItemStack[] gui = tvmm.getGUI();
                Inventory vmg = plugin.getServer().createInventory(p, 54, "§4VM Messages");
                vmg.setContents(gui);
                p.openInventory(vmg);
            }, 2L);
        }
    }

    private void doNext(Inventory inv, Player p) {
        int page = getPageNumber(inv);
        int start = (page * 44) + 44;
        close(p);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            TVMMessageGUI tvmm = new TVMMessageGUI(plugin, start, start + 44, p.getUniqueId().toString());
            ItemStack[] gui = tvmm.getGUI();
            Inventory vmg = plugin.getServer().createInventory(p, 54, "§4VM Messages");
            vmg.setContents(gui);
            p.openInventory(vmg);
        }, 2L);
    }

    private void doRead(Inventory inv, Player p) {
        if (selectedSlot != -1) {
            ItemStack is = inv.getItem(selectedSlot);
            ItemMeta im = is.getItemMeta();
            List<String> lore = im.getLore();
            int message_id = TARDISNumberParsers.parseInt(lore.get(2));
            TVMResultSetMessageById rsm = new TVMResultSetMessageById(plugin, message_id);
            if (rsm.resultSet()) {
                close(p);
                TVMUtils.readMessage(p, rsm.getMessage());
                // update read status
                new TVMQueryFactory(plugin).setReadStatus(message_id);
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
            int message_id = TARDISNumberParsers.parseInt(lore.get(2));
            TVMResultSetMessageById rsm = new TVMResultSetMessageById(plugin, message_id);
            if (rsm.resultSet()) {
                close(p);
                HashMap<String, Object> where = new HashMap<>();
                where.put("message_id", message_id);
                new TVMQueryFactory(plugin).doDelete("messages", where);
                p.sendMessage(plugin.getPluginName() + "Message deleted.");
            }
        } else {
            p.sendMessage(plugin.getPluginName() + "Select a message!");
        }
    }
}
