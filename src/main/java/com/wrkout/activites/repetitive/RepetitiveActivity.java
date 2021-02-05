package com.wrkout.activites.repetitive;

import com.wrkout.activites.BaseActivity;

import java.io.BufferedReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class RepetitiveActivity extends BaseActivity implements Comparable {
    protected int sets;
    protected int reps;
    protected int weight;

    public RepetitiveActivity(String name, Date date, int reps, int sets, int weight) {
        super(name, date);
        this.reps = reps;
        this.sets = sets;
        this.weight = weight;
    }

    public RepetitiveActivity(String name, Date date) {
        this(name, date, 0, 0, 0);
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

    @Override
    public void set(String key, String value) {
        switch (key) {
            case "sets":
                this.sets = Integer.parseInt(value);
                break;
            case "reps":
                this.reps = Integer.parseInt(value);
                break;
            case "weight":
                this.weight = Integer.parseInt(value);
                break;
            default:
                super.set(key, value);
        }
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
    protected Map<String, String> prepare() {
        Map<String, String> dict = new HashMap<>();

        dict.put("sets", String.valueOf(sets));
        dict.put("reps", String.valueOf(reps));
        dict.put("weight", String.valueOf(weight));
        dict.put("total", String.valueOf(sets*reps*weight));

        return dict;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RepetitiveActivity{");
        sb.append("date=").append(date);
        sb.append(", name='").append(name).append("'");
        sb.append(", reps=").append(reps);
        sb.append(", sets=").append(sets);
        sb.append(", weight=").append(weight);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public void prompt(BufferedReader input, HashMap<String, String> defaults) {
        super.prompt(input, defaults);
        promptForWeight(input, defaults);
        promptForSets(input, defaults);
        promptForReps(input, defaults);
    }

    private void promptForSets(BufferedReader input, HashMap<String, String> defaults) {
        this.sets = promptForInt(input,getLabel("sets")+"[nr]", defaults.get("sets"));
    }

    private void promptForWeight(BufferedReader input, HashMap<String, String> defaults) {
        this.weight = promptForInt(input,getLabel("weight")+"[kg]", defaults.get("weight"));
    }

    private void promptForReps(BufferedReader input, HashMap<String, String> defaults) {
        this.reps = promptForInt(input,getLabel("reps")+"[nr]", defaults.get("reps"));
    }

    @Override
    public int compareTo(Object o) {
        return getDate().compareTo(((BaseActivity)o).getDate());
    }
}
