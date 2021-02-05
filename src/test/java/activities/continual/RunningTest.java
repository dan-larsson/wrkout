package activities.continual;

import com.wrkout.activites.continual.Running;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


public class RunningTest {

    private Running activity;
    private int length = 10;
    private int time = 50;
    private Date today = new Date();
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @BeforeEach
    void setUp() {
        activity = new Running(length, time, today);
    }

    @Test
    @DisplayName("Ensure correct values are returned by prepare function")
    void testPrepare() {
        Map<String, String> data = activity.getMap();
        assertEquals("10", data.get("length"), "Should return 10 km");
        assertEquals("50", data.get("time"), "Should return 50 min");
    }

    @Test
    @DisplayName("Ensure we can set string values with set function")
    void testSet() {
        assertEquals(length, activity.getLength(), "Should return length set in construct");
        assertEquals(time, activity.getTime(), "Should return time set in construct");

        activity.set("length", "100");
        assertEquals(100, activity.getLength(), "Should return updated length");

        activity.set("time", "500");
        assertEquals(500, activity.getTime(), "Should return updated time");
    }

    @Test
    @DisplayName("Ensure all fields have labels")
    void testGetLabel() {
        assertNotNull(activity.getLabel("name"), "Parent field name should have a label");
        assertNotNull(activity.getLabel("date"), "Parent field date should have a label");
        assertNotNull(activity.getLabel("length"), "Length should have a label");
        assertNotNull(activity.getLabel("time"), "Time should have a label");
        assertNull(activity.getLabel("foo"), "foo should not have a label");
    }

    @Test
    @DisplayName("Ensure default values if no are given")
    void testPromptDefaults() {
        HashMap<String, String> defaults = new HashMap<String, String>();
        defaults.put("date", "1977-06-08");
        defaults.put("length", "33");
        defaults.put("time", "77");

        BufferedReader bufferedReader = new BufferedReader(new StringReader("\n\n\n"));
        activity.prompt(bufferedReader, defaults);

        assertEquals("1977-06-08", dateFormat.format(activity.getDate()), "Should return default date");
        assertEquals(33, activity.getLength(), "Should return default length");
        assertEquals(77, activity.getTime(), "Should return default time");

    }

    @Test
    @DisplayName("Ensure new values are set")
    void testPromptNewValues() {
        HashMap<String, String> defaults = new HashMap<String, String>();
        defaults.put("date", "1977-06-08");
        defaults.put("length", "33");
        defaults.put("time", "77");

        BufferedReader bufferedReader = new BufferedReader(new StringReader("2000-01-01\n222\n555\n"));
        activity.prompt(bufferedReader, defaults);

        assertEquals("2000-01-01", dateFormat.format(activity.getDate()), "Should return new date");
        assertEquals(222, activity.getLength(), "Should return new length");
        assertEquals(555, activity.getTime(), "Should return new time");
        assertEquals("2000-01-01", defaults.get("date"), "Default date should be updated");

    }
}