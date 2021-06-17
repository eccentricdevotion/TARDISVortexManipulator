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
public class TvmMySql {

    private final TvmDatabase service = TvmDatabase.getInstance();
    private final Connection connection = service.getConnection();
    private final TardisVortexManipulatorPlugin plugin;
    private Statement statement = null;

    public TvmMySql(TardisVortexManipulatorPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Creates the TARDISVortexManipulator default tables in the database.
     */
    public void createTables() {
        service.setIsMySql(true);
        try {
            service.testConnection(connection);
            statement = connection.createStatement();

            for (String query : Sql.CREATES) {
                String subbed = String.format(query, plugin.getConfig().getString("storage.mysql.prefix"));
                statement.executeUpdate(subbed);
            }
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
