package me.eccentric_nz.tardisvortexmanipulator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.TardisAPI;
import me.eccentric_nz.tardisvortexmanipulator.command.TVMCommand;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMDatabase;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMMySQL;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMSQLite;
import me.eccentric_nz.tardisvortexmanipulator.gui.TVMGUIListener;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class TARDISVortexManipulator extends JavaPlugin {

    private String pluginName;
    private TardisAPI tardisapi;
    private TARDIS tardis;
    public static TARDISVortexManipulator plugin;
    private final TVMDatabase service = TVMDatabase.getInstance();
    private static final List<Location> blocks = new ArrayList<Location>();

    @Override
    public void onDisable() {
        // TODO: Place any custom disable code here.
    }

    @Override
    public void onEnable() {
        plugin = this;
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
        loadDatabase();
        pm.registerEvents(new TVMGUIListener(this), this);
        getCommand("vm").setExecutor(new TVMCommand(this));
        getServer().addRecipe(new TVMRecipe(this).makeRecipe());
    }

    public String getPluginName() {
        return pluginName;
    }

    public TardisAPI getTardisAPI() {
        return tardisapi;
    }

    /**
     * Sets up the database.
     */
    private void loadDatabase() {
        String dbtype = getConfig().getString("storage.database");
        try {
            if (dbtype.equals("sqlite")) {
                String path = getDataFolder() + File.separator + "TVM.db";
                service.setConnection(path);
                TVMSQLite sqlite = new TVMSQLite(this);
                sqlite.createTables();
            } else {
                service.setConnection();
                TVMMySQL mysql = new TVMMySQL(this);
                mysql.createTables();
            }
        } catch (Exception e) {
            getServer().getConsoleSender().sendMessage(pluginName + "Connection and Tables Error: " + e);
        }
    }

    public static List<Location> getBlocks() {
        return blocks;
    }

    /**
     * Outputs a message to the console. Requires debug: true in config.yml
     *
     * @param o the Object to print to the console
     */
    public void debug(Object o) {
        if (getConfig().getBoolean("debug") == true) {
            getServer().getConsoleSender().sendMessage(pluginName + "Debug: " + o);
        }
    }
}
