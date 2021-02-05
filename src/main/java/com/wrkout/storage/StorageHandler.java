package com.wrkout.storage;

import com.wrkout.activites.BaseActivity;

import java.util.ArrayList;

public interface StorageHandler {

    /**
     * Write data to storage.
     * @param activityList List of activities
     */
    public void write(ArrayList<BaseActivity> activityList);

    /**
     * Read data from storage.
     * @param activityList List of activities.
     */
    public void read(ArrayList<BaseActivity> activityList);

}
