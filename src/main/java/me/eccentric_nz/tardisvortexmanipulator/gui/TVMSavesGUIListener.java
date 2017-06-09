/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.tardisvortexmanipulator.TARDISVortexManipulator;
import me.eccentric_nz.tardisvortexmanipulator.TVMUtils;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMQueryFactory;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetWarpByName;
import org.bukkit.Location;
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
public class TVMSavesGUIListener extends TVMGUICommon implements Listener {

    private final TARDISVortexManipulator plugin;
    int selectedSlot = -1;

    public TVMSavesGUIListener(TARDISVortexManipulator plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onGUIClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        String name = inv.getTitle();
        if (name.equals("§4VM Saves")) {
            event.setCancelled(true);
            final Player player = (Player) event.getWhoClicked();
            int slot = event.getRawSlot();
            if (slot >= 0 && slot < 54) {
                if (inv.getItem(slot) != null) {
                    switch (slot) {
                        case 45:
                            // page number
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
                            // delete save
                            delete(inv, player);
                            break;
                        case 53:
                            // warp
                            doWarp(inv, player);
                            break;
                        default:
                            selectedSlot = slot;
                            break;
                    }
                }
            }
        }
    }

    private void doPrev(Inventory inv, final Player p) {
        int page = getPageNumber(inv);
        if (page > 1) {
            final int start = (page * 44) - 44;
            close(p);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                TVMSavesGUI tvms = new TVMSavesGUI(plugin, start, start + 44, p.getUniqueId().toString());
                ItemStack[] gui = tvms.getGUI();
                Inventory vmg = plugin.getServer().createInventory(p, 54, "§4VM Saves");
                vmg.setContents(gui);
                p.openInventory(vmg);
            }, 2L);
        }
    }

    private void doNext(Inventory inv, final Player p) {
        int page = getPageNumber(inv);
        final int start = (page * 44) + 44;
        close(p);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            TVMSavesGUI tvms = new TVMSavesGUI(plugin, start, start + 44, p.getUniqueId().toString());
            ItemStack[] gui = tvms.getGUI();
            Inventory vmg = plugin.getServer().createInventory(p, 54, "§4VM Saves");
            vmg.setContents(gui);
            p.openInventory(vmg);
        }, 2L);
    }

    private void delete(Inventory inv, Player p) {
        if (selectedSlot != -1) {
            ItemStack is = inv.getItem(selectedSlot);
            ItemMeta im = is.getItemMeta();
            String save_name = im.getDisplayName();
            TVMResultSetWarpByName rss = new TVMResultSetWarpByName(plugin, p.getUniqueId().toString(), save_name);
            if (rss.resultSet()) {
                close(p);
                HashMap<String, Object> where = new HashMap<>();
                where.put("save_id", rss.getId());
                new TVMQueryFactory(plugin).doDelete("saves", where);
                p.sendMessage(plugin.getPluginName() + "Save deleted.");
            }
        } else {
            p.sendMessage(plugin.getPluginName() + "Select a save!");
        }
    }

    private void doWarp(Inventory inv, Player p) {
        if (selectedSlot != -1) {
            ItemStack is = inv.getItem(selectedSlot);
            ItemMeta im = is.getItemMeta();
            String save_name = im.getDisplayName();
            TVMResultSetWarpByName rss = new TVMResultSetWarpByName(plugin, p.getUniqueId().toString(), save_name);
            if (rss.resultSet()) {
                close(p);
                List<Player> players = new ArrayList<>();
                players.add(p);
                if (plugin.getConfig().getBoolean("allow.multiple")) {
                    p.getNearbyEntities(0.5d, 0.5d, 0.5d).forEach((e) -> {
                        if (e instanceof Player && !e.getUniqueId().equals(p.getUniqueId())) {
                            players.add((Player) e);
                        }
                    });
                }
                int required = plugin.getConfig().getInt("tachyon_use.travel.saved") * players.size();
                if (!TVMUtils.checkTachyonLevel(p.getUniqueId().toString(), required)) {
                    close(p);
                    p.sendMessage(plugin.getPluginName() + "You need at least " + required + " tachyons to travel!");
                    return;
                }
                Location l = rss.getWarp();
                p.sendMessage(plugin.getPluginName() + "Standby for Vortex travel...");
                while (!l.getChunk().isLoaded()) {
                    l.getChunk().load();
                }
                TVMUtils.movePlayers(players, l, p.getLocation().getWorld());
                // remove tachyons
                new TVMQueryFactory(plugin).alterTachyons(p.getUniqueId().toString(), -required);
            }
        } else {
            p.sendMessage(plugin.getPluginName() + "Select a save!");
        }
    }
}
