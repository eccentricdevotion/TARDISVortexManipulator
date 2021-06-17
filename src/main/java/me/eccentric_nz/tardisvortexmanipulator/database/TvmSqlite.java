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
package me.eccentric_nz.tardisvortexmanipulator.database;

import me.eccentric_nz.tardisvortexmanipulator.TardisVortexManipulatorPlugin;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author eccentric_nz
 */
public class TvmSqlite {

    private final TvmDatabase service = TvmDatabase.getInstance();
    private final Connection connection = service.getConnection();
    private final TardisVortexManipulatorPlugin plugin;
    private Statement statement = null;

    public TvmSqlite(TardisVortexManipulatorPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Creates the TARDISVortexManipulator default tables in the database.
     */
    public void createTables() {
        service.setIsMySql(false);
        try {
            statement = connection.createStatement();

            // Table structure for table 'saves'
            String querySaves = "CREATE TABLE IF NOT EXISTS saves (save_id INTEGER PRIMARY KEY NOT NULL, uuid TEXT DEFAULT '', save_name TEXT COLLATE NOCASE DEFAULT '', world TEXT COLLATE NOCASE DEFAULT '', x REAL DEFAULT 0.0, y REAL DEFAULT 0.0, z REAL DEFAULT 0.0, yaw REAL DEFAULT 0.0, pitch REAL DEFAULT 0.0)";
            statement.executeUpdate(querySaves);

            // Table structure for table 'saves'
            String queryMessages = "CREATE TABLE IF NOT EXISTS messages (message_id INTEGER PRIMARY KEY NOT NULL, uuid_to TEXT DEFAULT '', uuid_from TEXT DEFAULT '', message TEXT DEFAULT '', date INTEGER DEFAULT (strftime('%s', 'now')), read INTEGER DEFAULT 0)";
            statement.executeUpdate(queryMessages);

            //  Table structure for table 'beacon'
            String queryBeacons = "CREATE TABLE IF NOT EXISTS beacons (beacon_id INTEGER PRIMARY KEY NOT NULL, uuid TEXT DEFAULT '', location TEXT DEFAULT '', block_type TEXT DEFAULT '', data INTEGER DEFAULT 0)";
            statement.executeUpdate(queryBeacons);

            //  Table structure for table 'manipulator'
            String queryManipulator = "CREATE TABLE IF NOT EXISTS manipulator (uuid TEXT PRIMARY KEY NOT NULL, tachyon_level INTEGER DEFAULT 0)";
            statement.executeUpdate(queryManipulator);
        } catch (SQLException e) {
            plugin.getServer().getConsoleSender().sendMessage(plugin.getPluginName() + "SQLite create table error: " + e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.getServer().getConsoleSender().sendMessage(plugin.getPluginName() + "SQLite close statement error: " + e);
            }
        }
    }
}
