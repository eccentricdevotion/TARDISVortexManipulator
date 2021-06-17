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
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author eccentric_nz
 */
public class TvmDatabase {

    private static final TvmDatabase INSTANCE = new TvmDatabase();
    public Connection connection = null;
    public Statement statement = null;
    private boolean isMySql;

    public static synchronized TvmDatabase getInstance() {
        return INSTANCE;
    }

    public void setIsMySql(boolean isMySql) {
        this.isMySql = isMySql;
    }

    public void setConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Cannot find the driver in the classpath!", e);
        }
        String jdbc = "jdbc:mysql://" + TardisVortexManipulatorPlugin.plugin.getConfig().getString("storage.mysql.host") + ":" + TardisVortexManipulatorPlugin.plugin.getConfig().getString("storage.mysql.port") + "/" + TardisVortexManipulatorPlugin.plugin.getConfig().getString("storage.mysql.database") + "?autoReconnect=true";
        if (!TardisVortexManipulatorPlugin.plugin.getConfig().getBoolean("storage.mysql.useSSL")) {
            jdbc += "&useSSL=false";
        }
        String username = TardisVortexManipulatorPlugin.plugin.getConfig().getString("storage.mysql.user");
        String password = TardisVortexManipulatorPlugin.plugin.getConfig().getString("storage.mysql.password");
        try {
            connection = DriverManager.getConnection(jdbc, username, password);
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new RuntimeException("Cannot connect the database!", e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(String path) throws Exception {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        connection.setAutoCommit(true);
    }

    /**
     * @return an exception
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Clone is not allowed.");
    }

    /**
     * Test the database connection
     */
    public void testConnection(Connection connection) {
        if (isMySql) {
            try {
                statement = connection.createStatement();
                statement.executeQuery("SELECT 1");
            } catch (SQLException e) {
                try {
                    setConnection();
                } catch (Exception ex) {
                    TardisVortexManipulatorPlugin.plugin.debug("Could not re-connect to database!");
                }
            }
        }
    }
}
