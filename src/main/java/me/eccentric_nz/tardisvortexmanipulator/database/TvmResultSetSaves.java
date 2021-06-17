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
import me.eccentric_nz.tardisvortexmanipulator.storage.TvmSave;
import org.bukkit.World;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TvmResultSetSaves {

    private final TvmDatabase service = TvmDatabase.getInstance();
    private final Connection connection = service.getConnection();
    private final TardisVortexManipulatorPlugin plugin;
    private final String uuid;
    private final int start, limit;
    private final List<TvmSave> saves = new ArrayList<>();
    private final String prefix;

    public TvmResultSetSaves(TardisVortexManipulatorPlugin plugin, String uuid, int start, int limit) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.start = start;
        this.limit = limit;
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
        String query = String.format("SELECT * FROM " + prefix + "saves WHERE uuid = ? ORDER BY save_name LIMIT %d, %d", start, start + limit);
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setString(1, uuid);
            resultSet = statement.executeQuery();
            if (resultSet.isBeforeFirst()) {
                while (resultSet.next()) {
                    TvmSave tvmSave = new TvmSave();
                    tvmSave.setId(resultSet.getInt("save_id"));
                    tvmSave.setName(resultSet.getString("save_name"));
                    String worldName = resultSet.getString("world");
                    tvmSave.setWorld(worldName);
                    tvmSave.setX(resultSet.getFloat("x"));
                    tvmSave.setY(resultSet.getFloat("y"));
                    tvmSave.setZ(resultSet.getFloat("z"));
                    World world = plugin.getServer().getWorld(worldName);
                    tvmSave.setEnv((world != null) ? world.getEnvironment().toString() : "NORMAL");
                    saves.add(tvmSave);
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for saves table! " + e.getMessage());
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
                plugin.debug("Error closing saves table! " + e.getMessage());
            }
        }
        return true;
    }

    public List<TvmSave> getSaves() {
        return saves;
    }
}
