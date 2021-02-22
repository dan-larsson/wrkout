package com.wrkout.user;

import com.wrkout.SQLite;

import java.sql.*;
import java.util.ArrayList;

public class UserHandler extends SQLite {

    private ArrayList<User> userList;

    public UserHandler() {
        connect();
    }

    public void deleteUser(int userId) throws IllegalStateException, SQLException {
        if (!connectedToDatabase)
            throw new IllegalStateException("Not Connected to Database");

        String sql = String.format("DELETE FROM users WHERE id = %d", userId);
        statement.executeUpdate(sql);
    }

    public void addUser(User user) throws IllegalStateException, SQLException {
        if (!connectedToDatabase)
            throw new IllegalStateException("Not Connected to Database");

        boolean isFirst = true;
        StringBuilder sqlKeys = new StringBuilder("INSERT INTO users (");
        StringBuilder sqlVals = new StringBuilder(") VALUES (");
        StringBuilder sql = new StringBuilder();

        String[] keys = User.getKeys();

        for (String key : keys) {
            if (isFirst) {
                sqlKeys.append(key);
                sqlVals.append(String.format("'%s'", user.get(key)));
                isFirst = false;
            } else {
                sqlKeys.append(String.format(",%s", key));
                sqlVals.append(String.format(",'%s'", user.get(key)));
            }
        }

        sql.append(sqlKeys.toString());
        sql.append(sqlVals.toString());
        sql.append(");");

        statement.executeUpdate(sql.toString());
    }

    public ArrayList<User> getUsers() throws IllegalStateException, SQLException {
        if (!connectedToDatabase)
            throw new IllegalStateException("Not Connected to Database");

        userList = new ArrayList<>();

        User instance;
        boolean first = true;
        StringBuilder sql = new StringBuilder("SELECT ");
        String[] keys = User.getKeys(true);

        for (String key : keys) {
            if (first) {
                sql.append(String.format("users.%s", key));
                first = false;
            } else {
                sql.append(String.format(",users.%s", key));
            }
        }
        sql.append(" FROM users");
        sql.append(" ORDER BY users.name;");

        ResultSet resultSet = statement.executeQuery(sql.toString());

        while (resultSet.next()) {
            instance = new User();
            for (int i = 0; i < keys.length; i++) {
                String value = resultSet.getString(i+1);
                String key = keys[i];
                if (value != null) {
                    instance.set(key, value);
                }
            }
            userList.add(instance);
        }
        return userList;
    }

    public static String getCreateSQL() {
        StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS users (");
        for (String key : User.getKeys(true)) {
            sql.append(String.format("%s CHAR(32), ", key));
        }
        sql.append("id INTEGER PRIMARY KEY AUTOINCREMENT);");

        return sql.toString();
    }

    public static String getFirstUserSQL() {
        return "INSERT INTO users (id, name, weight) VALUES (1, 'Dan', '76');";
    }


    public void createTables() throws IllegalStateException {
        if (!connectedToDatabase)
            throw new IllegalStateException("Not Connected to Database");

        try {
            statement.executeUpdate(getCreateSQL());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        try {
            statement.executeUpdate(getFirstUserSQL());
        } catch (Exception e) {
            // pass
        }
    }

}
