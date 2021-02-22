package com.wrkout.activites;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public abstract class BaseActivity implements Comparable {

    public static final double xxx = 6.5;

    protected Date date;
    protected String name;
    protected int id;
    protected int time;
    // User data
    protected String username;
    protected int userweight;


    public static SimpleDateFormat dateFormat;

    public BaseActivity(String name, Date date) {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        this.date = date;
        this.name = name;
    }

    /**
     * Number of kcal/h and per kg body weight.
     * @return Power consumption
     */
    protected abstract double getPowerConsumption();

    /**
     * Return object as HashMap.
     * @return object as hashmap.
     */
    public Map<String, String> getMap() {
        Map<String, String> dict = prepare();

        dict.put("date", dateFormat.format(date));
        dict.put("name", String.valueOf(name));

        return dict;
    }

    public String[] getArray(String[] keys) {
        String[] obj = new String[keys.length];
        for (int i = 0; i < keys.length; i++) {
            obj[i] = this.get(keys[i]);
        }
        return obj;
    }

    public String get(String key) {
        switch (key) {
            case "date":
                return dateFormat.format(date);
            case "name":
                return name;
            case "time":
                return String.valueOf(time);
            case "id":
                return String.valueOf(id);
            case "username":
                return username;
            case "userweight":
                return String.valueOf(userweight);
            default:
                return null;
        }
    }

    public static String getDefaultValue(String key) {
        switch (key) {
            case "date":
                return dateFormat.format(new Date());
            default:
                return null;
        }
    }
    public boolean set(String key, String value) {
        System.out.printf("Setting %s: %s\n", key, value);
        switch (key) {
            case "date":
                try {
                    this.date = dateFormat.parse(value);
                } catch (ParseException e) {
                    this.date = new Date();
                }
                return true;
            case "name":
                this.name = value;
                return true;
            case "time":
                this.time = Integer.parseInt(value);
                return true;
            case "id":
                this.id = Integer.parseInt(value);
                return true;
            case "username":
                this.username = value;
                return true;
            case "userweight":
                this.userweight = Integer.parseInt(value);
                return true;
            default:
                return false;
        }
    }

    public String getLabel(String key) {
        switch (key) {
            case "date": return "Datum";
            case "name": return "Aktivitet";
            case "time": return "Tid";
            default: return null;
        }
    }

    public String[] getKeys() {
        return getKeys(false);
    }

    public String[] getKeys(boolean includeHidden) {
        if (includeHidden) {
            return new String[]{"date", "name", "id", "time", "username", "userweight"};
        } else {
            return new String[]{"date", "name", "time"};
        }
    }

    public String[] getVals() {
        return getArray(getKeys());
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Activity{");
        sb.append("date=").append(date);
        sb.append(", name='").append(name).append('\'');
        sb.append(", time=").append(time);
        sb.append('}');
        return sb.toString();
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public int getId() {
        return id;
    }

    public int getTime() {
        return time;
    }

    protected abstract Map<String, String> prepare();

    public int getKCAL() {
        try {
            double powerConsumption = getPowerConsumption();
            double hours = getTime() / 60.0;

            return (int)(powerConsumption * hours * userweight);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

}
