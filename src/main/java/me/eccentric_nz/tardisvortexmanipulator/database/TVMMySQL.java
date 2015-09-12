/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import me.eccentric_nz.tardisvortexmanipulator.TARDISVortexManipulator;

/**
 *
 * @author eccentric_nz
 */
public class TVMMySQL {

    private final TVMDatabase service = TVMDatabase.getInstance();
    private final Connection connection = service.getConnection();
    private Statement statement = null;
    private final TARDISVortexManipulator plugin;

    public TVMMySQL(TARDISVortexManipulator plugin) {
        this.plugin = plugin;
    }

    /**
     * Creates the TARDISVortexManipulator default tables in the database.
     */
    public void createTables() {
        service.setIsMySQL(true);
        try {
            service.testConnection(connection);
            statement = connection.createStatement();

            // Table structure for table 'saves'
            String querySaves = "CREATE TABLE IF NOT EXISTS saves (save_id int(11) NOT NULL AUTO_INCREMENT, uuid varchar(48) DEFAULT '', save_name varchar(64) DEFAULT '', world varchar(64) DEFAULT '', x float DEFAULT '0', y float DEFAULT '0', z float DEFAULT '0', yaw float DEFAULT '0', pitch float DEFAULT '0', PRIMARY KEY (save_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;";
            statement.executeUpdate(querySaves);

            // Table structure for table 'saves'
            String queryMessages = "CREATE TABLE IF NOT EXISTS messages (message_id int(11) NOT NULL AUTO_INCREMENT, uuid_to varchar(48) DEFAULT '', uuid_from varchar(48) DEFAULT '', message text NULL, date bigint(20), `read` int(1) DEFAULT '0', PRIMARY KEY (message_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;";
            statement.executeUpdate(queryMessages);

            // Table structure for table 'beacon'
            String queryBeacons = "CREATE TABLE IF NOT EXISTS beacons (beacon_id int(11) NOT NULL AUTO_INCREMENT, uuid varchar(48) DEFAULT '', location varchar(512) DEFAULT '', block_type varchar(32) DEFAULT '', data int(2) DEFAULT '0', PRIMARY KEY (beacon_id)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;";
            statement.executeUpdate(queryBeacons);

            // Table structure for table 'manipulator'
            String queryManipulator = "CREATE TABLE IF NOT EXISTS manipulator (uuid varchar(48) NOT NULL, tachyon_level int(11) DEFAULT '0', PRIMARY KEY (uuid)) DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;";
            statement.executeUpdate(queryManipulator);

        } catch (SQLException e) {
            plugin.getServer().getConsoleSender().sendMessage(plugin.getPluginName() + "MySQL create table error: " + e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.getServer().getConsoleSender().sendMessage(plugin.getPluginName() + "MySQL close statement error: " + e);
            }
        }
    }
}
