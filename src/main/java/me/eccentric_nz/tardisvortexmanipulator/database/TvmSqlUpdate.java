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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TvmSqlUpdate implements Runnable {

    private final TardisVortexManipulatorPlugin plugin;
    private final TvmDatabase service = TvmDatabase.getInstance();
    private final String table;
    private final HashMap<String, Object> data;
    private final HashMap<String, Object> where;
    private final String prefix;
    Connection connection = service.getConnection();

    /**
     * Updates data in an SQLite database table. This method builds an SQL query string from the parameters supplied and
     * then executes the update.
     *
     * @param plugin an instance of the main plugin class
     * @param table  the database table name to update.
     * @param data   a HashMap<String, Object> of table fields and values update.
     * @param where  a HashMap<String, Object> of table fields and values to select the records to update.
     */
    public TvmSqlUpdate(TardisVortexManipulatorPlugin plugin, String table, HashMap<String, Object> data, HashMap<String, Object> where) {
        this.plugin = plugin;
        this.table = table;
        this.data = data;
        this.where = where;
        prefix = this.plugin.getPrefix();
    }

    @Override
    public void run() {
        PreparedStatement preparedStatement = null;
        String updates;
        String wheres;
        StringBuilder stringBuilderUpdates = new StringBuilder();
        StringBuilder stringBuilderWheres = new StringBuilder();
        data.forEach((key, value) -> stringBuilderUpdates.append(key).append(" = ?,"));
        where.forEach((key, value) -> {
            stringBuilderWheres.append(key).append(" = ");
            if (value.getClass().equals(String.class) || value.getClass().equals(UUID.class)) {
                stringBuilderWheres.append("'").append(value).append("' AND ");
            } else {
                stringBuilderWheres.append(value).append(" AND ");
            }
        });
        where.clear();
        updates = stringBuilderUpdates.substring(0, stringBuilderUpdates.length() - 1);
        wheres = stringBuilderWheres.substring(0, stringBuilderWheres.length() - 5);
        String query = "UPDATE " + prefix + table + " SET " + updates + " WHERE " + wheres;
        try {
            service.testConnection(connection);
            preparedStatement = connection.prepareStatement(query);
            int s = 1;
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                if (entry.getValue().getClass().equals(String.class) || entry.getValue().getClass().equals(UUID.class)) {
                    preparedStatement.setString(s, entry.getValue().toString());
                }
                if (entry.getValue() instanceof Integer) {
                    preparedStatement.setInt(s, (Integer) entry.getValue());
                }
                if (entry.getValue() instanceof Long) {
                    preparedStatement.setLong(s, (Long) entry.getValue());
                }
                s++;
            }
            data.clear();
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            plugin.debug("Update error for " + table + "! " + e.getMessage());
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing " + table + "! " + e.getMessage());
            }
        }
    }
}
