package com.wrkout.activites.repetitive;

import java.util.Date;

public class BarbellCurl extends RepetitiveActivity {

    public static final String NAME = "Stångcurl";

    public BarbellCurl(int reps, int sets, int weight, Date date, int time) {
        super(BarbellCurl.NAME, date, reps, sets, weight, time);
    }

    public BarbellCurl(int reps, int sets, int weight, int time) {
        super(BarbellCurl.NAME, new Date(), reps, sets, weight, time);
    }

    public BarbellCurl() {
        super(BarbellCurl.NAME, new Date());
    }
}