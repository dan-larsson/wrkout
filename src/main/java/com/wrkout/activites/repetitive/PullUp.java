package com.wrkout.activites.repetitive;

import java.util.Date;

public class PullUp extends RepetitiveActivity {

    public static final String NAME = "Pullups";

    public PullUp(int reps, int sets, int weight, Date date) {
        super(PullUp.NAME, date, reps, sets, weight);
    }

    public PullUp(int reps, int sets, int weight) {
        super(PullUp.NAME, new Date(), reps, sets, weight);
    }

    public PullUp() {
        super(PullUp.NAME, new Date());
    }
}


