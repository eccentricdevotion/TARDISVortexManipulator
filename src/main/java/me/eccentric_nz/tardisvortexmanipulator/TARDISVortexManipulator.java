package me.eccentric_nz.tardisvortexmanipulator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.TardisAPI;
import me.eccentric_nz.tardisvortexmanipulator.command.TVMCommand;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMDatabase;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMMySQL;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMSQLite;
import me.eccentric_nz.tardisvortexmanipulator.gui.TVMGUIListener;
import me.eccentric_nz.tardisvortexmanipulator.listeners.TVMBlockListener;
import me.eccentric_nz.tardisvortexmanipulator.listeners.TVMCraftListener;
import me.eccentric_nz.tardisvortexmanipulator.listeners.TVMDeathListener;
import me.eccentric_nz.tardisvortexmanipulator.listeners.TVMEquipListener;
import me.eccentric_nz.tardisvortexmanipulator.listeners.TVMMoveListener;
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
    private final List<Location> blocks = new ArrayList<Location>();
    private final List<UUID> beaconSetters = new ArrayList<UUID>();
    private final List<UUID> travellers = new ArrayList<UUID>();
    private PluginManager pm;

    @Override
    public void onDisable() {
        // TODO: Place any custom disable code here.
    }

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();
        pm = getServer().getPluginManager();
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
        registerListeners();
        getCommand("vm").setExecutor(new TVMCommand(this));
        getServer().addRecipe(new TVMRecipe(this).makeRecipe());
        startRecharger();
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

    private void registerListeners() {
        pm.registerEvents(new TVMGUIListener(this), this);
        pm.registerEvents(new TVMBlockListener(this), this);
        pm.registerEvents(new TVMMoveListener(this), this);
        pm.registerEvents(new TVMCraftListener(this), this);
        pm.registerEvents(new TVMEquipListener(this), this);
        pm.registerEvents(new TVMDeathListener(this), this);
    }

    private void startRecharger() {
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new TVMTachyonRunnable(this), 1200L, getConfig().getLong("tachyon_use.recharge_interval"));
    }

    public List<Location> getBlocks() {
        return blocks;
    }

    public List<UUID> getBeaconSetters() {
        return beaconSetters;
    }

    public List<UUID> getTravellers() {
        return travellers;
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
