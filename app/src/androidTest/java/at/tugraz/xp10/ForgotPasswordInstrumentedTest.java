package at.tugraz.xp10;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.FragmentManager;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import at.tugraz.xp10.fragments.ForgotPasswordDialogFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static at.tugraz.xp10.Utils.EditTextMatchers.withErrorText;


@RunWith(AndroidJUnit4.class)
public class ForgotPasswordInstrumentedTest {

    @Rule
    public ActivityTestRule<LoginActivity> menuActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Before
    public void init(){
        // sign out before tests
        if(menuActivityTestRule.getActivity().isUserLoggedIn())
        {
            // need to sign out
            onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_logout));
        }
        onView(withId(R.id.forgot_password_button)).perform(click());
    }
    @Test
    public void displayLayout() throws Exception {
        onView(withId(R.id.email_address)).check(matches(isDisplayed()));
        onView(withId(android.R.id.button1)).check(matches(isDisplayed()));
        onView(withId(android.R.id.button2)).check(matches(isDisplayed()));
    }
    @Test
    public void pressSubmitButtonMailRequired() throws Exception {
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.email_address)).check(matches(withErrorText(R.string.error_field_required)));
    }

    // TODO other tests with inputs and async answer from firebase
}
