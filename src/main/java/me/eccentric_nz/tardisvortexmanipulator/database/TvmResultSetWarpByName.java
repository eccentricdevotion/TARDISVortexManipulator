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
import org.bukkit.Location;
import org.bukkit.World;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author eccentric_nz
 */
public class TvmResultSetWarpByName {

    private final TvmDatabase service = TvmDatabase.getInstance();
    private final Connection connection = service.getConnection();
    private final TardisVortexManipulatorPlugin plugin;
    private final String uuid;
    private final String name;
    private final String prefix;
    private int id;
    private Location warp;

    public TvmResultSetWarpByName(TardisVortexManipulatorPlugin plugin, String uuid, String name) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.name = name;
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
        String query = "SELECT * FROM " + prefix + "saves WHERE uuid = ? AND save_name = ?";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setString(1, uuid);
            statement.setString(2, name);
            resultSet = statement.executeQuery();
            if (resultSet.isBeforeFirst()) {
                resultSet.next();
                id = resultSet.getInt("save_id");
                World world = plugin.getServer().getWorld(resultSet.getString("world"));
                float x = resultSet.getFloat("x");
                float y = resultSet.getFloat("y");
                float z = resultSet.getFloat("z");
                float yaw = resultSet.getFloat("yaw");
                float pitch = resultSet.getFloat("pitch");
                warp = new Location(world, x, y, z, yaw, pitch);
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

    public int getId() {
        return id;
    }

    public Location getWarp() {
        return warp;
    }
}
