/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator.listeners;

import me.eccentric_nz.tardisvortexmanipulator.TARDISVortexManipulator;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetManipulator;
import me.eccentric_nz.tardisvortexmanipulator.gui.TVMGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author eccentric_nz
 */
public class TVMEquipListener implements Listener {

    private final TARDISVortexManipulator plugin;

    public TVMEquipListener(TARDISVortexManipulator plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (!action.equals(Action.RIGHT_CLICK_AIR)) {
            return;
        }
        final Player player = event.getPlayer();
        if (!player.hasPermission("vm.teleport")) {
            return;
        }
        ItemStack is = player.getItemInHand();
        if (is != null && is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().getDisplayName().equals("Vortex Manipulator")) {
            // get tachyon level
            TVMResultSetManipulator rs = new TVMResultSetManipulator(plugin, player.getUniqueId().toString());
            if (rs.resultSet()) {
                // open gui
                ItemStack[] gui = new TVMGUI(plugin, rs.getTachyonLevel()).getGUI();
                Inventory vmg = plugin.getServer().createInventory(player, 54, "§4Vortex Manipulator");
                vmg.setContents(gui);
                player.openInventory(vmg);
            }
        }
    }
}
