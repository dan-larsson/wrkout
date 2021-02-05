package com.wrkout.activites.repetitive;

import java.util.Date;

public class BenchPress extends RepetitiveActivity {

    public static final String NAME = "Bänkpress";

    public BenchPress(int reps, int sets, int weight, Date date) {
        super(BenchPress.NAME, date, reps, sets, weight);
    }

    public BenchPress(int reps, int sets, int weight) {
        super(BenchPress.NAME, new Date(), reps, sets, weight);
    }

    public BenchPress() {
        super(BenchPress.NAME, new Date());
    }
 }
