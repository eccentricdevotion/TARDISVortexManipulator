/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator.gui;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import me.eccentric_nz.tardisvortexmanipulator.TARDISVortexManipulator;
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
public class TVMSavesGUIListener implements Listener {

    private final TARDISVortexManipulator plugin;
    List<String> components = Arrays.asList("", "", "", "");
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

    public TVMSavesGUIListener(TARDISVortexManipulator plugin) {
        this.plugin = plugin;
        // init string positions
        this.pos = new int[4];
        for (int i = 0; i < 4; i++) {
            this.pos[i] = 0;
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onGUIClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        String name = inv.getTitle();
        if (name.equals("§4VM Saves")) {
            event.setCancelled(true);
            final Player player = (Player) event.getWhoClicked();
            UUID uuid = player.getUniqueId();
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
                        if (which == 0) {
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
                        if (which == 0) {
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
                        saveCurrentLocation(player);
                        break;
                    case 20:
                        // x
                        which = 1;
                        resetTrackers();
                        break;
                    case 21:
                        // four
                        if (which == 0) {
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
                        if (which == 0) {
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
                        if (which == 0) {
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
                        if (which == 0) {
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
                        if (which == 0) {
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
                        if (which == 0) {
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
                        // warp
                        doWarp(player);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void updateDisplay(Inventory inv, char s) {
        ItemStack display = inv.getItem(2);
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
            default:
                combined = components.get(0) + " " + components.get(1) + " " + components.get(2) + " " + comp;
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

    private void saveCurrentLocation(Player p) {

    }

    private void loadSaves(Player p) {

    }

    private void message(Player p) {

    }

    private void setBeacon(Player p) {

    }

    private void doWarp(Player p) {

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

    @EventHandler(ignoreCancelled = true)
    public void onMenuDrag(InventoryDragEvent event) {
        Inventory inv = event.getInventory();
        String title = inv.getTitle();
        if (!title.equals("§4Vortex Manipulator")) {
            return;
        }
        Set<Integer> slots = event.getRawSlots();
        for (Integer slot : slots) {
            if ((slot >= 0 && slot < 81)) {
                event.setCancelled(true);
            }
        }
    }
}
