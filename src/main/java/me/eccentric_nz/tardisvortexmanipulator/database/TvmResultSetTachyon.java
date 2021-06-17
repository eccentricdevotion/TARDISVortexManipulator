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
import me.eccentric_nz.tardisvortexmanipulator.storage.TvmTachyon;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TvmResultSetTachyon {

    private final TvmDatabase service = TvmDatabase.getInstance();
    private final Connection connection = service.getConnection();
    private final TardisVortexManipulatorPlugin plugin;
    private final List<TvmTachyon> vortexManipulators = new ArrayList<>();
    private final String prefix;

    public TvmResultSetTachyon(TardisVortexManipulatorPlugin plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves an SQL ResultSet from the beacons table. This method builds an SQL query string from the parameters
     * supplied and then executes the query. Use the getters to retrieve the results.
     *
     * @return true or false depending on whether any data matches the query
     */
    public boolean resultSet() {
        Statement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM " + prefix + "manipulator";
        try {
            service.testConnection(connection);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            if (resultSet.isBeforeFirst()) {
                while (resultSet.next()) {
                    TvmTachyon tvmTachyon = new TvmTachyon(UUID.fromString(resultSet.getString("uuid")), resultSet.getInt("tachyon_level"));
                    vortexManipulators.add(tvmTachyon);
                }
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

    public List<TvmTachyon> getManipulators() {
        return vortexManipulators;
    }
}
