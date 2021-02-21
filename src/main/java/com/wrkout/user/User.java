package com.wrkout.user;

public class User {
    private int id;
    private String name;
    private int weight;
    private int length;

    public User(int id, String name, int weight, int length) {
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.length = length;
    }

    public User() {
        this(0, "", 0, 0);
    }

    public static String getLabel(String key) {
        switch (key) {
            case "id": return "ID";
            case "name": return "Namn";
            case "weight": return "Vikt";
            case "length": return "Längd";
            default:
                return "";
        }
    }

    public static String[] getLabels() {
        return getLabels(false);
    }

    public static String[] getLabels(boolean includeHidden) {
        String[] keys = getKeys(includeHidden);
        String[] labels = new String[keys.length];
        for (int i = 0; i < keys.length; i++) {
            labels[i] = getLabel(keys[i]);
        }
        return labels;
    }

    public static String[] getKeys() {
        return getKeys(false);
    }

    public static String[] getKeys(boolean includeHidden) {
        if (includeHidden) {
            return new String[]{"id", "name", "weight", "length"};
        } else {
            return new String[]{"name", "weight", "length"};
        }
    }

    public boolean set(String key, String value) {
        switch (key) {
            case "id":
                this.id = Integer.parseInt(value);
                return true;
            case "name":
                this.name = value;
                return true;
            case "weight":
                this.weight = Integer.parseInt(value);
                return true;
            case "length":
                this.length = Integer.parseInt(value);
                return true;
            default:
                return false;
        }
    }

    public String get(String key) {
        switch (key) {
            case "id":
                return String.valueOf(id);
            case "name":
                return name;
            case "weight":
                return String.valueOf(weight);
            case "length":
                return String.valueOf(length);
            default:
                return null;
        }
    }

    public static String getCreateSQL() {
        StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS users (");
        for (String key : getKeys()) {
            sql.append(String.format("%s CHAR(32), ", key));
        }
        sql.append("id INTEGER PRIMARY KEY AUTOINCREMENT);");

        return sql.toString();
    }

    public static String getFirstUserSQL() {
        return "INSERT INTO users (name, weight) VALUES ('Dan', '76');";
    }

    public int getId() {
        return id;
    }

    public int getWeight() {
        return weight;
    }

    public String getName() {
        return name;
    }

    public int getLength() {
        return length;
    }
}
