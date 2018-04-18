package at.tugraz.xp10;

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
        FragmentManager fragmentManager = menuActivityTestRule.getActivity().getSupportFragmentManager();
        RegisterFragment newFragment = RegisterFragment.newInstance();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, newFragment)
                .addToBackStack(null).commit();
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
