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

import me.eccentric_nz.tardis.utility.TardisNumberParsers;
import me.eccentric_nz.tardisvortexmanipulator.TardisVortexManipulatorPlugin;
import me.eccentric_nz.tardisvortexmanipulator.TvmUtils;
import me.eccentric_nz.tardisvortexmanipulator.database.TvmQueryFactory;
import me.eccentric_nz.tardisvortexmanipulator.database.TvmResultSetMessageById;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TvmMessageGuiListener extends TvmGuiCommon implements Listener {

    private final TardisVortexManipulatorPlugin plugin;
    int selectedSlot = -1;

    public TvmMessageGuiListener(TardisVortexManipulatorPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onMessageGUIClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        String name = view.getTitle();
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
                        doPrev(view, player);
                        break;
                    case 49:
                        // next page
                        doNext(view, player);
                        break;
                    case 51:
                        // read
                        doRead(view, player);
                        break;
                    case 53:
                        // delete
                        doDelete(view, player);
                        break;
                    default:
                        // select a message
                        selectedSlot = slot;
                        break;
                }
            }
        }
    }

    private void doPrev(InventoryView view, Player player) {
        int page = getPageNumber(view);
        if (page > 1) {
            int start = (page * 44) - 44;
            close(player);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                TvmMessageGui tvmMessageGui = new TvmMessageGui(plugin, start, start + 44, player.getUniqueId().toString());
                ItemStack[] gui = tvmMessageGui.getGui();
                Inventory vortexManipulatorGui = plugin.getServer().createInventory(player, 54, "§4VM Messages");
                vortexManipulatorGui.setContents(gui);
                player.openInventory(vortexManipulatorGui);
            }, 2L);
        }
    }

    private void doNext(InventoryView view, Player player) {
        int page = getPageNumber(view);
        int start = (page * 44) + 44;
        close(player);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            TvmMessageGui tvmMessageGui = new TvmMessageGui(plugin, start, start + 44, player.getUniqueId().toString());
            ItemStack[] gui = tvmMessageGui.getGui();
            Inventory vortexManipulatorGui = plugin.getServer().createInventory(player, 54, "§4VM Messages");
            vortexManipulatorGui.setContents(gui);
            player.openInventory(vortexManipulatorGui);
        }, 2L);
    }

    private void doRead(InventoryView view, Player player) {
        if (selectedSlot != -1) {
            ItemStack itemStack = view.getItem(selectedSlot);
            assert itemStack != null;
            ItemMeta itemMeta = itemStack.getItemMeta();
            assert itemMeta != null;
            List<String> lore = itemMeta.getLore();
            assert lore != null;
            int messageId = TardisNumberParsers.parseInt(lore.get(2));
            TvmResultSetMessageById resultSetMessage = new TvmResultSetMessageById(plugin, messageId);
            if (resultSetMessage.resultSet()) {
                close(player);
                TvmUtils.readMessage(player, resultSetMessage.getMessage());
                // update read status
                new TvmQueryFactory(plugin).setReadStatus(messageId);
            }
        } else {
            player.sendMessage(plugin.getPluginName() + "Select a message!");
        }
    }

    private void doDelete(InventoryView view, Player player) {
        if (selectedSlot != -1) {
            ItemStack itemStack = view.getItem(selectedSlot);
            assert itemStack != null;
            ItemMeta itemMeta = itemStack.getItemMeta();
            assert itemMeta != null;
            List<String> lore = itemMeta.getLore();
            assert lore != null;
            int messageId = TardisNumberParsers.parseInt(lore.get(2));
            TvmResultSetMessageById resultSetMessage = new TvmResultSetMessageById(plugin, messageId);
            if (resultSetMessage.resultSet()) {
                close(player);
                HashMap<String, Object> where = new HashMap<>();
                where.put("message_id", messageId);
                new TvmQueryFactory(plugin).doDelete("messages", where);
                player.sendMessage(plugin.getPluginName() + "Message deleted.");
            }
        } else {
            player.sendMessage(plugin.getPluginName() + "Select a message!");
        }
    }
}
