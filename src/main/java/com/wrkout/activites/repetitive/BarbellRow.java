package com.wrkout.activites.repetitive;

import java.util.Date;

public class BarbellRow extends RepetitiveActivity {

    public static final String NAME = "Stångrodd";

    public BarbellRow(int reps, int sets, int weight, Date date, int time) {
        super(BarbellRow.NAME, date, reps, sets, weight, time);
    }

    public BarbellRow(int reps, int sets, int weight, int time) {
        super(BarbellRow.NAME, new Date(), reps, sets, weight, time);
    }

    public BarbellRow() {
        super(BarbellRow.NAME, new Date());
    }
}