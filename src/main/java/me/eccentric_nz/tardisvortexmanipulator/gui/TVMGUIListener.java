/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator.gui;

import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.enumeration.FLAG;
import me.eccentric_nz.tardisvortexmanipulator.TARDISVortexManipulator;
import me.eccentric_nz.tardisvortexmanipulator.TVMUtils;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMQueryFactory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * @author eccentric_nz
 */
public class TVMGUIListener extends TVMGUICommon implements Listener {

    private final TARDISVortexManipulator plugin;
    List<String> titles = Arrays.asList("§4Vortex Manipulator", "§4VM Messages", "§4VM Saves");
    List<String> components = Arrays.asList("", "", "", "", "", "");
    List<Integer> letters = Arrays.asList(0, 4, 5);
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
        super(plugin);
        this.plugin = plugin;
        // init string positions
        pos = new int[6];
        for (int i = 0; i < 6; i++) {
            pos[i] = 0;
        }
        qf = new TVMQueryFactory(this.plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onGUIClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        String name = inv.getTitle();
        if (name.equals("§4Vortex Manipulator")) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
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
                        if (letters.contains(which)) {
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
                        if (letters.contains(which)) {
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
                    case 18:
                        // lifesigns
                        which = 5;
                        resetTrackers();
                        break;
                    case 20:
                        // x
                        which = 1;
                        resetTrackers();
                        break;
                    case 21:
                        // four
                        if (letters.contains(which)) {
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
                        if (letters.contains(which)) {
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
                        if (letters.contains(which)) {
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
                        if (letters.contains(which)) {
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
                        if (letters.contains(which)) {
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
                        if (letters.contains(which)) {
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
                        if (letters.contains(which) || components.get(0).startsWith("~")) {
                            updateDisplay(inv, hash[th]);
                            th++;
                            if (th == hash.length) {
                                th = 0;
                            }
                        } else {
                            updateDisplay(inv, '-');
                        }
                        break;
                    case 43:
                        // beacon
                        setBeacon(player);
                        break;
                    case 45:
                        // close
                        close(player);
                        components = Arrays.asList("", "", "", "", "", "");
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
                        switch (which) {
                            case 4:
                                // save
                                saveCurrentLocation(player, inv);
                                break;
                            case 5:
                                // scan
                                scanLifesigns(player, inv);
                                break;
                            default:
                                // warp
                                doWarp(player, inv);
                                break;
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
        String name = lore.get(0);
        if (name.isEmpty()) {
            p.sendMessage(plugin.getPluginName() + "You need to enter a save name!");
            return;
        }
        Location l = p.getLocation();
        HashMap<String, Object> set = new HashMap<>();
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

    private void scanLifesigns(Player p, Inventory inv) {
        close(p);
        if (!p.hasPermission("vm.lifesigns")) {
            p.sendMessage(plugin.getPluginName() + "You don't have permission to use the lifesigns scanner!");
            return;
        }
        int required = plugin.getConfig().getInt("tachyon_use.lifesigns");
        if (!TVMUtils.checkTachyonLevel(p.getUniqueId().toString(), required)) {
            p.sendMessage(plugin.getPluginName() + "You don't have enough tachyons to use the lifesigns scanner!");
            return;
        }
        // remove tachyons
        qf.alterTachyons(p.getUniqueId().toString(), -required);
        // process GUI
        ItemStack display = inv.getItem(4);
        ItemMeta dim = display.getItemMeta();
        List<String> lore = dim.getLore();
        String pname = lore.get(0).trim();
        if (pname.isEmpty()) {
            p.sendMessage(plugin.getPluginName() + "Nearby entities:");
            // scan nearby entities
            double d = plugin.getConfig().getDouble("lifesign_scan_distance");
            List<Entity> ents = p.getNearbyEntities(d, d, d);
            if (ents.size() > 0) {
                // record nearby entities
                HashMap<EntityType, Integer> scannedentities = new HashMap<>();
                List<String> playernames = new ArrayList<>();
                ents.forEach((k) -> {
                    EntityType et = k.getType();
                    if (TARDISConstants.ENTITY_TYPES.contains(et)) {
                        Integer entity_count = (scannedentities.containsKey(et)) ? scannedentities.get(et) : 0;
                        boolean visible = true;
                        if (et.equals(EntityType.PLAYER)) {
                            Player entPlayer = (Player) k;
                            if (p.canSee(entPlayer)) {
                                playernames.add(entPlayer.getName());
                            } else {
                                visible = false;
                            }
                        }
                        if (visible) {
                            scannedentities.put(et, entity_count + 1);
                        }
                    }
                });
                scannedentities.entrySet().forEach((entry) -> {
                    String message = "";
                    StringBuilder buf = new StringBuilder();
                    if (entry.getKey().equals(EntityType.PLAYER) && playernames.size() > 0) {
                        playernames.forEach((pn) -> {
                            buf.append(", ").append(pn);
                        });
                        message = " (" + buf.toString().substring(2) + ")";
                    }
                    p.sendMessage("    " + entry.getKey() + ": " + entry.getValue() + message);
                });
                scannedentities.clear();
            } else {
                p.sendMessage("No nearby entities.");
            }
        } else {
            Player scanned = plugin.getServer().getPlayer(pname);
            if (scanned == null) {
                p.sendMessage(plugin.getPluginName() + "Could not find a player with that name!");
                return;
            }
            if (!scanned.isOnline()) {
                p.sendMessage(plugin.getPluginName() + pname + " is not online!");
                return;
            }
            // getHealth() / getMaxHealth() * getHealthScale()
            double mh = scanned.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
            double health = scanned.getHealth() / mh * scanned.getHealthScale();
            float hunger = (scanned.getFoodLevel() / 20F) * 100;
            int air = scanned.getRemainingAir();
            p.sendMessage(plugin.getPluginName() + pname + "'s lifesigns:");
            p.sendMessage("Has been alive for: " + TVMUtils.convertTicksToTime(scanned.getTicksLived()));
            p.sendMessage("Health: " + String.format("%.1f", health / 2) + " hearts");
            p.sendMessage("Hunger bar: " + String.format("%.2f", hunger) + "%");
            p.sendMessage("Air: ~" + (air / 20) + " seconds remaining");
        }
    }

    private void loadSaves(Player p) {
        close(p);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            TVMSavesGUI tvms = new TVMSavesGUI(plugin, 0, 44, p.getUniqueId().toString());
            ItemStack[] gui = tvms.getGUI();
            Inventory vmg = plugin.getServer().createInventory(p, 54, "§4VM Saves");
            vmg.setContents(gui);
            p.openInventory(vmg);
        }, 2L);
    }

    private void message(Player p) {
        close(p);
        if (!p.hasPermission("vm.message")) {
            p.sendMessage(plugin.getPluginName() + "You don't have permission to use Vortex messages!");
            return;
        }
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            TVMMessageGUI tvmm = new TVMMessageGUI(plugin, 0, 44, p.getUniqueId().toString());
            ItemStack[] gui = tvmm.getGUI();
            Inventory vmg = plugin.getServer().createInventory(p, 54, "§4VM Messages");
            vmg.setContents(gui);
            p.openInventory(vmg);
        }, 2L);
    }

    private void setBeacon(Player p) {
        if (!p.hasPermission("vm.beacon")) {
            close(p);
            p.sendMessage(plugin.getPluginName() + "You don't have permission to set a beacon signal!");
            return;
        }
        UUID uuid = p.getUniqueId();
        String message = "You don't have enough tachyons to set a beacon signal!";
        int required = plugin.getConfig().getInt("tachyon_use.beacon");
        if (TVMUtils.checkTachyonLevel(uuid.toString(), required)) {
            String ustr = uuid.toString();
            Location l = p.getLocation();
            // potential griefing, we need to check the location first!
            List<FLAG> flags = new ArrayList<>();
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
            b.setBlockData(Material.BEACON.createBlockData());
            Block down = b.getRelative(BlockFace.DOWN);
            qf.saveBeaconBlock(ustr, down);
            BlockData iron = Material.IRON_BLOCK.createBlockData();
            down.setBlockData(iron);
            List<BlockFace> faces = Arrays.asList(BlockFace.EAST, BlockFace.NORTH_EAST, BlockFace.NORTH, BlockFace.NORTH_WEST, BlockFace.WEST, BlockFace.SOUTH_WEST, BlockFace.SOUTH, BlockFace.SOUTH_EAST);
            faces.forEach((f) -> {
                qf.saveBeaconBlock(ustr, down.getRelative(f));
                down.getRelative(f).setBlockData(iron);
            });
            plugin.getBeaconSetters().add(uuid);
            message = "Beacon signal set, don't move!";
            // remove tachyons
            qf.alterTachyons(p.getUniqueId().toString(), -required);
        }
        close(p);
        p.sendMessage(plugin.getPluginName() + message);
    }

    private void doWarp(Player p, Inventory inv) {
        ItemStack display = inv.getItem(4);
        ItemMeta dim = display.getItemMeta();
        List<String> lore = dim.getLore();
        List<String> dest;
        if (!lore.get(0).trim().isEmpty()) {
            dest = Arrays.asList(lore.get(0).trim().split(" "));
        } else {
            dest = new ArrayList<>();
        }
        List<String> worlds = new ArrayList<>();
        Location l;
        // set parameters
        List<FLAG> flags = new ArrayList<>();
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
                // check world is an actual world
                if (plugin.getServer().getWorld(dest.get(0)) == null) {
                    close(p);
                    p.sendMessage(plugin.getPluginName() + "World does not exist!");
                    return;
                }
                // check world is enabled for travel
                if (!plugin.getTardisAPI().getWorlds().contains(dest.get(0))) {
                    close(p);
                    p.sendMessage(plugin.getPluginName() + "You cannot travel to this world using the Vortex Manipulator!");
                    return;
                }
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
                    // check world is enabled for travel
                    if (!plugin.getTardisAPI().getWorlds().contains(dest.get(0))) {
                        close(p);
                        p.sendMessage(plugin.getPluginName() + "You cannot travel to this world using the Vortex Manipulator!");
                        return;
                    }
                }
                double x;
                double y;
                double z;
                try {
                    if (dest.get(1).startsWith("~")) {
                        // get players current location
                        Location tl = p.getLocation();
                        double tx = tl.getX();
                        double ty = tl.getY();
                        double tz = tl.getZ();
                        // strip off the initial "~" and add to current position
                        x = tx + Double.parseDouble(dest.get(1).substring(1));
                        y = ty + Double.parseDouble(dest.get(2).substring(1));
                        z = tz + Double.parseDouble(dest.get(3).substring(1));
                    } else {
                        x = Double.parseDouble(dest.get(1));
                        y = Double.parseDouble(dest.get(2));
                        z = Double.parseDouble(dest.get(3));
                    }
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
        UUID uuid = p.getUniqueId();
        if (!TVMUtils.checkTachyonLevel(uuid.toString(), required)) {
            close(p);
            p.sendMessage(plugin.getPluginName() + "You need at least " + required + " tachyons to travel!");
            return;
        }
        if (l != null) {
            close(p);
            List<Player> players = new ArrayList<>();
            players.add(p);
            if (plugin.getConfig().getBoolean("allow.multiple")) {
                p.getNearbyEntities(0.5d, 0.5d, 0.5d).forEach((e) -> {
                    if (e instanceof Player && !e.getUniqueId().equals(uuid)) {
                        players.add((Player) e);
                    }
                });
            }
            int actual = required * players.size();
            if (!TVMUtils.checkTachyonLevel(uuid.toString(), actual)) {
                p.sendMessage(plugin.getPluginName() + "You need at least " + actual + " tachyons to travel!");
                return;
            }
            p.sendMessage(plugin.getPluginName() + "Standby for Vortex travel...");
            while (!l.getChunk().isLoaded()) {
                l.getChunk().load();
            }
            TVMUtils.movePlayers(players, l, p.getLocation().getWorld());
            // remove tachyons
            qf.alterTachyons(uuid.toString(), -actual);
        } else {
            //close(p);
            p.sendMessage(plugin.getPluginName() + "No location could be found within those parameters.");
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onMenuDrag(InventoryDragEvent event) {
        Inventory inv = event.getInventory();
        String title = inv.getTitle();
        if (!titles.contains(title)) {
            return;
        }
        Set<Integer> slots = event.getRawSlots();
        slots.forEach((slot) -> {
            if ((slot >= 0 && slot < 81)) {
                event.setCancelled(true);
            }
        });
    }
}
