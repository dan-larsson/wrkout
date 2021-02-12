package activities;

import com.wrkout.activites.ActivityHandler;
import com.wrkout.activites.BaseActivity;
import com.wrkout.activites.repetitive.ChinUp;
import com.wrkout.activites.repetitive.Deadlift;
import com.wrkout.activites.repetitive.PullUp;
import com.wrkout.storage.StorageHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;


class StorageMock implements StorageHandler {

    public void write(ArrayList<BaseActivity> activityList) {
        // Do nothing
    }

    public void read(ArrayList<BaseActivity> activityList) {
        Date now = new Date(System.currentTimeMillis());
        // Add PullUp 7 days ago
        activityList.add(new PullUp(1, 2, 3, new Date(now.getTime() - (7 * 24 * 60 * 60 * 1000))));
        // Add ChinUp 9 days ago
        activityList.add(new ChinUp(1, 2, 3, new Date(now.getTime() - (9 * 24 * 60 * 60 * 1000))));
        // Add Deadlift 10 days ago
        activityList.add(new Deadlift(1, 2, 3, new Date(now.getTime() - (10 * 24 * 60 * 60 * 1000))));
    }

    public String[] getColumnNames() {
        return null;
    }

    @Override
    public void addActivity(BaseActivity activity, int user_id) {

    }

    public String[][] getRows(int user_id) {
        return null;
    }
}


public class ActivityHandlerTest {

    @Test
    @DisplayName("Ensure all activities are represented in the newActivity method")
    void testAvailableActivities() {
        String[] all = ActivityHandler.oneOfEach();
        for (String s : all) {
            assertNotNull(ActivityHandler.newActivity(s));
        }
    }

    @Test
    @DisplayName("Ensure all activities are represented in the newActivity method")
    void testRead() {
        ActivityHandler activityHandler = new ActivityHandler(new StorageMock());
        activityHandler.read();

        assertEquals(PullUp.NAME, activityHandler.get(0).getName());
        assertEquals(ChinUp.NAME, activityHandler.get(1).getName());
        assertEquals(Deadlift.NAME, activityHandler.get(2).getName());
        assertNull(activityHandler.get(3));
    }
}