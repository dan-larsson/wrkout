package com.wrkout.activites;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public abstract class BaseActivity implements Comparable {

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
            case "kcal":
                return String.valueOf(getKCAL());
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
                if (value.equals("")) {
                    this.time = 0;
                } else {
                    this.time = Integer.parseInt(value);
                }
                return true;

            case "id":
                if (value.equals("")) {
                    this.id = 0;
                } else {
                    this.id = Integer.parseInt(value);
                }
                return true;

            case "username":
                this.username = value;
                return true;

            case "userweight":
                if (value.equals("")) {
                    this.userweight = 0;
                } else {
                    this.userweight = Integer.parseInt(value);
                }
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

    /**
     * Calculate the number of calories used for this activity.
     *
     * Formula:
     * Power consumption (kcal/h per kg body weight) * h * body weight.
     *
     * @return Number of calories used for current activity.
     */
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
