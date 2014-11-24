/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;
import me.eccentric_nz.tardisvortexmanipulator.TARDISVortexManipulator;
import me.eccentric_nz.tardisvortexmanipulator.message.TVMMessage;

/**
 *
 * @author eccentric_nz
 */
public class TVMResultSetInbox {

    private final TVMDatabase service = TVMDatabase.getInstance();
    private final Connection connection = service.getConnection();
    private final TARDISVortexManipulator plugin;
    private final String where;
    private final boolean read;
    private List<TVMMessage> mail;

    public TVMResultSetInbox(TARDISVortexManipulator plugin, String where, boolean read) {
        this.plugin = plugin;
        this.where = where;
        this.read = read;
    }

    /**
     * Retrieves an SQL ResultSet from the messages table. This method builds an
     * SQL query string from the parameters supplied and then executes the
     * query. Use the getters to retrieve the results.
     *
     * @return true or false depending on whether any data matches the query
     */
    public boolean resultSet() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT * FROM messages WHERE uuid_to = ? AND read = ? ORDER BY date DESC";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setString(1, where);
            statement.setInt(2, (read ? 1 : 0));
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    TVMMessage tvmm = new TVMMessage();
                    tvmm.setId(rs.getInt("message_id"));
                    tvmm.setWho(UUID.fromString(rs.getString("uuid_from")));
                    tvmm.setMessage(rs.getString("message"));
                    tvmm.setDate(getFormattedDate(rs.getLong("date")));
                    mail.add(tvmm);
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("Inbox error for messages table! " + e.getMessage());
            return false;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing messages table! " + e.getMessage());
            }
        }
        return true;
    }

    private String getFormattedDate(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat(plugin.getConfig().getString("date_format"));
        Date theDate = new Date(millis);
        return sdf.format(theDate);
    }

    public List<TVMMessage> getMail() {
        return mail;
    }
}