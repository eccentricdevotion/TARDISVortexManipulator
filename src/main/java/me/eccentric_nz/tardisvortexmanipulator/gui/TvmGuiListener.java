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

import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.enumeration.Flag;
import me.eccentric_nz.tardisvortexmanipulator.TardisVortexManipulatorPlugin;
import me.eccentric_nz.tardisvortexmanipulator.TvmUtils;
import me.eccentric_nz.tardisvortexmanipulator.database.TvmQueryFactory;
import org.bukkit.ChatColor;
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
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * @author eccentric_nz
 */
public class TvmGuiListener extends TvmGuiCommon implements Listener {

    private final TardisVortexManipulatorPlugin plugin;
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
    TvmQueryFactory queryFactory;

    public TvmGuiListener(TardisVortexManipulatorPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
        // init string positions
        pos = new int[6];
        for (int i = 0; i < 6; i++) {
            pos[i] = 0;
        }
        queryFactory = new TvmQueryFactory(this.plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onGuiClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        String name = view.getTitle();
        if (name.equals("§4Vortex Manipulator")) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            int slot = event.getRawSlot();
            if (slot >= 0 && slot < 54) {
                switch (slot) {
                    case 11:
                        // world
                        which = 0;
                        resetTrackers();
                        break;
                    case 12:
                        // one
                        updateDisplay(view, '1');
                        break;
                    case 13:
                        // two
                        if (letters.contains(which)) {
                            updateDisplay(view, two[t2]);
                            t2++;
                            if (t2 == two.length) {
                                t2 = 0;
                            }
                        } else {
                            updateDisplay(view, '2');
                        }
                        break;
                    case 14:
                        // three
                        if (letters.contains(which)) {
                            updateDisplay(view, three[t3]);
                            t3++;
                            if (t3 == three.length) {
                                t3 = 0;
                            }
                        } else {
                            updateDisplay(view, '3');
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
                            updateDisplay(view, four[t4]);
                            t4++;
                            if (t4 == four.length) {
                                t4 = 0;
                            }
                        } else {
                            updateDisplay(view, '4');
                        }
                        break;
                    case 22:
                        // five
                        if (letters.contains(which)) {
                            updateDisplay(view, five[t5]);
                            t5++;
                            if (t5 == five.length) {
                                t5 = 0;
                            }
                        } else {
                            updateDisplay(view, '5');
                        }
                        break;
                    case 23:
                        // six
                        if (letters.contains(which)) {
                            updateDisplay(view, six[t6]);
                            t6++;
                            if (t6 == six.length) {
                                t6 = 0;
                            }
                        } else {
                            updateDisplay(view, '6');
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
                            updateDisplay(view, seven[t7]);
                            t7++;
                            if (t7 == seven.length) {
                                t7 = 0;
                            }
                        } else {
                            updateDisplay(view, '7');
                        }
                        break;
                    case 31:
                        // eight
                        if (letters.contains(which)) {
                            updateDisplay(view, eight[t8]);
                            t8++;
                            if (t8 == eight.length) {
                                t8 = 0;
                            }
                        } else {
                            updateDisplay(view, '8');
                        }
                        break;
                    case 32:
                        // nine
                        if (letters.contains(which)) {
                            updateDisplay(view, nine[t9]);
                            t9++;
                            if (t9 == nine.length) {
                                t9 = 0;
                            }
                        } else {
                            updateDisplay(view, '9');
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
                        updateDisplay(view, star[ts]);
                        ts++;
                        if (ts == star.length) {
                            ts = 0;
                        }
                        break;
                    case 40:
                        //zero
                        updateDisplay(view, '0');
                        break;
                    case 41:
                        // hash
                        if (letters.contains(which) || components.get(0).startsWith("~")) {
                            updateDisplay(view, hash[th]);
                            th++;
                            if (th == hash.length) {
                                th = 0;
                            }
                        } else {
                            updateDisplay(view, '-');
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
                            case 4 ->
                                    // save
                                    saveCurrentLocation(player, view);
                            case 5 ->
                                    // scan
                                    scanLifesigns(player, view);
                            default ->
                                    // warp
                                    doWarp(player, view);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void updateDisplay(InventoryView view, char s) {
        ItemStack display = view.getItem(4);
        ItemMeta displayMeta = display.getItemMeta();
        char[] chars = (components.get(which).isEmpty()) ? new char[1] : components.get(which).toCharArray();
        if (pos[which] >= chars.length) {
            char[] temp = chars.clone();
            chars = new char[pos[which] + 1];
            int i = 0;
            for (char ignored : temp) {
                chars[i] = temp[i];
                i++;
            }
        }
        chars[pos[which]] = s;
        String component = new String(chars);
        String combined = switch (which) {
            case 0 -> component + " " + components.get(1) + " " + components.get(2) + " " + components.get(3);
            case 1 -> components.get(0) + " " + component + " " + components.get(2) + " " + components.get(3);
            case 2 -> components.get(0) + " " + components.get(1) + " " + component + " " + components.get(3);
            case 3 -> components.get(0) + " " + components.get(1) + " " + components.get(2) + " " + component;
            default -> component;
        };
        components.set(which, component);
        List<String> displayLore = Collections.singletonList(ChatColor.GRAY + combined);
        displayMeta.setLore(displayLore);
        display.setItemMeta(displayMeta);
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

    private void saveCurrentLocation(Player player, InventoryView view) {
        ItemStack display = view.getItem(4);
        ItemMeta displayMeta = display.getItemMeta();
        List<String> lore = displayMeta.getLore();
        String name = lore.get(0);
        if (name.isEmpty()) {
            player.sendMessage(plugin.getPluginName() + "You need to enter a save name!");
            return;
        }
        Location location = player.getLocation();
        HashMap<String, Object> set = new HashMap<>();
        set.put("uuid", player.getUniqueId().toString());
        set.put("save_name", lore.get(0));
        set.put("world", location.getWorld().getName());
        set.put("x", location.getX());
        set.put("y", location.getY());
        set.put("z", location.getZ());
        set.put("yaw", location.getYaw());
        set.put("pitch", location.getPitch());
        queryFactory.doInsert("saves", set);
        close(player);
        player.sendMessage(plugin.getPluginName() + "Current location saved.");
    }

    private void scanLifesigns(Player player, InventoryView view) {
        close(player);
        if (!player.hasPermission("vm.lifesigns")) {
            player.sendMessage(plugin.getPluginName() + "You don't have permission to use the lifesigns scanner!");
            return;
        }
        int required = plugin.getConfig().getInt("tachyon_use.lifesigns");
        if (!TvmUtils.checkTachyonLevel(player.getUniqueId().toString(), required)) {
            player.sendMessage(plugin.getPluginName() + "You don't have enough tachyons to use the lifesigns scanner!");
            return;
        }
        // remove tachyons
        queryFactory.alterTachyons(player.getUniqueId().toString(), -required);
        // process GUI
        ItemStack display = view.getItem(4);
        ItemMeta displayMeta = display.getItemMeta();
        List<String> lore = displayMeta.getLore();
        String playerName = lore.get(0).trim();
        if (playerName.isEmpty()) {
            player.sendMessage(plugin.getPluginName() + "Nearby entities:");
            // scan nearby entities
            double d = plugin.getConfig().getDouble("lifesign_scan_distance");
            List<Entity> nearbyEntities = player.getNearbyEntities(d, d, d);
            if (nearbyEntities.size() > 0) {
                // record nearby entities
                HashMap<EntityType, Integer> scannedEntities = new HashMap<>();
                List<String> playerNames = new ArrayList<>();
                nearbyEntities.forEach((entity) -> {
                    EntityType entityType = entity.getType();
                    if (TARDISConstants.ENTITY_TYPES.contains(entityType)) {
                        Integer entityCount = scannedEntities.getOrDefault(entityType, 0);
                        boolean visible = true;
                        if (entityType.equals(EntityType.PLAYER)) {
                            Player entityPlayer = (Player) entity;
                            if (player.canSee(entityPlayer)) {
                                playerNames.add(entityPlayer.getName());
                            } else {
                                visible = false;
                            }
                        }
                        if (visible) {
                            scannedEntities.put(entityType, entityCount + 1);
                        }
                    }
                });
                scannedEntities.forEach((key, value) -> {
                    String message = "";
                    StringBuilder stringBuilder = new StringBuilder();
                    if (key.equals(EntityType.PLAYER) && playerNames.size() > 0) {
                        playerNames.forEach((pn) -> stringBuilder.append(", ").append(pn));
                        message = " (" + stringBuilder.substring(2) + ")";
                    }
                    player.sendMessage("    " + key + ": " + value + message);
                });
                scannedEntities.clear();
            } else {
                player.sendMessage("No nearby entities.");
            }
        } else {
            Player scanned = plugin.getServer().getPlayer(playerName);
            if (scanned == null) {
                player.sendMessage(plugin.getPluginName() + "Could not find a player with that name!");
                return;
            }
            if (!scanned.isOnline()) {
                player.sendMessage(plugin.getPluginName() + playerName + " is not online!");
                return;
            }
            // getHealth() / getMaxHealth() * getHealthScale()
            double maxHealth = scanned.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
            double health = scanned.getHealth() / maxHealth * scanned.getHealthScale();
            float hunger = (scanned.getFoodLevel() / 20F) * 100;
            int air = scanned.getRemainingAir();
            player.sendMessage(plugin.getPluginName() + playerName + "'s lifesigns:");
            player.sendMessage("Has been alive for: " + TvmUtils.convertTicksToTime(scanned.getTicksLived()));
            player.sendMessage("Health: " + String.format("%.1f", health / 2) + " hearts");
            player.sendMessage("Hunger bar: " + String.format("%.2f", hunger) + "%");
            player.sendMessage("Air: ~" + (air / 20) + " seconds remaining");
        }
    }

    private void loadSaves(Player p) {
        close(p);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            TvmSavesGui tvms = new TvmSavesGui(plugin, 0, 44, p.getUniqueId().toString());
            ItemStack[] gui = tvms.getGui();
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
            TvmMessageGui tvmm = new TvmMessageGui(plugin, 0, 44, p.getUniqueId().toString());
            ItemStack[] gui = tvmm.getGui();
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
        if (TvmUtils.checkTachyonLevel(uuid.toString(), required)) {
            String ustr = uuid.toString();
            Location l = p.getLocation();
            // potential griefing, we need to check the location first!
            List<Flag> flags = new ArrayList<>();
            if (plugin.getConfig().getBoolean("respect.factions")) {
                flags.add(Flag.RESPECT_FACTIONS);
            }
            if (plugin.getConfig().getBoolean("respect.griefprevention")) {
                flags.add(Flag.RESPECT_GRIEFPREVENTION);
            }
            if (plugin.getConfig().getBoolean("respect.towny")) {
                flags.add(Flag.RESPECT_TOWNY);
            }
            if (plugin.getConfig().getBoolean("respect.worldborder")) {
                flags.add(Flag.RESPECT_WORLDBORDER);
            }
            if (plugin.getConfig().getBoolean("respect.worldguard")) {
                flags.add(Flag.RESPECT_WORLDGUARD);
            }
            Parameters params = new Parameters(p, flags);
            if (!plugin.getTardisApi().getRespect().getRespect(l, params)) {
                close(p);
                p.sendMessage(plugin.getPluginName() + "You are not permitted to set a beacon signal here!");
                return;
            }
            Block b = l.getBlock().getRelative(BlockFace.DOWN);
            queryFactory.saveBeaconBlock(ustr, b);
            b.setBlockData(Material.BEACON.createBlockData());
            Block down = b.getRelative(BlockFace.DOWN);
            queryFactory.saveBeaconBlock(ustr, down);
            BlockData iron = Material.IRON_BLOCK.createBlockData();
            down.setBlockData(iron);
            List<BlockFace> faces = Arrays.asList(BlockFace.EAST, BlockFace.NORTH_EAST, BlockFace.NORTH, BlockFace.NORTH_WEST, BlockFace.WEST, BlockFace.SOUTH_WEST, BlockFace.SOUTH, BlockFace.SOUTH_EAST);
            faces.forEach((f) -> {
                queryFactory.saveBeaconBlock(ustr, down.getRelative(f));
                down.getRelative(f).setBlockData(iron);
            });
            plugin.getBeaconSetters().add(uuid);
            message = "Beacon signal set, don't move!";
            // remove tachyons
            queryFactory.alterTachyons(p.getUniqueId().toString(), -required);
        }
        close(p);
        p.sendMessage(plugin.getPluginName() + message);
    }

    private void doWarp(Player p, InventoryView view) {
        ItemStack display = view.getItem(4);
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
        List<Flag> flags = new ArrayList<>();
        flags.add(Flag.PERMS_AREA);
        flags.add(Flag.PERMS_NETHER);
        flags.add(Flag.PERMS_THEEND);
        flags.add(Flag.PERMS_WORLD);
        if (plugin.getConfig().getBoolean("respect.factions")) {
            flags.add(Flag.RESPECT_FACTIONS);
        }
        if (plugin.getConfig().getBoolean("respect.griefprevention")) {
            flags.add(Flag.RESPECT_GRIEFPREVENTION);
        }
        if (plugin.getConfig().getBoolean("respect.towny")) {
            flags.add(Flag.RESPECT_TOWNY);
        }
        if (plugin.getConfig().getBoolean("respect.worldborder")) {
            flags.add(Flag.RESPECT_WORLDBORDER);
        }
        if (plugin.getConfig().getBoolean("respect.worldguard")) {
            flags.add(Flag.RESPECT_WORLDGUARD);
        }
        Parameters params = new Parameters(p, flags);
        int required;
        switch (dest.size()) {
            case 1, 2, 3 -> {
                required = plugin.getConfig().getInt("tachyon_use.travel.world");
                // only world specified (or incomplete setting)
                // check world is an actual world
                if (plugin.getServer().getWorld(dest.get(0)) == null) {
                    close(p);
                    p.sendMessage(plugin.getPluginName() + "World does not exist!");
                    return;
                }
                // check world is enabled for travel
                if (!plugin.getTardisApi().getWorlds().contains(dest.get(0))) {
                    close(p);
                    p.sendMessage(plugin.getPluginName() + "You cannot travel to this world using the Vortex Manipulator!");
                    return;
                }
                worlds.add(dest.get(0));
                l = plugin.getTardisApi().getRandomLocation(worlds, null, params);
            }
            case 4 -> {
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
                    if (!plugin.getTardisApi().getWorlds().contains(dest.get(0))) {
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
            }
            default -> {
                required = plugin.getConfig().getInt("tachyon_use.travel.random");
                // random
                l = plugin.getTardisApi().getRandomLocation(plugin.getTardisApi().getWorlds(), null, params);
            }
        }
        UUID uuid = p.getUniqueId();
        if (!TvmUtils.checkTachyonLevel(uuid.toString(), required)) {
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
            if (!TvmUtils.checkTachyonLevel(uuid.toString(), actual)) {
                p.sendMessage(plugin.getPluginName() + "You need at least " + actual + " tachyons to travel!");
                return;
            }
            p.sendMessage(plugin.getPluginName() + "Standby for Vortex travel...");
            while (!l.getChunk().isLoaded()) {
                l.getChunk().load();
            }
            TvmUtils.movePlayers(players, l, p.getLocation().getWorld());
            // remove tachyons
            queryFactory.alterTachyons(uuid.toString(), -actual);
        } else {
            p.sendMessage(plugin.getPluginName() + "No location could be found within those parameters.");
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onMenuDrag(InventoryDragEvent event) {
        InventoryView view = event.getView();
        String title = view.getTitle();
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
