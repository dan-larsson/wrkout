package com.wrkout.activites.repetitive;

import java.util.Date;

public class BarbellCurl extends RepetitiveActivity {

    public static final String NAME = "Stångcurl";

    public BarbellCurl(int reps, int sets, int weight, Date date) {
        super(BarbellCurl.NAME, date, reps, sets, weight);
    }

    public BarbellCurl(int reps, int sets, int weight) {
        super(BarbellCurl.NAME, new Date(), reps, sets, weight);
    }

    public BarbellCurl() {
        super(BarbellCurl.NAME, new Date());
    }
}