package at.tugraz.xp10;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.isSelected;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertNotNull;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ListSettingInstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void init(){
        onView(withId(R.id.button_add_list)).perform(click());
    }


    @Test
    public void checkTextfieldsExist() {
        onView(withId(R.id.list_setting_title)).perform(typeText("Title"));
        onView(withId(R.id.list_setting_description)).perform(typeText("Description"));
     }
    @Test
    public void checkButtonsExist() {
        onView(withId(R.id.list_setting_cancel)).check(matches(isClickable()));
        onView(withId(R.id.list_setting_save)).check(matches(isClickable()));
    }
    @Test
    public void checkTitleNotEmpty() {
        onView(withId(R.id.list_setting_save)).perform(click());
    }
//    @Test
//    public void addUserFieldExists() {
//        //
//    }



}
