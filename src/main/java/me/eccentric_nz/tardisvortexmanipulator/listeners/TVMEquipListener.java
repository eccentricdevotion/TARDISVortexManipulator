/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator.listeners;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import me.eccentric_nz.tardisvortexmanipulator.TARDISVortexManipulator;
import me.eccentric_nz.tardisvortexmanipulator.TVMUtils;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMQueryFactory;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetManipulator;
import me.eccentric_nz.tardisvortexmanipulator.gui.TVMGUI;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
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
    private final HashSet<Material> transparent = new HashSet<Material>();

    public TVMEquipListener(TARDISVortexManipulator plugin) {
        this.plugin = plugin;
        this.transparent.add(Material.AIR);
        this.transparent.add(Material.BROWN_MUSHROOM);
        this.transparent.add(Material.DEAD_BUSH);
        this.transparent.add(Material.DOUBLE_PLANT);
        this.transparent.add(Material.GRASS);
        this.transparent.add(Material.LONG_GRASS);
        this.transparent.add(Material.REDSTONE_WIRE);
        this.transparent.add(Material.RED_MUSHROOM);
        this.transparent.add(Material.RED_ROSE);
        this.transparent.add(Material.SNOW);
        this.transparent.add(Material.VINE);
        this.transparent.add(Material.YELLOW_FLOWER);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (!action.equals(Action.RIGHT_CLICK_AIR) && !action.equals(Action.LEFT_CLICK_AIR)) {
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
                if (action.equals(Action.RIGHT_CLICK_AIR)) {
                    // open gui
                    ItemStack[] gui = new TVMGUI(plugin, rs.getTachyonLevel()).getGUI();
                    Inventory vmg = plugin.getServer().createInventory(player, 54, "§4Vortex Manipulator");
                    vmg.setContents(gui);
                    player.openInventory(vmg);
                } else if (action.equals(Action.LEFT_CLICK_AIR)) {
                    UUID uuid = player.getUniqueId();
                    int maxDistance = plugin.getConfig().getInt("max_look_at_distance");
                    Location bl = player.getTargetBlock(transparent, maxDistance).getLocation();
                    bl.add(0.0d, 1.0d, 0.0d);
                    List<Player> players = new ArrayList<Player>();
                    players.add(player);
                    if (plugin.getConfig().getBoolean("allow.multiple")) {
                        for (Entity e : player.getNearbyEntities(0.5d, 0.5d, 0.5d)) {
                            if (e instanceof Player && !e.getUniqueId().equals(uuid)) {
                                players.add((Player) e);
                            }
                        }
                    }
                    int required = plugin.getConfig().getInt("tachyon_use.travel.to_block");
                    int actual = required * players.size();
                    if (!TVMUtils.checkTachyonLevel(uuid.toString(), actual)) {
                        player.sendMessage(plugin.getPluginName() + "You need at least " + actual + " tachyons to travel!");
                        return;
                    }
                    player.sendMessage(plugin.getPluginName() + "Standby for Vortex travel...");
                    TVMUtils.movePlayers(players, bl, player.getLocation().getWorld());
                    // remove tachyons
                    new TVMQueryFactory(plugin).alterTachyons(uuid.toString(), -actual);
                }
            }
        }
    }
}
