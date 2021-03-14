package com.wrkout.activites;

import com.wrkout.activites.continual.ContinualActivity;
import com.wrkout.activites.repetitive.RepetitiveActivity;

import java.util.ArrayList;
import java.util.Date;

public class Summary {

    private Date date;
    private int time;
    private int total;
    private int kcal;
    private int length;
    private int num;

    private ArrayList<BaseActivity> activityList;

    public Summary(Date date) {
        this.date = date;
        activityList = new ArrayList<>();
    }

    public void add(BaseActivity activity) {
        activityList.add(activity);
        calculateFields();
    }

    public String get(String key) {
        switch (key) {
            case "date":
                return BaseActivity.dateFormat.format(date);
            case "time":
                return String.valueOf(time);
            case "total":
                return String.valueOf(total);
            case "length":
                return String.valueOf(length);
            case "kcal":
                return String.valueOf(kcal);
            case "num":
                return String.valueOf(num);
            default:
                return null;
        }
    }

    public static String getLabel(String key) {
        switch (key) {
            case "date":
                return "Datum";
            case "time":
                return "Tid (min)";
            case "total":
                return "Totalt (kg)";
            case "length":
                return "Sträcka (km)";
            case "kcal":
                return "Kalorier (kcal)";
            case "num":
                return "Antal aktiviteter";
            default:
                return null;
        }
    }

    public static String[] getLabels() {
        String[] keys = getKeys();
        String[] labels = new String[keys.length];

        for (int i = 0; i < keys.length; i++) {
            labels[i] = getLabel(keys[i]);
        }

        return labels;
    }

    public static String[] getKeys() {
        return new String[]{
                "date",
                "total",
                "time",
                "length",
                "kcal",
                "num"
        };
    }

    private void calculateFields() {
        time = 0;
        total = 0;
        kcal = 0;
        length = 0;
        num = 0;
        for (BaseActivity activity : activityList) {
            num += 1;
            time += activity.getTime();
            kcal += activity.getKCAL();
            if (activity instanceof RepetitiveActivity) {
                total += ((RepetitiveActivity)activity).getTotal();
            } else if (activity instanceof ContinualActivity) {
                length += ((ContinualActivity)activity).getLength();
            }
        }
    }

}
