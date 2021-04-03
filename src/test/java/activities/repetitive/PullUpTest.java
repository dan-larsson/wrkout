package activities.repetitive;

import com.wrkout.activites.repetitive.PullUp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;



public class PullUpTest {

    private PullUp activity;
    private final int sets = 1;
    private final int reps = 2;
    private final int weight = 3;
    private final int time = 4;
    private final Date today = new Date();
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @BeforeEach
    void setUp() {
        activity = new PullUp(reps, sets, weight, today, time);
    }

    @Test
    @DisplayName("Ensure we can set string values with set function")
    void testSet() {
        assertEquals(sets, activity.getSets(), "Should return sets set in construct");
        assertEquals(reps, activity.getReps(), "Should return reps set in construct");
        assertEquals(weight, activity.getWeight(), "Should return weight set in construct");
        assertEquals(time, activity.getTime(), "Should return time set in construct");

        activity.set("sets", "10");
        assertEquals(10, activity.getSets(), "Should return updated number of sets");

        activity.set("reps", "20");
        assertEquals(20, activity.getReps(), "Should return updated number of reps");

        activity.set("weight", "30");
        assertEquals(30, activity.getWeight(), "Should return updated weight");

        activity.set("time", "30");
        assertEquals(40, activity.getTime(), "Should return updated time");
    }

    @Test
    @DisplayName("Ensure all fields have labels")
    void testGetLabel() {
        assertNotNull(activity.getLabel("name"), "Parent field name should have a label");
        assertNotNull(activity.getLabel("date"), "Parent field date should have a label");
        assertNotNull(activity.getLabel("sets"), "Sets should have a label");
        assertNotNull(activity.getLabel("reps"), "Reps should have a label");
        assertNotNull(activity.getLabel("weight"), "Weight should have a label");
        assertNotNull(activity.getLabel("time"), "Time should have a label");
        assertNull(activity.getLabel("foo"), "foo should not have a label");
    }
}