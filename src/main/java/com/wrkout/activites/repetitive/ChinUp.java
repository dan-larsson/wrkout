package com.wrkout.activites.repetitive;

import java.util.Date;

public class ChinUp extends RepetitiveActivity {

    public static final String NAME = "Chins";

    public ChinUp(int reps, int sets, int weight, Date date) {
        super(ChinUp.NAME, date, reps, sets, weight);
    }

    public ChinUp(int reps, int sets, int weight) {
        super(ChinUp.NAME, new Date(), reps, sets, weight);
    }

    public ChinUp() {
        super(ChinUp.NAME, new Date());
    }
 }