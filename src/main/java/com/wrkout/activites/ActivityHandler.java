package com.wrkout.activites;

import com.wrkout.storage.CsvHandler;
import com.wrkout.storage.StorageHandler;
import com.wrkout.activites.continual.Running;
import com.wrkout.activites.repetitive.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.Map;

public class ActivityHandler {

    private ArrayList<BaseActivity> activityList;
    private StorageHandler storageHandler;

    public ActivityHandler(StorageHandler storageHandler) {
        this.storageHandler = storageHandler;
        activityList = new ArrayList<>();
    }

    public ActivityHandler() {
        this(new CsvHandler());
    }

    /**
     * Appends the specified activity to the end of this list.
     * @param activity activity
     */
    public void add(BaseActivity activity) {
        activityList.add(activity);
    }

    /**
     * Returns the activity at the specified position in this list.
     * @param index position.
     * @return activity at the specified position.
     */
    public BaseActivity get(int index) {
        return activityList.get(index);
    }

    public void print() {
        String[] keys = new String[32];
        int[] maxLengths = new int[32];
        int currentKeyNum = 1;
        String[][] table;
        int colIndex;
        boolean first = true;
        String fmt;
        StringBuilder str = new StringBuilder();
        StringBuilder hr = new StringBuilder();
        Formatter fm = new Formatter(str);

        // Add first special column #
        keys[0] = "#";
        maxLengths[0] = 5;
        fmt = "| %-" + (maxLengths[0]+1) + "s";
        fm.format(fmt, keys[0]);
        str.append("|");

        // Construct the rest of the header
        for (int i=0; i < activityList.size(); i++) {
            BaseActivity inst = activityList.get(i);
            Map<String, String> row = inst.getMap();
            for (Map.Entry<String, String> entry : row.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (value.length() > 5) {
                    maxLengths[currentKeyNum] = 25;
                } else {
                    maxLengths[currentKeyNum] = 10;
                }
                if (!Arrays.asList(keys).contains(key)) {
                    keys[currentKeyNum] = key;
                    currentKeyNum += 1;
                    if (first) {
                        first = false;
                    } else {
                        str.append(" |");
                    }
                    fmt = " %-" + maxLengths[currentKeyNum-1] + "s";
                    fm.format(fmt, inst.getLabel(key));
                }
            }
        }
        str.append(" |\n");

        // construct the horizontal ruler
        for (int i = 0; i < currentKeyNum; i++) {
            hr.append("+");
            hr.append("-".repeat(maxLengths[i]+2));
        }
        hr.append("+\n");

        // print the header
        System.out.printf(hr.toString());
        System.out.printf(str.toString());
        System.out.printf(hr.toString());

        // put all values in the correct bucket
        table = new String[activityList.size()][currentKeyNum];
        for (int i=0; i < activityList.size() ;i++) {
            table[i][0] = String.valueOf(i+1);
            Map<String, String> row = activityList.get(i).getMap();
            for (Map.Entry<String, String> entry: row.entrySet()) {
                colIndex = Arrays.asList(keys).indexOf(entry.getKey());
                table[i][colIndex] = entry.getValue();
            }
        }

        // print the rows
        for (String[] strings : table) {
            first = true;
            for (int j = 0; j < strings.length; j++) {
                fmt = "| %-" + (maxLengths[j] + 1) + "s";
                if (strings[j] == null) {
                    System.out.printf(fmt, "-");
                } else {
                    System.out.printf(fmt, strings[j]);
                }
            }
            System.out.printf("|\n");
        }

        // finish of with a ruler
        System.out.printf(hr.toString());

    }

    /**
     * Read activities from storage.
     */
    public void read() {
        storageHandler.read(activityList);
    }

    /**
     * Write activities to storage.
     */
    public void write() {
        storageHandler.write(activityList);
    }

    /**
     * Return an array of activity names.
     * @return array of activity names.
     */
    public static String[] oneOfEach() {
        String[] all = new String[9];
        all[0] = BarbellCurl.NAME;
        all[1] = BarbellRow.NAME;
        all[2] = BarbellSquat.NAME;
        all[3] = BenchPress.NAME;
        all[4] = ChinUp.NAME;
        all[5] = Deadlift.NAME;
        all[6] = Running.NAME;
        all[7] = OverheadPress.NAME;
        all[8] = PullUp.NAME;

        return all;
    }

    /**
     * Return a new activity instance given its name.
     * @param type Activity type name
     * @return BaseActivity instance.
     */
    public static BaseActivity newActivity(String type) {
        switch (type) {
            case BarbellCurl.NAME: return new BarbellCurl();
            case BarbellRow.NAME: return new BarbellRow();
            case BarbellSquat.NAME: return new BarbellSquat();
            case BenchPress.NAME: return new BenchPress();
            case ChinUp.NAME: return new ChinUp();
            case Deadlift.NAME: return new Deadlift();
            case Running.NAME: return new Running();
            case OverheadPress.NAME: return new OverheadPress();
            case PullUp.NAME: return new PullUp();
            default:
                return null;
        }
    }

    public BaseActivity getSuggestion() {
        return null;
    }
}
