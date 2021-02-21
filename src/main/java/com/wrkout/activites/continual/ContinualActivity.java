package com.wrkout.activites.continual;

import com.wrkout.activites.BaseActivity;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class ContinualActivity extends BaseActivity implements Comparable {
    // Length in km
    protected int length;

    public ContinualActivity(String name, Date date, int length, int time) {
        super(name, date);
        this.length = length;
        this.time = time;
    }

    public ContinualActivity(String name, Date date) {
        this(name, date, 0, 0);
    }

    public int getLength() {
        return length;
    }

    public int getTime() {
        return time;
    }

    @Override
    protected Map<String, String> prepare() {
        Map<String, String> dict = new HashMap<>();

        dict.put("length", String.valueOf(length));
        dict.put("time", String.valueOf(time));

        return dict;
    }

    @Override
    public boolean set(String key, String value) {
        switch (key) {
            case "length":
                this.length = Integer.parseInt(value);
                return true;
            case "time":
                this.time = Integer.parseInt(value);
                return true;
            default:
                return super.set(key, value);
        }
    }

    @Override
    public String get(String key) {
        switch (key) {
            case "length":
                return String.valueOf(length);
            case "time":
                return String.valueOf(time);
            default:
                return super.get(key);
        }
    }

    @Override
    public String getLabel(String key) {
        switch (key) {
            case "length": return "Sträcka";
            case "time": return "Tid";
            default:
                return super.getLabel(key);
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ContinualActivity{");
        sb.append("date=").append(date);
        sb.append(", name='").append(name).append("'");
        sb.append(", length=").append(length);
        sb.append(", time=").append(time);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public String[] getKeys() {
        return getKeys(false);
    }

    @Override
    public String[] getKeys(boolean includeHidden) {
        return Stream.concat(
                Arrays.stream(super.getKeys(includeHidden)),
                Arrays.stream(new String[]{"length", "time"})
        ).toArray(String[]::new);
    }

    @Override
    public int compareTo(Object o) {
        return getDate().compareTo(((BaseActivity)o).getDate());
    }

    protected double getPowerConsumption() {
        return 13.5;
    }
}
