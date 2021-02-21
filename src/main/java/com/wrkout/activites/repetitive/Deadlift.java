package com.wrkout.activites.repetitive;

import java.util.Date;

public class Deadlift extends RepetitiveActivity {

    public static final String NAME = "Marklyft";

    public Deadlift(int reps, int sets, int weight, Date date, int time) {
        super(Deadlift.NAME, date, reps, sets, weight, time);
    }

    public Deadlift(int reps, int sets, int weight, int time) {
        super(Deadlift.NAME, new Date(), reps, sets, weight, time);
    }

    public Deadlift() {
        super(Deadlift.NAME, new Date());
    }
 }