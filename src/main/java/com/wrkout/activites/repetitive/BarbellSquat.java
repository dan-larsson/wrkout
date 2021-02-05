package com.wrkout.activites.repetitive;

import java.util.Date;

public class BarbellSquat extends RepetitiveActivity {

    public static final String NAME = "Knäböj";

    public BarbellSquat(int reps, int sets, int weight, Date date) {
        super(BarbellSquat.NAME, date, reps, sets, weight);
    }

    public BarbellSquat(int reps, int sets, int weight) {
        super(BarbellSquat.NAME, new Date(), reps, sets, weight);
    }

    public BarbellSquat() {
        super(BarbellSquat.NAME, new Date());
    }
}
