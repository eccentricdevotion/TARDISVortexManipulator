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
package me.eccentric_nz.tardisvortexmanipulator.command;

import me.eccentric_nz.tardisvortexmanipulator.TardisVortexManipulatorPlugin;
import me.eccentric_nz.tardisvortexmanipulator.TvmUtils;
import me.eccentric_nz.tardisvortexmanipulator.database.*;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

public class TvmCommandMessage implements CommandExecutor {

    private final TardisVortexManipulatorPlugin plugin;

    public TvmCommandMessage(TardisVortexManipulatorPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
        if (command.getName().equalsIgnoreCase("vmm")) {
            Player player = null;
            if (sender instanceof Player) {
                player = (Player) sender;
            }
            if (player == null) {
                sender.sendMessage(plugin.getPluginName() + "That command cannot be used from the console!");
                return true;
            }
            if (!player.hasPermission("vm.message")) {
                player.sendMessage(plugin.getPluginName() + "You don't have permission to use that command!");
                return true;
            }
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName() && itemStack.getItemMeta().getDisplayName().equals("Vortex Manipulator")) {
                if (args.length < 2) {
                    player.sendMessage(plugin.getPluginName() + "Incorrect command usage!");
                    return false;
                }
                String first = args[0].toLowerCase(); // TODO Figure out how to name these three "first"s differently.
                try {
                    FIRST f = FIRST.valueOf(first);
                    switch (f) {
                        case msg -> {
                            OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(args[1]);
                            String offlinePlayerUuid = offlinePlayer.getUniqueId().toString();
                            // check they have a Vortex Manipulator
                            TvmResultSetManipulator resultSetOfflinePlayer = new TvmResultSetManipulator(plugin, offlinePlayerUuid);
                            if (!resultSetOfflinePlayer.resultSet()) {
                                player.sendMessage(plugin.getPluginName() + args[1] + " does not have a Vortex Manipulator!");
                                return true;
                            }
                            StringBuilder stringBuilder = new StringBuilder();
                            for (int i = 2; i < args.length; i++) {
                                stringBuilder.append(args[i]).append(" ");
                            }
                            String message = stringBuilder.toString();
                            HashMap<String, Object> whereOfflinePlayer = new HashMap<>();
                            whereOfflinePlayer.put("uuid_to", offlinePlayerUuid);
                            whereOfflinePlayer.put("uuid_from", player.getUniqueId().toString());
                            whereOfflinePlayer.put("message", message);
                            whereOfflinePlayer.put("date", System.currentTimeMillis());
                            new TvmQueryFactory(plugin).doInsert("messages", whereOfflinePlayer);
                            player.sendMessage(plugin.getPluginName() + "Message sent.");
                        }
                        case list -> {
                            String uuid = player.getUniqueId().toString();
                            if (args.length == 2) {
                                if (args[1].equalsIgnoreCase("out")) {
                                    // list outbox
                                    TvmResultSetOutbox resultSetOutbox = new TvmResultSetOutbox(plugin, uuid, 0, 10);
                                    if (resultSetOutbox.resultSet()) {
                                        TvmUtils.sendOutboxList(player, resultSetOutbox, 1);
                                    } else {
                                        player.sendMessage(plugin.getPluginName() + "There are no messages in your outbox.");
                                        return true;
                                    }
                                } else {
                                    // list inbox
                                    TvmResultSetInbox resultSetInbox = new TvmResultSetInbox(plugin, uuid, 0, 10);
                                    if (resultSetInbox.resultSet()) {
                                        TvmUtils.sendInboxList(player, resultSetInbox, 1);
                                    } else {
                                        player.sendMessage(plugin.getPluginName() + "There are no messages in your inbox.");
                                        return true;
                                    }
                                }
                                break;
                            }
                            if (args.length < 3) {
                                player.sendMessage(plugin.getPluginName() + "You need to specify a page number!");
                                return true;
                            }
                            int page = parseNum(args[2]);
                            if (page == -1) {
                                player.sendMessage(plugin.getPluginName() + "Invalid page number!");
                                return true;
                            }
                            int start = (page * 10) - 10;
                            int limit = page * 10;
                            if (args[1].equalsIgnoreCase("out")) {
                                // outbox
                                TvmResultSetOutbox resultSetOutbox = new TvmResultSetOutbox(plugin, uuid, start, limit);
                                if (resultSetOutbox.resultSet()) {
                                    TvmUtils.sendOutboxList(player, resultSetOutbox, page);
                                } else {
                                    player.sendMessage(plugin.getPluginName() + "There are no messages in your outbox.");
                                    return true;
                                }
                            } else {
                                // inbox
                                TvmResultSetInbox resultSetInbox = new TvmResultSetInbox(plugin, uuid, start, limit);
                                if (resultSetInbox.resultSet()) {
                                    TvmUtils.sendInboxList(player, resultSetInbox, page);
                                } else {
                                    player.sendMessage(plugin.getPluginName() + "There are no messages in your inbox.");
                                    return true;
                                }
                            }
                        }
                        case read -> {
                            int readId = parseNum(args[1]);
                            if (readId != -1) {
                                TvmResultSetMessageById resultSetMessage = new TvmResultSetMessageById(plugin, readId);
                                if (resultSetMessage.resultSet()) {
                                    TvmUtils.readMessage(player, resultSetMessage.getMessage());
                                    // update read status
                                    new TvmQueryFactory(plugin).setReadStatus(readId);
                                } else {
                                    player.sendMessage(plugin.getPluginName() + "No message exists with that id, use /vmm list [in|out] first!");
                                    return true;
                                }
                            }
                        }
                        case delete -> {
                            int deleteId = parseNum(args[1]);
                            if (deleteId != -1) {
                                TvmResultSetMessageById resultSetMessage = new TvmResultSetMessageById(plugin, deleteId);
                                if (resultSetMessage.resultSet()) {
                                    HashMap<String, Object> where = new HashMap<>();
                                    where.put("message_id", deleteId);
                                    new TvmQueryFactory(plugin).doDelete("messages", where);
                                    player.sendMessage(plugin.getPluginName() + "Message deleted.");
                                }
                            } else {
                                player.sendMessage(plugin.getPluginName() + "No message exists with that id, use /vmm list [in|out] first!");
                                return true;
                            }
                        }
                        default -> {
                            // clear
                            if (!args[1].equalsIgnoreCase("in") && !args[1].equalsIgnoreCase("out")) {
                                player.sendMessage(plugin.getPluginName() + "You need to specify which mail box you want to clear (in or out)!");
                                return true;
                            }
                            TvmQueryFactory queryFactory = new TvmQueryFactory(plugin);
                            HashMap<String, Object> where = new HashMap<>();
                            String which = "Outbox";
                            if (args[1].equalsIgnoreCase("out")) {
                                where.put("uuid_from", player.getUniqueId().toString());
                            } else {
                                where.put("uuid_to", player.getUniqueId().toString());
                                which = "Inbox";
                            }
                            queryFactory.doDelete("messages", where);
                            player.sendMessage(plugin.getPluginName() + which + " cleared.");
                        }
                    }
                } catch (IllegalArgumentException e) {
                    player.sendMessage(plugin.getPluginName() + "Incorrect command usage!");
                    return false;
                }
                return true;
            } else {
                player.sendMessage(plugin.getPluginName() + "You don't have a Vortex Manipulator in your hand!");
                return true;
            }
        }
        return false;
    }

    private int parseNum(String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private enum FIRST {

        msg,
        list,
        read,
        delete,
        clear
    }
}
