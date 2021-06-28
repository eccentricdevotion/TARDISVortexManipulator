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

import me.eccentric_nz.tardis.utility.TardisNumberParsers;
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
public class TvmSqlInsert implements Runnable {

    private final TardisVortexManipulatorPlugin plugin;
    private final TvmDatabase service = TvmDatabase.getInstance();
    private final Connection connection = service.getConnection();
    private final String table;
    private final HashMap<String, Object> data;
    private final String prefix;

    /**
     * Inserts data into an SQLite database table. This method builds a prepared SQL statement from the parameters
     * supplied and then executes the insert.
     *
     * @param plugin an instance of the main plugin class
     * @param table  the database table name to insert the data into.
     * @param data   a {@link HashMap}{@code <}{@link String}{@code , }{@link Object}{@code >} of table fields and values to insert.
     */
    public TvmSqlInsert(TardisVortexManipulatorPlugin plugin, String table, HashMap<String, Object> data) {
        this.plugin = plugin;
        this.table = table;
        this.data = data;
        prefix = this.plugin.getPrefix();
    }

    @Override
    public void run() {
        PreparedStatement preparedStatement = null;
        String fields;
        String questions;
        StringBuilder stringBuilderFields = new StringBuilder();
        StringBuilder stringBuilderQuestions = new StringBuilder();
        data.forEach((key, value) -> {
            stringBuilderFields.append(key).append(",");
            stringBuilderQuestions.append("?,");
        });
        fields = stringBuilderFields.substring(0, stringBuilderFields.length() - 1);
        questions = stringBuilderQuestions.substring(0, stringBuilderQuestions.length() - 1);
        try {
            service.testConnection(connection);
            preparedStatement = connection.prepareStatement("INSERT INTO " + prefix + table + " (" + fields + ") VALUES (" + questions + ")");
            int i = 1;
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                if (entry.getValue().getClass().equals(String.class) || entry.getValue().getClass().equals(UUID.class)) {
                    preparedStatement.setString(i, entry.getValue().toString());
                } else if (entry.getValue().getClass().getName().contains("Double")) {
                    preparedStatement.setDouble(i, TardisNumberParsers.parseDouble(entry.getValue().toString()));
                } else if (entry.getValue().getClass().getName().contains("Float")) {
                    preparedStatement.setFloat(i, TardisNumberParsers.parseFloat(entry.getValue().toString()));
                } else if (entry.getValue().getClass().getName().contains("Long")) {
                    preparedStatement.setLong(i, TardisNumberParsers.parseLong(entry.getValue().toString()));
                } else {
                    preparedStatement.setInt(i, TardisNumberParsers.parseInt(entry.getValue().toString()));
                }
                i++;
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
