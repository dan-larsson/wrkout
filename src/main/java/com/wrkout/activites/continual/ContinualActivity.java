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
    // Time in min
    protected int time;

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
        String[] keys = {"length", "time"};
        return Stream.concat(
                Arrays.stream(super.getKeys()),
                Arrays.stream(keys)
        ).toArray(String[]::new);
    }

    @Override
    public void prompt(BufferedReader input, HashMap<String, String> defaults) {
        super.prompt(input, defaults);
        promptForLength(input, defaults);
        promptForTime(input, defaults);
    }

    private void promptForLength(BufferedReader input, HashMap<String, String> defaults) {
        this.length = promptForInt(input,getLabel("length")+"[km]", defaults.get("length"));
    }

    private void promptForTime(BufferedReader input, HashMap<String, String> defaults) {
        this.time = promptForInt(input,getLabel("time")+"[min]", defaults.get("time"));
    }

    @Override
    public int compareTo(Object o) {
        return getDate().compareTo(((BaseActivity)o).getDate());
    }
}
