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
import me.eccentric_nz.tardisvortexmanipulator.TvmUtils;
import me.eccentric_nz.tardisvortexmanipulator.database.TvmQueryFactory;
import me.eccentric_nz.tardisvortexmanipulator.database.TvmResultSetWarpByName;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TvmSavesGuiListener extends TvmGuiCommon implements Listener {

    private final TardisVortexManipulatorPlugin plugin;
    int selectedSlot = -1;

    public TvmSavesGuiListener(TardisVortexManipulatorPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onGuiClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        String name = view.getTitle();
        if (name.equals("§4VM Saves")) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            int slot = event.getRawSlot();
            if (slot >= 0 && slot < 54) {
                if (view.getItem(slot) != null) {
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
                            doPrev(view, player);
                            break;
                        case 49:
                            // next page
                            doNext(view, player);
                            break;
                        case 51:
                            // delete save
                            delete(view, player);
                            break;
                        case 53:
                            // warp
                            doWarp(view, player);
                            break;
                        default:
                            selectedSlot = slot;
                            break;
                    }
                }
            }
        }
    }

    private void doPrev(InventoryView view, Player p) {
        int page = getPageNumber(view);
        if (page > 1) {
            int start = (page * 44) - 44;
            close(p);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                TvmSavesGui tvms = new TvmSavesGui(plugin, start, start + 44, p.getUniqueId().toString());
                ItemStack[] gui = tvms.getGui();
                Inventory vmg = plugin.getServer().createInventory(p, 54, "§4VM Saves");
                vmg.setContents(gui);
                p.openInventory(vmg);
            }, 2L);
        }
    }

    private void doNext(InventoryView view, Player p) {
        int page = getPageNumber(view);
        int start = (page * 44) + 44;
        close(p);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            TvmSavesGui tvms = new TvmSavesGui(plugin, start, start + 44, p.getUniqueId().toString());
            ItemStack[] gui = tvms.getGui();
            Inventory vmg = plugin.getServer().createInventory(p, 54, "§4VM Saves");
            vmg.setContents(gui);
            p.openInventory(vmg);
        }, 2L);
    }

    private void delete(InventoryView view, Player p) {
        if (selectedSlot != -1) {
            ItemStack is = view.getItem(selectedSlot);
            assert is != null;
            ItemMeta im = is.getItemMeta();
            assert im != null;
            String save_name = im.getDisplayName();
            TvmResultSetWarpByName rss = new TvmResultSetWarpByName(plugin, p.getUniqueId().toString(), save_name);
            if (rss.resultSet()) {
                close(p);
                HashMap<String, Object> where = new HashMap<>();
                where.put("save_id", rss.getId());
                new TvmQueryFactory(plugin).doDelete("saves", where);
                p.sendMessage(plugin.getPluginName() + "Save deleted.");
            }
        } else {
            p.sendMessage(plugin.getPluginName() + "Select a save!");
        }
    }

    private void doWarp(InventoryView view, Player p) {
        if (selectedSlot != -1) {
            ItemStack is = view.getItem(selectedSlot);
            assert is != null;
            ItemMeta im = is.getItemMeta();
            assert im != null;
            String save_name = im.getDisplayName();
            TvmResultSetWarpByName rss = new TvmResultSetWarpByName(plugin, p.getUniqueId().toString(), save_name);
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
                if (!TvmUtils.checkTachyonLevel(p.getUniqueId().toString(), required)) {
                    close(p);
                    p.sendMessage(plugin.getPluginName() + "You need at least " + required + " tachyons to travel!");
                    return;
                }
                Location l = rss.getWarp();
                p.sendMessage(plugin.getPluginName() + "Standby for Vortex travel...");
                while (!l.getChunk().isLoaded()) {
                    l.getChunk().load();
                }
                TvmUtils.movePlayers(players, l, p.getLocation().getWorld());
                // remove tachyons
                new TvmQueryFactory(plugin).alterTachyons(p.getUniqueId().toString(), -required);
            }
        } else {
            p.sendMessage(plugin.getPluginName() + "Select a save!");
        }
    }
}
