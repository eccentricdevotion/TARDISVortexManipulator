package me.eccentric_nz.tardisvortexmanipulator;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class TARDISVortexManipulatorCommand implements CommandExecutor {

    private final TARDISVortexManipulator plugin;

    public TARDISVortexManipulatorCommand(TARDISVortexManipulator plugin) {
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
            if (args[0].equalsIgnoreCase("gui")) {
                // open gui
                ItemStack[] gui = new TARDISVortexManipulatorGUI(plugin).getGUI();
                Inventory vmg = plugin.getServer().createInventory(player, 54, "§4Vortex Manipulator");
                vmg.setContents(gui);
                player.openInventory(vmg);
            }
            // do stuff
            return true;
        }
        return false;
    }
}
