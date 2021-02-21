package com.wrkout.activites.repetitive;

import java.util.Date;

public class OverheadPress extends RepetitiveActivity {

    public static final String NAME = "Militärpress";

    public OverheadPress(int reps, int sets, int weight, Date date, int time) {
        super(OverheadPress.NAME, date, reps, sets, weight, time);
    }

    public OverheadPress(int reps, int sets, int weight, int time) {
        super(OverheadPress.NAME, new Date(), reps, sets, weight, time);
    }

    public OverheadPress() {
        super(OverheadPress.NAME, new Date());
    }
}
