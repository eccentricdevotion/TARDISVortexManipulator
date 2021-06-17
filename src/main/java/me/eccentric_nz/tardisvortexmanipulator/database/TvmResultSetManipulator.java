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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TvmResultSetManipulator {

    private final TvmDatabase service = TvmDatabase.getInstance();
    private final Connection connection = service.getConnection();
    private final TardisVortexManipulatorPlugin plugin;
    private final String playerUuid;
    private final String prefix;
    private UUID uuid;
    private int tachyonLevel;

    public TvmResultSetManipulator(TardisVortexManipulatorPlugin plugin, String playerUuid) {
        this.plugin = plugin;
        this.playerUuid = playerUuid;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves an SQL ResultSet from the manipulator table. This method builds an SQL query string from the parameters
     * supplied and then executes the query. Use the getters to retrieve the results.
     *
     * @return true or false depending on whether any data matches the query
     */
    public boolean resultSet() {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM " + prefix + "manipulator WHERE uuid = ?";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setString(1, playerUuid);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                uuid = UUID.fromString(resultSet.getString("uuid"));
                tachyonLevel = resultSet.getInt("tachyon_level");
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for manipulator table! " + e.getMessage());
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
                plugin.debug("Error closing manipulator table! " + e.getMessage());
            }
        }
        return true;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getTachyonLevel() {
        return tachyonLevel;
    }
}
