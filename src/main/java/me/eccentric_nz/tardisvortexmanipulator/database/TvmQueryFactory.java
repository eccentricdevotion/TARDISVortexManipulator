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
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Do basic SQL INSERT, UPDATE and DELETE queries.
 *
 * @author eccentric_nz
 */
public class TvmQueryFactory {

    private final TardisVortexManipulatorPlugin plugin;
    private final TvmDatabase service = TvmDatabase.getInstance();
    private final String prefix;
    Connection connection = service.getConnection();

    public TvmQueryFactory(TardisVortexManipulatorPlugin plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Inserts data into an SQLite database table. This method executes the SQL in a separate thread.
     *
     * @param table the database table name to insert the data into.
     * @param data  a HashMap<String, Object> of table fields and values to insert.
     */
    public void doInsert(String table, HashMap<String, Object> data) {
        TvmSqlInsert insert = new TvmSqlInsert(plugin, table, data);
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, insert);
    }

    /**
     * Inserts data into an SQLite database table. This method builds a prepared SQL statement from the parameters
     * supplied and then executes the insert.
     *
     * @param table the database table name to insert the data into.
     * @param data  a HashMap<String, Object> of table fields and values to insert.
     * @return the primary key of the record that was inserted
     */
    public int doSyncInsert(String table, HashMap<String, Object> data) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSetId = null;
        String fields;
        String questions;
        StringBuilder stringBuilderField = new StringBuilder();
        StringBuilder stringBuilderQuestions = new StringBuilder();
        data.forEach((key, value) -> {
            stringBuilderField.append(key).append(",");
            stringBuilderQuestions.append("?,");
        });
        fields = stringBuilderField.substring(0, stringBuilderField.length() - 1);
        questions = stringBuilderQuestions.substring(0, stringBuilderQuestions.length() - 1);
        try {
            service.testConnection(connection);
            preparedStatement = connection.prepareStatement("INSERT INTO " + prefix + table + " (" + fields + ") VALUES (" + questions + ")", PreparedStatement.RETURN_GENERATED_KEYS);
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
            resultSetId = preparedStatement.getGeneratedKeys();
            return (resultSetId.next()) ? resultSetId.getInt(1) : -1;
        } catch (SQLException e) {
            plugin.debug("Insert error for " + table + "! " + e.getMessage());
            return -1;
        } finally {
            try {
                if (resultSetId != null) {
                    resultSetId.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing " + table + "! " + e.getMessage());
            }
        }
    }

    /**
     * Updates data in an SQLite database table. This method executes the SQL in a separate thread.
     *
     * @param table the database table name to update.
     * @param data  a HashMap<String, Object> of table fields and values update.
     * @param where a HashMap<String, Object> of table fields and values to select the records to update.
     */
    public void doUpdate(String table, HashMap<String, Object> data, HashMap<String, Object> where) {
        TvmSqlUpdate update = new TvmSqlUpdate(plugin, table, data, where);
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, update);
    }

    /**
     * Deletes rows from an SQLite database table. This method executes the SQL in a separate thread.
     *
     * @param table the database table name to insert the data into.
     * @param where a HashMap<String, Object> of table fields and values to select the records to delete.
     */
    public void doDelete(String table, HashMap<String, Object> where) {
        TvmSqlDelete delete = new TvmSqlDelete(plugin, table, where);
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, delete);
    }

    /**
     * Deletes rows from an SQLite database table. This method executes the SQL in a separate thread.
     *
     * @param table the database table name to insert the data into.
     * @param where a HashMap<String, Object> of table fields and values to select the records to delete.
     * @return true or false depending on whether the data was deleted successfully
     */
    public boolean doSyncDelete(String table, HashMap<String, Object> where) {
        Statement statement = null;
        String values;
        StringBuilder stringBuilderWhere = new StringBuilder();
        where.forEach((key, value) -> {
            stringBuilderWhere.append(key).append(" = ");
            if (value.getClass().equals(String.class) || value.getClass().equals(UUID.class)) {
                stringBuilderWhere.append("'").append(value).append("' AND ");
            } else {
                stringBuilderWhere.append(value).append(" AND ");
            }
        });
        where.clear();
        values = stringBuilderWhere.substring(0, stringBuilderWhere.length() - 5);
        String query = "DELETE FROM " + prefix + table + " WHERE " + values;
        try {
            service.testConnection(connection);
            statement = connection.createStatement();
            return (statement.executeUpdate(query) > 0);
        } catch (SQLException e) {
            plugin.debug("Delete error for " + table + "! " + e.getMessage());
            return false;
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

    /**
     * Save a beacon block.
     *
     * @param uuid the uuid of the player who has set the beacon
     * @param block    the block to save
     */
    public void saveBeaconBlock(String uuid, Block block) {
        Location location = block.getLocation();
        plugin.getBlocks().add(location);
        String data = block.getBlockData().getAsString();
        HashMap<String, Object> set = new HashMap<>();
        set.put("uuid", uuid);
        set.put("location", location.toString());
        set.put("block_type", data);
        doSyncInsert("beacons", set);
    }

    /**
     * Alter tachyon levels. This method executes the SQL in a separate thread.
     *
     * @param uuid   the player's string UUID
     * @param amount the amount add tachyons to add or remove
     */
    public void alterTachyons(String uuid, int amount) {
        TvmAlterTachyon alter = new TvmAlterTachyon(plugin, amount, uuid);
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, alter);
    }

    /**
     * Update message read status. This method executes the SQL in a separate thread.
     *
     * @param id the message_id to alter
     */
    public void setReadStatus(int id) {
        TvmSetReadStatus set = new TvmSetReadStatus(plugin, id);
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, set);
    }
}
