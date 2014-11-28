/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.enumeration.FLAG;
import me.eccentric_nz.tardisvortexmanipulator.TARDISVortexManipulator;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMQueryFactory;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetManipulator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author eccentric_nz
 */
public class TVMGUIListener implements Listener {

    private final TARDISVortexManipulator plugin;
    List<String> titles = Arrays.asList("§4Vortex Manipulator", "§4VM Messages", "§4VM Saves");
    List<String> components = Arrays.asList("", "", "", "", "");
    char[] two = new char[]{'2', 'a', 'b', 'c'};
    char[] three = new char[]{'3', 'd', 'e', 'f'};
    char[] four = new char[]{'4', 'g', 'h', 'i'};
    char[] five = new char[]{'5', 'j', 'k', 'l'};
    char[] six = new char[]{'6', 'm', 'n', 'o'};
    char[] seven = new char[]{'7', 'p', 'q', 'r', 's'};
    char[] eight = new char[]{'8', 't', 'u', 'v'};
    char[] nine = new char[]{'9', 'w', 'x', 'y', 'z'};
    char[] star = new char[]{'*', ' '};
    char[] hash = new char[]{'#', '~', '_', '-'};
    int which = 0;
    int[] pos;
    int t2 = 0;
    int t3 = 0;
    int t4 = 0;
    int t5 = 0;
    int t6 = 0;
    int t7 = 0;
    int t8 = 0;
    int t9 = 0;
    int ts = 0;
    int th = 0;
    TVMQueryFactory qf;

    public TVMGUIListener(TARDISVortexManipulator plugin) {
        this.plugin = plugin;
        // init string positions
        this.pos = new int[5];
        for (int i = 0; i < 5; i++) {
            this.pos[i] = 0;
        }
        qf = new TVMQueryFactory(this.plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onGUIClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        String name = inv.getTitle();
        if (name.equals("§4Vortex Manipulator")) {
            event.setCancelled(true);
            final Player player = (Player) event.getWhoClicked();
            int slot = event.getRawSlot();
            if (slot >= 0 && slot < 54) {
                switch (slot) {
//                    case 4:
//                        // display
//                        break;
//                    case 9:
//                        // tachyon level
//                        break;
                    case 11:
                        // world
                        which = 0;
                        resetTrackers();
                        break;
                    case 12:
                        // one
                        updateDisplay(inv, '1');
                        break;
                    case 13:
                        // two
                        if (which == 0 || which == 4) {
                            updateDisplay(inv, two[t2]);
                            t2++;
                            if (t2 == two.length) {
                                t2 = 0;
                            }
                        } else {
                            updateDisplay(inv, '2');
                        }
                        break;
                    case 14:
                        // three
                        if (which == 0 || which == 4) {
                            updateDisplay(inv, three[t3]);
                            t3++;
                            if (t3 == three.length) {
                                t3 = 0;
                            }
                        } else {
                            updateDisplay(inv, '3');
                        }
                        break;
                    case 16:
                        // save
                        which = 4;
                        resetTrackers();
                        break;
                    case 20:
                        // x
                        which = 1;
                        resetTrackers();
                        break;
                    case 21:
                        // four
                        if (which == 0 || which == 4) {
                            updateDisplay(inv, four[t4]);
                            t4++;
                            if (t4 == four.length) {
                                t4 = 0;
                            }
                        } else {
                            updateDisplay(inv, '4');
                        }
                        break;
                    case 22:
                        // five
                        if (which == 0 || which == 4) {
                            updateDisplay(inv, five[t5]);
                            t5++;
                            if (t5 == five.length) {
                                t5 = 0;
                            }
                        } else {
                            updateDisplay(inv, '5');
                        }
                        break;
                    case 23:
                        // six
                        if (which == 0 || which == 4) {
                            updateDisplay(inv, six[t6]);
                            t6++;
                            if (t6 == six.length) {
                                t6 = 0;
                            }
                        } else {
                            updateDisplay(inv, '6');
                        }
                        break;
                    case 25:
                        // load
                        // open saves GUI
                        loadSaves(player);
                        break;
                    case 29:
                        // y
                        which = 2;
                        resetTrackers();
                        break;
                    case 30:
                        // seven
                        if (which == 0 || which == 4) {
                            updateDisplay(inv, seven[t7]);
                            t7++;
                            if (t7 == seven.length) {
                                t7 = 0;
                            }
                        } else {
                            updateDisplay(inv, '7');
                        }
                        break;
                    case 31:
                        // eight
                        if (which == 0 || which == 4) {
                            updateDisplay(inv, eight[t8]);
                            t8++;
                            if (t8 == eight.length) {
                                t8 = 0;
                            }
                        } else {
                            updateDisplay(inv, '8');
                        }
                        break;
                    case 32:
                        // nine
                        if (which == 0 || which == 4) {
                            updateDisplay(inv, nine[t9]);
                            t9++;
                            if (t9 == nine.length) {
                                t9 = 0;
                            }
                        } else {
                            updateDisplay(inv, '9');
                        }
                        break;
                    case 34:
                        // message
                        message(player);
                        break;
                    case 38:
                        // z
                        which = 3;
                        resetTrackers();
                        break;
                    case 39:
                        // star
                        updateDisplay(inv, star[ts]);
                        ts++;
                        if (ts == star.length) {
                            ts = 0;
                        }
                        break;
                    case 40:
                        //zero
                        updateDisplay(inv, '0');
                        break;
                    case 41:
                        // hash
                        updateDisplay(inv, hash[th]);
                        th++;
                        if (th == hash.length) {
                            th = 0;
                        }
                        break;
                    case 43:
                        // beacon
                        setBeacon(player);
                        break;
                    case 45:
                        // close
                        close(player);
                        break;
                    case 48:
                        // previous cursor
                        if (pos[which] > 0) {
                            pos[which]--;
                        }
                        resetTrackers();
                        break;
                    case 50:
                        // next cursor
                        int next = components.get(which).length() + 1;
                        if (pos[which] < next) {
                            pos[which]++;
                        }
                        resetTrackers();
                        break;
                    case 53:
                        if (which == 4) {
                            // save
                            saveCurrentLocation(player, inv);
                        } else {
                            // warp
                            doWarp(player, inv);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void updateDisplay(Inventory inv, char s) {
        ItemStack display = inv.getItem(4);
        ItemMeta dim = display.getItemMeta();
        char[] chars = (components.get(which).isEmpty()) ? new char[1] : components.get(which).toCharArray();
        if (pos[which] >= chars.length) {
            char[] tmp = chars.clone();
            chars = new char[pos[which] + 1];
            int i = 0;
            for (char c : tmp) {
                chars[i] = tmp[i];
                i++;
            }
        }
        chars[pos[which]] = s;
        String comp = new String(chars);
        String combined;
        switch (which) {
            case 0:
                combined = comp + " " + components.get(1) + " " + components.get(2) + " " + components.get(3);
                break;
            case 1:
                combined = components.get(0) + " " + comp + " " + components.get(2) + " " + components.get(3);
                break;
            case 2:
                combined = components.get(0) + " " + components.get(1) + " " + comp + " " + components.get(3);
                break;
            case 3:
                combined = components.get(0) + " " + components.get(1) + " " + components.get(2) + " " + comp;
                break;
            default:
                combined = comp;
                break;
        }
        components.set(which, comp);
        List<String> dlore = Arrays.asList(combined);
        dim.setLore(dlore);
        display.setItemMeta(dim);
    }

    private void resetTrackers() {
        t2 = 0;
        t3 = 0;
        t4 = 0;
        t5 = 0;
        t6 = 0;
        t7 = 0;
        t8 = 0;
        t9 = 0;
        ts = 0;
        th = 0;
    }

    private void saveCurrentLocation(Player p, Inventory inv) {
        ItemStack display = inv.getItem(4);
        ItemMeta dim = display.getItemMeta();
        List<String> lore = dim.getLore();
        Location l = p.getLocation();
        HashMap<String, Object> set = new HashMap<String, Object>();
        set.put("uuid", p.getUniqueId().toString());
        set.put("save_name", lore.get(0));
        set.put("world", l.getWorld().getName());
        set.put("x", l.getX());
        set.put("y", l.getY());
        set.put("z", l.getZ());
        set.put("yaw", l.getYaw());
        set.put("pitch", l.getPitch());
        qf.doInsert("saves", set);
        close(p);
        p.sendMessage(plugin.getPluginName() + "Current location saved.");
    }

    private void loadSaves(final Player p) {
        close(p);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                TVMSavesGUI tvms = new TVMSavesGUI(plugin, 0, 44, p.getUniqueId().toString());
                ItemStack[] gui = tvms.getGUI();
                Inventory vmg = plugin.getServer().createInventory(p, 54, "§4VM Saves");
                vmg.setContents(gui);
                p.openInventory(vmg);
            }
        }, 2L);
    }

    private void message(final Player p) {
        close(p);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                TVMMessageGUI tvmm = new TVMMessageGUI(plugin, 0, 44, p.getUniqueId().toString(), false);
                ItemStack[] gui = tvmm.getGUI();
                Inventory vmg = plugin.getServer().createInventory(p, 54, "§4VM Messages");
                vmg.setContents(gui);
                p.openInventory(vmg);
            }
        }, 2L);
    }

    private void setBeacon(Player p) {
        UUID uuid = p.getUniqueId();
        String message = "You don't have enough tachyons to set a beacon signal!";
        if (checkTachyonLevel(uuid, plugin.getConfig().getInt("tachyon_use.beacon"))) {
            String ustr = uuid.toString();
            Location l = p.getLocation();
            // potential griefing, we need to check the location first!
            List<FLAG> flags = new ArrayList<FLAG>();
            if (plugin.getConfig().getBoolean("respect.factions")) {
                flags.add(FLAG.RESPECT_FACTIONS);
            }
            if (plugin.getConfig().getBoolean("respect.griefprevention")) {
                flags.add(FLAG.RESPECT_GRIEFPREVENTION);
            }
            if (plugin.getConfig().getBoolean("respect.towny")) {
                flags.add(FLAG.RESPECT_TOWNY);
            }
            if (plugin.getConfig().getBoolean("respect.worldborder")) {
                flags.add(FLAG.RESPECT_WORLDBORDER);
            }
            if (plugin.getConfig().getBoolean("respect.worldguard")) {
                flags.add(FLAG.RESPECT_WORLDGUARD);
            }
            Parameters params = new Parameters(p, flags);
            if (!plugin.getTardisAPI().getRespect().getRespect(l, params)) {
                close(p);
                p.sendMessage(plugin.getPluginName() + "You are not permitted to set a beacon signal here!");
                return;
            }
            Block b = l.getBlock().getRelative(BlockFace.DOWN);
            qf.saveBeaconBlock(ustr, b);
            b.setType(Material.BEACON);
            Block down = b.getRelative(BlockFace.DOWN);
            qf.saveBeaconBlock(ustr, down);
            down.setType(Material.IRON_BLOCK);
            List<BlockFace> faces = Arrays.asList(BlockFace.EAST, BlockFace.NORTH_EAST, BlockFace.NORTH, BlockFace.NORTH_WEST, BlockFace.WEST, BlockFace.SOUTH_WEST, BlockFace.SOUTH, BlockFace.SOUTH_EAST);
            for (BlockFace f : faces) {
                qf.saveBeaconBlock(ustr, down.getRelative(f));
                down.getRelative(f).setType(Material.IRON_BLOCK);
            }
            plugin.getBeaconSetters().add(uuid);
            message = "Beacon signal set, don't move!";
        }
        close(p);
        p.sendMessage(plugin.getPluginName() + message);
    }

    private void doWarp(final Player p, Inventory inv) {
        ItemStack display = inv.getItem(4);
        ItemMeta dim = display.getItemMeta();
        List<String> lore = dim.getLore();
        List<String> dest;
        if (!lore.get(0).trim().isEmpty()) {
            dest = Arrays.asList(lore.get(0).trim().split(" "));
        } else {
            dest = new ArrayList<String>();
        }
        List<String> worlds = new ArrayList<String>();
        Location l;
        // set parameters
        List<FLAG> flags = new ArrayList<FLAG>();
        flags.add(FLAG.PERMS_AREA);
        flags.add(FLAG.PERMS_NETHER);
        flags.add(FLAG.PERMS_THEEND);
        flags.add(FLAG.PERMS_WORLD);
        if (plugin.getConfig().getBoolean("respect.factions")) {
            flags.add(FLAG.RESPECT_FACTIONS);
        }
        if (plugin.getConfig().getBoolean("respect.griefprevention")) {
            flags.add(FLAG.RESPECT_GRIEFPREVENTION);
        }
        if (plugin.getConfig().getBoolean("respect.towny")) {
            flags.add(FLAG.RESPECT_TOWNY);
        }
        if (plugin.getConfig().getBoolean("respect.worldborder")) {
            flags.add(FLAG.RESPECT_WORLDBORDER);
        }
        if (plugin.getConfig().getBoolean("respect.worldguard")) {
            flags.add(FLAG.RESPECT_WORLDGUARD);
        }
        Parameters params = new Parameters(p, flags);
        int required;
        switch (dest.size()) {
            case 1:
            case 2:
            case 3:
                required = plugin.getConfig().getInt("tachyon_use.travel.world");
                // only world specified (or incomplete setting)
                worlds.add(dest.get(0));
                l = plugin.getTardisAPI().getRandomLocation(worlds, null, params);
                break;
            case 4:
                required = plugin.getConfig().getInt("tachyon_use.travel.coords");
                // world, x, y, z specified
                World w;
                if (dest.get(0).contains("~")) {
                    // relative location
                    w = p.getLocation().getWorld();
                } else {
                    w = plugin.getServer().getWorld(dest.get(0));
                    if (w == null) {
                        close(p);
                        p.sendMessage(plugin.getPluginName() + "World does not exist!");
                        return;
                    }
                }
                double x;
                double y;
                double z;
                try {
                    x = Double.parseDouble(dest.get(1));
                    y = Double.parseDouble(dest.get(2));
                    z = Double.parseDouble(dest.get(3));
                } catch (NumberFormatException e) {
                    close(p);
                    p.sendMessage(plugin.getPluginName() + "Could not parse coordinates!");
                    return;
                }
                l = new Location(w, x, y, z);
                // check block has space for player
                if (!l.getBlock().getType().equals(Material.AIR)) {
                    p.sendMessage(plugin.getPluginName() + "Destination block is not AIR! Adjusting...");
                    // get highest block at these coords
                    int highest = l.getWorld().getHighestBlockYAt(l);
                    l.setY(highest);
                }
                break;
            default:
                required = plugin.getConfig().getInt("tachyon_use.travel.random");
                // random
                l = plugin.getTardisAPI().getRandomLocation(plugin.getTardisAPI().getWorlds(), null, params);
                break;
        }
        if (!checkTachyonLevel(p.getUniqueId(), required)) {
            close(p);
            p.sendMessage(plugin.getPluginName() + "You need at least " + required + " tachyons to travel!");
            return;
        }
        if (l != null) {
            final Location warp = l;
            close(p);
            p.sendMessage(plugin.getPluginName() + "Standby for Vortex travel...");
            while (!warp.getChunk().isLoaded()) {
                warp.getChunk().load();
            }
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    p.teleport(warp);
                }
            }, 60L);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    p.teleport(warp);
                }
            }, 65L);
            // remove tachyons
            qf.alterTachyons(p.getUniqueId().toString(), -required);
        } else {
            //close(p);
            p.sendMessage(plugin.getPluginName() + "No location could be found within those parameters.");
        }
    }

    /**
     * Closes the inventory.
     *
     * @param p the player using the GUI
     */
    public void close(final Player p) {
        components = Arrays.asList("", "", "", "", "");
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                p.closeInventory();
            }
        }, 1L);
    }

    @EventHandler(ignoreCancelled = true)
    public void onMenuDrag(InventoryDragEvent event) {
        Inventory inv = event.getInventory();
        String title = inv.getTitle();
        if (!titles.contains(title)) {
            return;
        }
        Set<Integer> slots = event.getRawSlots();
        for (Integer slot : slots) {
            if ((slot >= 0 && slot < 81)) {
                event.setCancelled(true);
            }
        }
    }

    /**
     * Check they have enough tachyons.
     *
     * @param uuid the String UUID of the player to check
     * @param required the minimum amount of Tachyon required
     */
    private boolean checkTachyonLevel(String uuid, int required) {
        TVMResultSetManipulator rs = new TVMResultSetManipulator(plugin, uuid);
        if (!rs.resultSet()) {
            return false;
        }
        return rs.getTachyonLevel() >= required;
    }

    /**
     * Check they have enough tachyons.
     *
     * @param uuid the UUID of the player to check
     * @param required the minimum amount of Tachyon required
     */
    private boolean checkTachyonLevel(UUID uuid, int required) {
        return checkTachyonLevel(uuid.toString(), required);
    }
}
