package activities.repetitive;

import com.wrkout.activites.repetitive.OverheadPress;
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



public class OverheadPressTest {

    private OverheadPress activity;
    private final int sets = 1;
    private final int reps = 2;
    private final int weight = 3;
    private final Date today = new Date();
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @BeforeEach
    void setUp() {
        activity = new OverheadPress(reps, sets, weight, today);
    }

    @Test
    @DisplayName("Ensure correct values are returned by prepare function")
    void testPrepare() {
        Map<String, String> data = activity.getMap();
        assertEquals("1", data.get("sets"), "Should return 1 set");
        assertEquals("2", data.get("reps"), "Should return 2 reps");
        assertEquals("3", data.get("weight"), "Should return 3 in weight");
    }

    @Test
    @DisplayName("Ensure we can set string values with set function")
    void testSet() {
        assertEquals(sets, activity.getSets(), "Should return sets set in construct");
        assertEquals(reps, activity.getReps(), "Should return reps set in construct");
        assertEquals(weight, activity.getWeight(), "Should return weight set in construct");

        activity.set("sets", "10");
        assertEquals(10, activity.getSets(), "Should return updated number of sets");

        activity.set("reps", "20");
        assertEquals(20, activity.getReps(), "Should return updated number of reps");

        activity.set("weight", "30");
        assertEquals(30, activity.getWeight(), "Should return updated weight");
    }

    @Test
    @DisplayName("Ensure all fields have labels")
    void testGetLabel() {
        assertNotNull(activity.getLabel("name"), "Parent field name should have a label");
        assertNotNull(activity.getLabel("date"), "Parent field date should have a label");
        assertNotNull(activity.getLabel("sets"), "Sets should have a label");
        assertNotNull(activity.getLabel("reps"), "Reps should have a label");
        assertNotNull(activity.getLabel("weight"), "Weight should have a label");
        assertNull(activity.getLabel("foo"), "foo should not have a label");
    }

    @Test
    @DisplayName("Ensure default values if no are given")
    void testPromptDefaults() {
        HashMap<String, String> defaults = new HashMap<>();
        defaults.put("date", "1977-06-08");
        defaults.put("sets", "111");
        defaults.put("reps", "222");
        defaults.put("weight", "333");

        BufferedReader bufferedReader = new BufferedReader(new StringReader("\n\n\n\n"));
        activity.prompt(bufferedReader, defaults);

        assertEquals("1977-06-08", dateFormat.format(activity.getDate()), "Should return default date");
        assertEquals(111, activity.getSets(), "Should return default number of sets");
        assertEquals(222, activity.getReps(), "Should return default number of reps");
        assertEquals(333, activity.getWeight(), "Should return default weight");
    }

    @Test
    @DisplayName("Ensure new values are set")
    void testPromptNewValues() {
        HashMap<String, String> defaults = new HashMap<>();
        defaults.put("date", "1977-06-08");
        defaults.put("sets", "111");
        defaults.put("reps", "222");
        defaults.put("weight", "333");

        BufferedReader bufferedReader = new BufferedReader(new StringReader("2000-01-01\n444\n555\n666\n"));
        activity.prompt(bufferedReader, defaults);

        assertEquals("2000-01-01", dateFormat.format(activity.getDate()), "Should return new date");
        assertEquals(666, activity.getReps(), "Should return updated number of reps");
        assertEquals(555, activity.getSets(), "Should return updated number of sets");
        assertEquals(444, activity.getWeight(), "Should return updated weight");
        assertEquals("2000-01-01", defaults.get("date"), "Default date should be updated");

    }
}