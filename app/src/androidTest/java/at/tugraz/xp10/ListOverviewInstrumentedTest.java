package at.tugraz.xp10;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(AndroidJUnit4.class)
public class ListOverviewInstrumentedTest {

    @Rule
    public ActivityTestRule<ListOverview> mActivityRule = new ActivityTestRule<>(
            ListOverview.class);

    @Test
    public void useAppContext() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("at.tugraz.xp10", appContext.getPackageName());
    }

    @Test
    public void test_if_present() throws Exception {
        assertNotEquals(null, findViewById(R.id.grid_overview));
    }
}
