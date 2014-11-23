package me.eccentric_nz.tardisvortexmanipulator;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.TardisAPI;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class TARDISVortexManipulator extends JavaPlugin {

    private String pluginName;
    private TardisAPI tardisapi;
    private TARDIS tardis;

    @Override
    public void onDisable() {
        // TODO: Place any custom disable code here.
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        PluginManager pm = getServer().getPluginManager();
        /* Get TARDIS */
        Plugin p = pm.getPlugin("TARDIS");
        if (p == null) {
            System.err.println("Cannot find TARDIS!");
            return;
        }
        tardis = (TARDIS) p;
        tardisapi = tardis.getTardisAPI();
        PluginDescriptionFile pdfFile = getDescription();
        pluginName = ChatColor.GOLD + "[" + pdfFile.getName() + "]" + ChatColor.RESET + " ";
        pm.registerEvents(new TARDISVortexManipulatorGUIListener(this), this);
        getCommand("vm").setExecutor(new TARDISVortexManipulatorCommand(this));
        getServer().addRecipe(new TARDISVortexManipulatorRecipe(this).makeRecipe());
    }

    public String getPluginName() {
        return pluginName;
    }

    public TardisAPI getTardisAPI() {
        return tardisapi;
    }
}
