package com.wrkout.storage;

import com.wrkout.activites.ActivityHandler;
import com.wrkout.activites.BaseActivity;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CsvHandler implements StorageHandler {

    public static final String delimiter = ";";
    public static final String fileName = "data.csv";
    /**
     * Maximum number of data points.
     */
    public static final int maxKeys = 32;

    private List<String> columnNames = new ArrayList<>();

    private static Boolean contains(String[] arr, String key) {
        return Arrays.asList(arr).contains(key);
    }

    private static int indexOf(String[] arr, String key) {
        return Arrays.asList(arr).indexOf(key);
    }

    public void addActivity(BaseActivity activity, int user_id) {

    }

    public String[] getColumnNames() {
        if (columnNames.size() == 0) {
            String[] data;

            try {
                BufferedReader csvReader = new BufferedReader(new FileReader(fileName));

                String row = csvReader.readLine();
                if (row != null) {
                    data = row.split(delimiter);
                    for (String name : data) {
                        columnNames.add(name);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return columnNames.toArray(String[]::new);
    }


    public String[][] getRows(int user_id) {
        String[] keys = getColumnNames();
        List<String[]> activityList = new ArrayList<>();
        String[] data;
        String row;
        boolean isFirst = true;

        try {
            BufferedReader csvReader = new BufferedReader(new FileReader(fileName));
            String className;
            int nameIndex = indexOf(keys, "name");
            BaseActivity instance;

            while ((row = csvReader.readLine()) != null) {
                if (isFirst) {
                    // skip first row
                    isFirst = false;
                    continue;
                }
                data = row.split(delimiter);
                className = data[nameIndex];
                instance = ActivityHandler.newActivity(className);
                if (instance != null) {
                    for (int i = 0; i < data.length; i++) {
                        instance.set(keys[i], data[i]);
                    }
                    activityList.add(instance.getArray(keys));
                }
            }
            csvReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return activityList.toArray(String[][]::new);
    }

    public void read(ArrayList<BaseActivity> activityList) {
        String[] keys = new String[maxKeys];
        String[] data;
        String newName;
        int currentKeyNum = 0;
        int index;

        try {
            BufferedReader csvReader = new BufferedReader(new FileReader(fileName));

            // Fetch first line which contains the keys
            String row = csvReader.readLine();
            if (row != null) {
                data = row.split(delimiter);
                for (String datum : data) {
                    index = 2;
                    newName = datum;
                    while (contains(keys, newName)) {
                        newName = datum + index;
                        index += 1;
                    }
                    keys[currentKeyNum] = newName;
                    currentKeyNum += 1;
                }
            }

            String className;
            int nameIndex = indexOf(keys, "name");
            BaseActivity instance;

            // fetch the rest
            while ((row = csvReader.readLine()) != null) {
                data = row.split(delimiter);
                className = data[nameIndex];
                instance = ActivityHandler.newActivity(className);
                if (instance != null) {
                    for (int i = 0; i < data.length; i++) {
                        instance.set(keys[i], data[i]);
                    }
                    activityList.add(instance);
                }
            }

            csvReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(ArrayList<BaseActivity> activityList) {
        boolean first = true;
        String[] keys = new String[maxKeys];
        int currentKeyNum = 0;
        String[][] table;
        int colIndex;

        Collections.sort(activityList, Collections.reverseOrder());

        try (Writer writer = new FileWriter(fileName)) {
            for (AtomicInteger i = new AtomicInteger(); i.get() < activityList.size(); i.getAndIncrement()) {
                Map<String, String> row = activityList.get(i.get()).getMap();
                for (String key : row.keySet()) {
                    if (!contains(keys, key)) {
                        keys[currentKeyNum] = key;
                        currentKeyNum += 1;
                        if (first) {
                            first = false;
                        } else {
                            writer.append(delimiter);
                        }
                        writer.append(key);
                    }
                }
            }
            writer.append("\n");

            table = new String[activityList.size()][currentKeyNum];

            for (int i=0; i < activityList.size() ;i++) {
                Map<String, String> row = activityList.get(i).getMap();
                for (Map.Entry<String, String> entry: row.entrySet()) {
                    colIndex = indexOf(keys, entry.getKey());
                    table[i][colIndex] = entry.getValue();
                }
            }

            for (String[] strings : table) {
                first = true;
                for (int j = 0; j < strings.length; j++) {
                    if (first) {
                        first = false;
                    } else {
                        writer.append(delimiter);
                    }
                    if (strings[j] == null) {
                        writer.append('-');
                    } else {
                        writer.append(strings[j]);
                    }
                }
                writer.append("\n");
            }
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }
}
