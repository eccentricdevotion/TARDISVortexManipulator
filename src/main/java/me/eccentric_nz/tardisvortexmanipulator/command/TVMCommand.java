package me.eccentric_nz.tardisvortexmanipulator.command;

import java.util.ArrayList;
import java.util.List;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.enumeration.FLAG;
import me.eccentric_nz.tardisvortexmanipulator.TARDISVortexManipulator;
import me.eccentric_nz.tardisvortexmanipulator.TVMUtils;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMQueryFactory;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetManipulator;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetWarpByName;
import me.eccentric_nz.tardisvortexmanipulator.gui.TVMGUI;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class TVMCommand implements CommandExecutor {

    private final TARDISVortexManipulator plugin;

    public TVMCommand(TARDISVortexManipulator plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(final CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("vm")) {
            Player player = null;
            if (sender instanceof Player) {
                player = (Player) sender;
            }
            if (player == null) {
                sender.sendMessage(plugin.getPluginName() + "Command can only be used by a player!");
                return true;
            }
            if (!player.hasPermission("vm.teleport")) {
                player.sendMessage(plugin.getPluginName() + "You don't have permission to use that command!");
                return true;
            }
            ItemStack is = player.getItemInHand();
            if (is != null && is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().getDisplayName().equals("Vortex Manipulator")) {
                if (args[0].equalsIgnoreCase("gui")) {
                    // get tachyon level
                    TVMResultSetManipulator rs = new TVMResultSetManipulator(plugin, player.getUniqueId().toString());
                    if (rs.resultSet()) {
                        // open gui
                        ItemStack[] gui = new TVMGUI(plugin, rs.getTachyonLevel()).getGUI();
                        Inventory vmg = plugin.getServer().createInventory(player, 54, "§4Vortex Manipulator");
                        vmg.setContents(gui);
                        player.openInventory(vmg);
                        return true;
                    }
                }
                if (args[0].equalsIgnoreCase("go")) {
                    if (args.length < 2) {
                        player.sendMessage(plugin.getPluginName() + "You need to specify a save name!");
                        return true;
                    }
                    // check save exists
                    TVMResultSetWarpByName rsw = new TVMResultSetWarpByName(plugin, args[1]);
                    if (!rsw.resultSet()) {
                        player.sendMessage(plugin.getPluginName() + "Save does not exist!");
                        return true;
                    }
                    Location l = rsw.getWarp();
                    player.sendMessage(plugin.getPluginName() + "Standby for Vortex travel to " + args[1] + "...");
                    while (!l.getChunk().isLoaded()) {
                        l.getChunk().load();
                    }
                    TVMUtils.movePlayer(player, l, player.getLocation().getWorld());
                    // remove tachyons
                    new TVMQueryFactory(plugin).alterTachyons(player.getUniqueId().toString(), -plugin.getConfig().getInt("tachyon_use.saved"));
                }
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
                Parameters params = new Parameters(player, flags);
                int required;
                List<String> worlds = new ArrayList<String>();
                Location l;
                switch (args.length) {
                    case 1:
                    case 2:
                    case 3:
                        required = plugin.getConfig().getInt("tachyon_use.travel.world");
                        // only world specified (or incomplete setting)
                        worlds.add(args[0]);
                        l = plugin.getTardisAPI().getRandomLocation(worlds, null, params);
                        break;
                    case 4:
                        required = plugin.getConfig().getInt("tachyon_use.travel.coords");
                        // world, x, y, z specified
                        World w;
                        if (args[0].contains("~")) {
                            // relative location
                            w = player.getLocation().getWorld();
                        } else {
                            w = plugin.getServer().getWorld(args[0]);
                            if (w == null) {
                                player.sendMessage(plugin.getPluginName() + "World does not exist!");
                                return true;
                            }
                        }
                        double x;
                        double y;
                        double z;
                        try {
                            x = Double.parseDouble(args[1]);
                            y = Double.parseDouble(args[2]);
                            z = Double.parseDouble(args[3]);
                        } catch (NumberFormatException e) {
                            player.sendMessage(plugin.getPluginName() + "Could not parse coordinates!");
                            return true;
                        }
                        l = new Location(w, x, y, z);
                        // check block has space for player
                        if (!l.getBlock().getType().equals(Material.AIR)) {
                            player.sendMessage(plugin.getPluginName() + "Destination block is not AIR! Adjusting...");
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
                if (!TVMUtils.checkTachyonLevel(player.getUniqueId().toString(), required)) {
                    player.sendMessage(plugin.getPluginName() + "You need at least " + required + " tachyons to travel!");
                    return true;
                }
                if (l != null) {
                    player.sendMessage(plugin.getPluginName() + "Standby for Vortex travel...");
                    while (!l.getChunk().isLoaded()) {
                        l.getChunk().load();
                    }
                    TVMUtils.movePlayer(player, l, player.getLocation().getWorld());
                    // remove tachyons
                    new TVMQueryFactory(plugin).alterTachyons(player.getUniqueId().toString(), -required);
                } else {
                    //close(player);
                    player.sendMessage(plugin.getPluginName() + "No location could be found within those parameters.");
                }
                // do stuff
                return true;
            } else {
                player.sendMessage(plugin.getPluginName() + "You don't have a Vortex Manipulator in your hand!");
                return true;
            }
        }
        return false;
    }
}
