package com.wrkout.activites.repetitive;

import java.util.Date;

public class ChinUp extends RepetitiveActivity {

    public static final String NAME = "Chins";

    public ChinUp(int reps, int sets, int weight, Date date, int time) {
        super(ChinUp.NAME, date, reps, sets, weight, time);
    }

    public ChinUp(int reps, int sets, int weight, int time) {
        super(ChinUp.NAME, new Date(), reps, sets, weight, time);
    }

    public ChinUp() {
        super(ChinUp.NAME, new Date());
    }
 }