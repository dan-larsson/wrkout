package com.wrkout.activites;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

public abstract class BaseActivity implements Comparable {

    protected Date date;
    protected String name;

    public static SimpleDateFormat dateFormat;

    public BaseActivity(String name, Date date) {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        this.date = date;
        this.name = name;
    }

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
            default:
                return false;
        }
    }

    public String getLabel(String key) {
        switch (key) {
            case "date": return "Datum";
            case "name": return "Aktivitet";
            default: return null;
        }
    }

    public String[] getKeys() {
        String[] keys = {"date", "name"};
        return keys;
    }

    public String[] getVals() {
        return getArray(getKeys());
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Activity{");
        sb.append("date=").append(date);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    /**
     * Prompt user for input values for this activity.
     * @param input BufferedReader
     */
    public void prompt(BufferedReader input, HashMap<String, String> defaults) {
        promptForActivityDate(input, defaults);
    }

    public static String getUserInput(BufferedReader input, String label, String defaultValue) {
        if (defaultValue != null) {
            System.out.printf("%s (%s): ", label, defaultValue);
        } else {
            System.out.printf("%s: ", label);
        }

        try {
            String userInput = input.readLine();
            if ("".equals(userInput.trim())) {
                return defaultValue;
            } else {
                return userInput;
            }
        } catch (IOException e) {
            return "";
        }

    }

    public static int promptForInt(BufferedReader input, String label, String defaultValue) {
        int tmp = 0;
        while (tmp == 0) {
            String userInput = getUserInput(input, label, defaultValue);
            try {
                tmp = Integer.parseInt(userInput);
            } catch (NumberFormatException e) {
                System.out.printf("Could not parse \"%s\", try again.\n", userInput);
            }
        }
        return tmp;
    }

    public static Date promptForDate(BufferedReader input, String label, String defaultValue) {
        Date tmp = null;
        while (tmp == null) {
            String userInput = getUserInput(input, label, defaultValue);
            try {
                tmp = dateFormat.parse(userInput);
            } catch (ParseException e) {
                System.out.printf("Could not parse \"%s\", try again.\n", userInput);
            }
        }
        return tmp;
    }

    protected abstract Map<String, String> prepare();

    private void promptForActivityDate(BufferedReader input, HashMap<String, String> defaults) {
        this.date = promptForDate(input, getLabel("date")+"[yyyy-mm-dd]", defaults.get("date"));
        defaults.put("date", dateFormat.format(this.date)); // suggest the same date
    }

}
