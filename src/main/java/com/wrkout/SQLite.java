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

    protected int executeUpdate(String sql) throws SQLException {
        if (!connectedToDatabase)
            throw new IllegalStateException("Not Connected to Database");

        System.out.println(sql);
        return statement.executeUpdate(sql);
    }

    protected ResultSet executeQuery(String sql) throws SQLException {
        if (!connectedToDatabase)
            throw new IllegalStateException("Not Connected to Database");

        System.out.println(sql);
        return statement.executeQuery(sql);
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
