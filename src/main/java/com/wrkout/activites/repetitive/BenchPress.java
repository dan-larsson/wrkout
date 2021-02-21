package com.wrkout.activites.repetitive;

import java.util.Date;

public class BenchPress extends RepetitiveActivity {

    public static final String NAME = "Bänkpress";

    public BenchPress(int reps, int sets, int weight, Date date, int time) {
        super(BenchPress.NAME, date, reps, sets, weight, time);
    }

    public BenchPress(int reps, int sets, int weight, int time) {
        super(BenchPress.NAME, new Date(), reps, sets, weight, time);
    }

    public BenchPress() {
        super(BenchPress.NAME, new Date());
    }
 }
