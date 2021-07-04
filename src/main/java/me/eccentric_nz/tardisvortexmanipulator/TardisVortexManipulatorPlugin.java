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
package me.eccentric_nz.tardisvortexmanipulator;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.TardisAPI;
import me.eccentric_nz.tardisvortexmanipulator.command.*;
import me.eccentric_nz.tardisvortexmanipulator.database.TvmDatabase;
import me.eccentric_nz.tardisvortexmanipulator.database.TvmMySql;
import me.eccentric_nz.tardisvortexmanipulator.database.TvmSqlite;
import me.eccentric_nz.tardisvortexmanipulator.gui.TvmGuiListener;
import me.eccentric_nz.tardisvortexmanipulator.gui.TvmMessageGuiListener;
import me.eccentric_nz.tardisvortexmanipulator.gui.TvmSavesGuiListener;
import me.eccentric_nz.tardisvortexmanipulator.listeners.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TardisVortexManipulatorPlugin extends JavaPlugin {

    public static TardisVortexManipulatorPlugin plugin;
    private final TvmDatabase service = TvmDatabase.getInstance();
    private final List<Location> blocks = new ArrayList<>();
    private final List<UUID> beaconSetters = new ArrayList<>();
    private final List<UUID> travellers = new ArrayList<>();
    private String pluginName;
    private TardisAPI tardisApi;
    private TARDIS tardis;
    private PluginManager pluginManager;
    private String prefix;

    @Override
    public void onDisable() {
        // TODO Place any custom disable code here.
    }

    @Override
    public void onEnable() {
        plugin = this;
        PluginDescriptionFile pluginDescriptionFile = getDescription();
        pluginName = ChatColor.GOLD + "[" + pluginDescriptionFile.getName() + "]" + ChatColor.RESET + " ";
        saveDefaultConfig();
        new TvmConfig(this).checkConfig();
        pluginManager = getServer().getPluginManager();
        /*
         * Get TARDIS
         */
        Plugin tardis = pluginManager.getPlugin("TARDIS");
        if (tardis == null || !pluginManager.isPluginEnabled("TARDIS")) {
            System.err.println("[TARDISVortexManipulator] Cannot find TARDIS!");
            pluginManager.disablePlugin(this);
            return;
        }
        this.tardis = (TARDIS) tardis;
        tardisApi = this.tardis.getTardisAPI();
        prefix = getConfig().getString("storage.mysql.prefix");
        loadDatabase();
        registerListeners();
        registerCommands();
        getServer().addRecipe(new TvmRecipe(this).makeRecipe());
        startRecharger();
    }

    public String getPluginName() {
        return pluginName;
    }

    public TardisAPI getTardisApi() {
        return tardisApi;
    }

    public String getPrefix() {
        return prefix;
    }

    /**
     * Sets up the database.
     */
    private void loadDatabase() {
        String databaseType = getConfig().getString("storage.database");
        try {
            if (databaseType.equals("sqlite")) {
                String path = getDataFolder() + File.separator + "TVM.db";
                service.setConnection(path);
                TvmSqlite sqlite = new TvmSqlite(this);
                sqlite.createTables();
            } else {
                service.setConnection();
                TvmMySql mysql = new TvmMySql(this);
                mysql.createTables();
            }
        } catch (Exception e) {
            getServer().getConsoleSender().sendMessage(pluginName + "Connection and Tables Error: " + e);
        }
    }

    private void registerListeners() {
        pluginManager.registerEvents(new TvmBlockListener(this), this);
        pluginManager.registerEvents(new TvmCraftListener(this), this);
        pluginManager.registerEvents(new TvmDeathListener(this), this);
        pluginManager.registerEvents(new TvmEquipListener(this), this);
        pluginManager.registerEvents(new TvmGuiListener(this), this);
        pluginManager.registerEvents(new TvmMessageGuiListener(this), this);
        pluginManager.registerEvents(new TvmMoveListener(this), this);
        pluginManager.registerEvents(new TvmSavesGuiListener(this), this);
    }

    private void registerCommands() {
        getCommand("vm").setExecutor(new TvmCommand(this));
        getCommand("vma").setExecutor(new TvmCommandActivate(this));
        getCommand("vmb").setExecutor(new TvmCommandBeacon(this));
        getCommand("vmh").setExecutor(new TvmCommandHelp(this));
        getCommand("vmh").setTabCompleter(new TvmTabCompleteHelp());
        getCommand("vml").setExecutor(new TvmCommandLifesigns(this));
        getCommand("vmm").setExecutor(new TvmCommandMessage(this));
        getCommand("vmm").setTabCompleter(new TvmTabCompleteMessage());
        getCommand("vmr").setExecutor(new TvmCommandRemove(this));
        getCommand("vms").setExecutor(new TvmCommandSave(this));
        getCommand("vmg").setExecutor(new TvmCommandGive(this));
        getCommand("vmd").setExecutor(new TvmCommandConvert(this));
    }

    private void startRecharger() {
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new TvmTachyonRunnable(this), 1200L, getConfig().getLong("tachyon_use.recharge_interval"));
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
     * @param object the Object to print to the console
     */
    public void debug(Object object) {
        if (getConfig().getBoolean("debug")) {
            getServer().getConsoleSender().sendMessage(pluginName + "Debug: " + object);
        }
    }
}
