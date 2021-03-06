package com.wrkout.activites.repetitive;

import com.wrkout.activites.BaseActivity;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;


public class RepetitiveActivity extends BaseActivity implements Comparable {
    protected int sets;
    protected int reps;
    protected int weight;

    public RepetitiveActivity(String name, Date date, int reps, int sets, int weight, int time) {
        super(name, date);
        this.reps = reps;
        this.sets = sets;
        this.weight = weight;
        this.time = time;
    }

    public RepetitiveActivity(String name, Date date) {
        this(name, date, 0, 0, 0, 0);
    }

    public int getReps() {
        return reps;
    }

    public int getSets() {
        return sets;
    }

    public int getWeight() {
        return weight;
    }

    public int getTime() {
        return time;
    }

    @Override
    public String[] getKeys() {
        return getKeys(false);
    }

    @Override
    public String[] getKeys(boolean includeHidden) {
        return Stream.concat(
                Arrays.stream(super.getKeys(includeHidden)),
                Arrays.stream(new String[]{"sets", "reps", "weight", "time"})
        ).toArray(String[]::new);
    }

    @Override
    public boolean set(String key, String value) {
        switch (key) {
            case "sets":
                if (value.equals("")) {
                    this.sets = 0;
                } else {
                    this.sets = Integer.parseInt(value);
                }
                return true;
            case "reps":
                if (value.equals("")) {
                    this.reps = 0;
                } else {
                    this.reps = Integer.parseInt(value);
                }
                return true;
            case "weight":
                if (value.equals("")) {
                    this.weight = 0;
                } else {
                    this.weight = Integer.parseInt(value);
                }
                return true;
            default:
                return super.set(key, value);
        }
    }

    @Override
    public String get(String key) {
        switch (key) {
            case "sets":
                return String.valueOf(sets);
            case "reps":
                return String.valueOf(reps);
            case "weight":
                return String.valueOf(weight);
            case "total":
                return String.valueOf(getTotal());
            default:
                return super.get(key);
        }
    }

    public int getTotal() {
        return sets * reps * weight;
    }

    @Override
    public String getLabel(String key) {
        switch (key) {
            case "sets": return "Set";
            case "reps": return "Reps";
            case "weight": return "Vikt";
            case "total": return "Totalt";
            default:
                return super.getLabel(key);
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RepetitiveActivity{");
        sb.append("date=").append(date);
        sb.append(", name='").append(name).append("'");
        sb.append(", reps=").append(reps);
        sb.append(", sets=").append(sets);
        sb.append(", weight=").append(weight);
        sb.append(", time=").append(time);
        sb.append('}');
        return sb.toString();
    }

    public void blabla() {

    }

    @Override
    public int compareTo(Object o) {
        return getDate().compareTo(((BaseActivity)o).getDate());
    }

    protected double getPowerConsumption() {
        return 6.5;
    }
}
