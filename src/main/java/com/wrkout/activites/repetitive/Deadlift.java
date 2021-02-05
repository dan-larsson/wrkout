package com.wrkout.activites.repetitive;

import java.util.Date;

public class Deadlift extends RepetitiveActivity {

    public static final String NAME = "Marklyft";

    public Deadlift(int reps, int sets, int weight, Date date) {
        super(Deadlift.NAME, date, reps, sets, weight);
    }

    public Deadlift(int reps, int sets, int weight) {
        super(Deadlift.NAME, new Date(), reps, sets, weight);
    }

    public Deadlift() {
        super(Deadlift.NAME, new Date());
    }
 }