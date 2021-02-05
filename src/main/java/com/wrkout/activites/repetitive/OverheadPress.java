package com.wrkout.activites.repetitive;

import java.util.Date;

public class OverheadPress extends RepetitiveActivity {

    public static final String NAME = "Militärpress";

    public OverheadPress(int reps, int sets, int weight, Date date) {
        super(OverheadPress.NAME, date, reps, sets, weight);
    }

    public OverheadPress(int reps, int sets, int weight) {
        super(OverheadPress.NAME, new Date(), reps, sets, weight);
    }

    public OverheadPress() {
        super(OverheadPress.NAME, new Date());
    }
}
