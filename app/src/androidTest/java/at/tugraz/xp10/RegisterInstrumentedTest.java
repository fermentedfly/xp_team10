package at.tugraz.xp10;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import at.tugraz.xp10.fragments.ForgotPasswordDialogFragment;
import at.tugraz.xp10.fragments.RegisterFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static at.tugraz.xp10.Utils.EditTextMatchers.withErrorText;


@RunWith(AndroidJUnit4.class)
public class RegisterInstrumentedTest {

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
        onView(withId(R.id.register_button)).perform(click());
    }

    @Test
    public void displayLayout() throws Exception {
        onView(withId(R.id.register_firstname)).check(matches(isDisplayed()));
        onView(withId(R.id.register_lastname)).check(matches(isDisplayed()));
        onView(withId(R.id.register_email)).check(matches(isDisplayed()));
        onView(withId(R.id.register_password)).check(matches(isDisplayed()));
        onView(withId(R.id.register_confirm_password)).check(matches(isDisplayed()));
        onView(withId(R.id.register_cancel_button)).check(matches(isDisplayed()));
        onView(withId(R.id.register_button)).check(matches(isDisplayed()));
    }


    // TODO other tests with inputs and async answer from firebase
    // question the tutors?
}