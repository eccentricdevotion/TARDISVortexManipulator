package me.eccentric_nz.tardisvortexmanipulator.command;

import java.util.HashMap;
import me.eccentric_nz.tardisvortexmanipulator.TARDISVortexManipulator;
import me.eccentric_nz.tardisvortexmanipulator.TVMUtils;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMQueryFactory;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetInbox;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetManipulator;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetMessageById;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetOutbox;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TVMCommandMessage implements CommandExecutor {

    private final TARDISVortexManipulator plugin;

    public TVMCommandMessage(TARDISVortexManipulator plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(final CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("vmm")) {
            Player p = null;
            if (sender instanceof Player) {
                p = (Player) sender;
            }
            if (p == null) {
                sender.sendMessage(plugin.getPluginName() + "That command cannot be used from the console!");
                return true;
            }
            if (!p.hasPermission("vm.message")) {
                p.sendMessage(plugin.getPluginName() + "You don't have permission to use that command!");
                return true;
            }
            ItemStack is = p.getItemInHand();
            if (is != null && is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().getDisplayName().equals("Vortex Manipulator")) {
                if (args.length < 2) {
                    p.sendMessage(plugin.getPluginName() + "Incorrect command usage!");
                    return false;
                }
                String first = args[0].toLowerCase();
                try {
                    FIRST f = FIRST.valueOf(first);
                    switch (f) {
                        case msg:
                            OfflinePlayer ofp = plugin.getServer().getOfflinePlayer(args[1]);
                            if (ofp == null) {
                                p.sendMessage(plugin.getPluginName() + "Could not find a player with that name!");
                                return true;
                            }
                            String ofp_uuid = ofp.getUniqueId().toString();
                            // check they have a Vortex Manipulator
                            TVMResultSetManipulator rsofp = new TVMResultSetManipulator(plugin, ofp_uuid);
                            if (!rsofp.resultSet()) {
                                p.sendMessage(plugin.getPluginName() + args[1] + " does not have a Vortex Manipulator!");
                                return true;
                            }
                            StringBuilder sb = new StringBuilder();
                            for (int i = 2; i < args.length; i++) {
                                sb.append(args[i]).append(" ");
                            }
                            String message = sb.toString();
                            HashMap<String, Object> whereofp = new HashMap<String, Object>();
                            whereofp.put("uuid_to", ofp_uuid);
                            whereofp.put("uuid_from", p.getUniqueId().toString());
                            whereofp.put("message", message);
                            whereofp.put("date", System.currentTimeMillis());
                            new TVMQueryFactory(plugin).doInsert("messages", whereofp);
                            p.sendMessage(plugin.getPluginName() + "Message sent.");
                            break;
                        case list:
                            String uuid = p.getUniqueId().toString();
                            if (args.length == 2) {
                                if (args[1].equalsIgnoreCase("out")) {
                                    // list outbox
                                    TVMResultSetOutbox rso = new TVMResultSetOutbox(plugin, uuid, 0, 10);
                                    if (rso.resultSet()) {
                                        TVMUtils.sendOutboxList(p, rso, 1);
                                    }
                                } else {
                                    // list inbox
                                    TVMResultSetInbox rsi = new TVMResultSetInbox(plugin, uuid, 0, 10);
                                    if (rsi.resultSet()) {
                                        TVMUtils.sendInboxList(p, rsi, 1);
                                    }
                                }
                            }
                            if (args.length < 3) {
                                p.sendMessage(plugin.getPluginName() + "You need to specify a page number!");
                                return true;
                            }
                            int page = parseNum(args[1]);
                            if (page == -1) {
                                p.sendMessage(plugin.getPluginName() + "Invalid page number!");
                                return true;
                            }
                            int start = (page * 10) - 10;
                            int limit = start * 10;
                            if (args[1].equalsIgnoreCase("out")) {
                                // outbox
                                TVMResultSetOutbox rso = new TVMResultSetOutbox(plugin, uuid, start, limit);
                                if (rso.resultSet()) {
                                    TVMUtils.sendOutboxList(p, rso, page);
                                }
                            } else {
                                // inbox
                                TVMResultSetInbox rsi = new TVMResultSetInbox(plugin, uuid, start, limit);
                                if (rsi.resultSet()) {
                                    TVMUtils.sendInboxList(p, rsi, page);
                                }
                            }
                            break;
                        case read:
                            int read_id = parseNum(args[1]);
                            if (read_id != -1) {
                                TVMResultSetMessageById rsm = new TVMResultSetMessageById(plugin, read_id);
                                if (rsm.resultSet()) {
                                    TVMUtils.readMessage(p, rsm.getMessage());
                                    // update read status
                                    new TVMQueryFactory(plugin).setReadStatus(read_id);
                                } else {
                                    p.sendMessage(plugin.getPluginName() + "No message exists with that id, use /vmm list [in|out] first!");
                                    return true;
                                }
                            }
                            break;
                        case delete:
                            int delete_id = parseNum(args[1]);
                            if (delete_id != -1) {
                                TVMResultSetMessageById rsm = new TVMResultSetMessageById(plugin, delete_id);
                                if (rsm.resultSet()) {
                                    HashMap<String, Object> where = new HashMap<String, Object>();
                                    where.put("message_id", delete_id);
                                    new TVMQueryFactory(plugin).doDelete("messages", where);
                                    p.sendMessage(plugin.getPluginName() + "Message deleted.");
                                }
                            } else {
                                p.sendMessage(plugin.getPluginName() + "No message exists with that id, use /vmm list [in|out] first!");
                                return true;
                            }
                            break;
                        default:
                            // clear
                            if (!args[1].toLowerCase().equals("in") && !args[1].toLowerCase().equals("out")) {
                                p.sendMessage(plugin.getPluginName() + "You need to specify which mail box you want to clear (in or out)!");
                                return true;
                            }
                            TVMQueryFactory qf = new TVMQueryFactory(plugin);
                            HashMap<String, Object> where = new HashMap<String, Object>();
                            if (args[1].equalsIgnoreCase("out")) {
                                where.put("uuid_from", p.getUniqueId().toString());
                            } else {
                                where.put("uuid_to", p.getUniqueId().toString());
                            }
                            qf.doDelete("messages", where);
                            break;
                    }
                } catch (IllegalArgumentException e) {
                    p.sendMessage(plugin.getPluginName() + "Incorrect command usage!");
                    return false;
                }
                return true;
            } else {
                p.sendMessage(plugin.getPluginName() + "You don't have a Vortex Manipulator in your hand!");
                return true;
            }
        }
        return false;
    }

    private int parseNum(String s) {
        try {
            int i = Integer.parseInt(s);
            return i;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private enum FIRST {

        msg, list, read, delete, clear;
    }
}
