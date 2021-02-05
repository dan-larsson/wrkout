package com.wrkout.activites.continual;

import java.util.Date;

public class Running extends ContinualActivity {

    public static final String NAME = "Löpning";

    public Running(int length, int time, Date date) {
        super(Running.NAME, date, length, time);
    }

    public Running(int length, int time) {
        this(length, time, new Date());
    }

    public Running() {
        super(Running.NAME, new Date());
    }
}