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
import me.eccentric_nz.tardisvortexmanipulator.storage.TvmMessage;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TvmResultSetMessageById {

    private final TvmDatabase service = TvmDatabase.getInstance();
    private final Connection connection = service.getConnection();
    private final TardisVortexManipulatorPlugin plugin;
    private final int id;
    private final String prefix;
    private TvmMessage message;

    public TvmResultSetMessageById(TardisVortexManipulatorPlugin plugin, int id) {
        this.plugin = plugin;
        this.id = id;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves an SQL ResultSet from the messages table. This method builds an SQL query string from the parameters
     * supplied and then executes the query. Use the getters to retrieve the results.
     *
     * @return true or false depending on whether any data matches the query
     */
    public boolean resultSet() {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM " + prefix + "messages WHERE message_id = ?";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.isBeforeFirst()) {
                resultSet.next();
                message = new TvmMessage();
                message.setId(resultSet.getInt("message_id"));
                message.setWho(UUID.fromString(resultSet.getString("uuid_from")));
                message.setMessage(resultSet.getString("message"));
                message.setDate(getFormattedDate(resultSet.getLong("date")));
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("Warp error for saves table! " + e.getMessage());
            return false;
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing saves table for warp! " + e.getMessage());
            }
        }
        return true;
    }

    public TvmMessage getMessage() {
        return message;
    }

    private String getFormattedDate(long milliseconds) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(plugin.getConfig().getString("date_format"));
        Date theDate = new Date(milliseconds);
        return simpleDateFormat.format(theDate);
    }
}
