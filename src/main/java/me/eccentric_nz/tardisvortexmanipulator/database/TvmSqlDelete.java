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
import java.util.HashMap;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TvmSqlDelete implements Runnable {

    private final TardisVortexManipulatorPlugin plugin;
    private final TvmDatabase service = TvmDatabase.getInstance();
    private final Connection connection = service.getConnection();
    private final String table;
    private final HashMap<String, Object> where;
    private final String prefix;

    /**
     * Deletes rows from an SQLite database table. This method builds an SQL query string from the parameters supplied
     * and then executes the delete.
     *
     * @param plugin an instance of the main plugin class
     * @param table  the database table name to insert the data into.
     * @param where  a HashMap<String, Object> of table fields and values to select the records to delete.
     */
    public TvmSqlDelete(TardisVortexManipulatorPlugin plugin, String table, HashMap<String, Object> where) {
        this.plugin = plugin;
        this.table = table;
        this.where = where;
        prefix = this.plugin.getPrefix();
    }

    @Override
    public void run() {
        Statement statement = null;
        String values;
        StringBuilder stringBuilder = new StringBuilder();
        where.forEach((key, value) -> {
            stringBuilder.append(key).append(" = ");
            if (value.getClass().equals(String.class) || value.getClass().equals(UUID.class)) {
                stringBuilder.append("'").append(value).append("' AND ");
            } else {
                stringBuilder.append(value).append(" AND ");
            }
        });
        where.clear();
        values = stringBuilder.substring(0, stringBuilder.length() - 5);
        String query = "DELETE FROM " + prefix + table + " WHERE " + values;
        try {
            service.testConnection(connection);
            statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            plugin.debug("Delete error for " + table + "! " + e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing " + table + "! " + e.getMessage());
            }
        }
    }
}
