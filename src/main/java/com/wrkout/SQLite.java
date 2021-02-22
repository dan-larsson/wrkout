package com.wrkout;

import java.sql.*;

public class SQLite {

    public static final String URL = "jdbc:sqlite:wrkout.db";

    protected Connection connection;
    protected Statement statement;
    protected boolean connectedToDatabase = false;

    protected void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(URL);
            statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            connectedToDatabase = true;
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void disconnect() {
        if (!connectedToDatabase)
            return;

        try {
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectedToDatabase = false;
        }
    }
}
