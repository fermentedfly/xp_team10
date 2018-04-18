package at.tugraz.xp10;

import android.content.Context;
import android.content.res.Resources;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static at.tugraz.xp10.Utils.EditTextMatchers.withErrorText;
import static org.junit.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
public class LoginInstrumentedTest {

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
    }

    @Test
    public void displayLayout() throws Exception {
        onView(withId(R.id.email)).check(matches(isDisplayed()));
        onView(withId(R.id.password)).check(matches(isDisplayed()));
        onView(withId(R.id.login_button)).check(matches(isDisplayed()));
        onView(withId(R.id.forgot_password_button)).check(matches(isDisplayed()));
        onView(withId(R.id.register_button)).check(matches(isDisplayed()));
    }
    @Test
    public void pressLoginButtonMailRequired() throws Exception {
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.email)).check(matches(withErrorText(R.string.error_field_required)));
    }
    @Test
    public void pressLoginButtonMailWrong() throws Exception {
        onView(withId(R.id.email)).perform(typeText("wrong"));
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.email)).check(matches(withErrorText(R.string.error_invalid_email)));
    }
    @Test
    public void pressLoginButtonPasswordRequired() throws Exception {
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.password)).check(matches(withErrorText(R.string.error_field_required)));
    }
    @Test
    public void pressLoginButtonPasswordTooShort() throws Exception {
        onView(withId(R.id.password)).perform(typeText("123"));
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.password)).check(matches(withErrorText(R.string.error_invalid_password)));
    }
    @Test
    public void pressLoginButtonEmpty() throws Exception {
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.email)).check(matches(withErrorText(R.string.error_field_required)));
        onView(withId(R.id.password)).check(matches(withErrorText(R.string.error_field_required)));
    }
    @Test
    public void pressLoginButtonCorrect() throws Exception {
        onView(withId(R.id.email)).perform(typeText("ui@test.com"));
        onView(withId(R.id.password)).perform(typeText("test1234"));
        onView(withId(R.id.login_button)).perform(click());
        //TODO: Check if next view is main page
    }

    @Test
    public void pressRegisterButton() throws Exception {
        onView(withId(R.id.register_button)).perform(click());
        //TODO: Check if next view is register form
    }
    @Test
    public void pressForgotPasswordButton() throws Exception {
        onView(withId(R.id.forgot_password_button)).perform(click());
        //TODO: Check popup window
    }
}
