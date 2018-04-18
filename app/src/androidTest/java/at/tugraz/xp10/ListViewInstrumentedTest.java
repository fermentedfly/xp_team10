package at.tugraz.xp10;

import android.os.Bundle;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ListView;

import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;

import at.tugraz.xp10.Utils.TestFragmentActivity;
import at.tugraz.xp10.fragments.ListViewFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.isSelected;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;


class Matchers {
    public static Matcher<View> withRowCount (final int size) {
        return new TypeSafeMatcher<View> () {
            @Override
            public void describeTo(org.hamcrest.Description description) {

            }

            @Override public boolean matchesSafely (final View view) {
                return ((GridLayout) view).getRowCount() == size;
            }
        };
    }
}


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ListViewInstrumentedTest {

    private static final String s_Title = "TestTitle";

    private ListViewFragment m_Fragment;

    @Rule
    public ActivityTestRule<TestFragmentActivity> mainActivityTestRule =
            new ActivityTestRule<>(TestFragmentActivity.class);

    @Before
    public void init(){
        Bundle bundle = new Bundle();
        bundle.putString("Title", s_Title);

        m_Fragment = new ListViewFragment();
        m_Fragment.setArguments(bundle);
        mainActivityTestRule.getActivity().setFragment(m_Fragment);
    }

    @Test
    public void addItemButtonIsEnabled() {
        onView(withId(R.id.addItemButton)).check(matches(isEnabled()));
    }

    @Test
    public void addItemButtonIsDisplayed() {
        onView(withId(R.id.addItemButton)).check(matches(isDisplayed()));
    }

    @Test
    public void addItemButtonIsCompletelyDisplayed() {
        onView(withId(R.id.addItemButton)).check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void addItemButtonIsNotSelectable() {
        onView(withId(R.id.addItemButton)).check(matches(not(isSelected())));
    }

    @Test
    public void addItemButtonIsClickable() {
        onView(withId(R.id.addItemButton)).check(matches(isClickable()));
    }

    /*
    @Test
    public void addItemButtonListener() throws Exception {
        FragmentTransaction fragmentTransaction = mainActivityTestRule.getActivity().getSupportFragmentManager().beginTransaction();
        Fragment fragment = new ListViewFragment();
        fragmentTransaction.replace(R.id.content_frame,fragment,"myFragmentTag").addToBackStack(null);
        fragmentTransaction.commit();

        LayoutInflater inflater = mainActivityTestRule.getActivity().getLayoutInflater();
        ViewGroup view = (ViewGroup) mainActivityTestRule.getActivity().findViewById(android.R.id.content);
        View mView = inflater.inflate(R.layout.fragment_list_view, view, false);

        assertNotNull(mView);
        GridLayout gl = (GridLayout) mView.findViewById(R.id.listGridLayout);
        assertNotNull(gl);
        int row_count = gl.getRowCount();
        FloatingActionButton addItemButton = (FloatingActionButton) mView.findViewById(R.id.addItemButton);
        //addItemButton.performClick();
        assertEquals(row_count, 1);
        onView(withId(R.id.addItemButton)).perform(click());
        //onView(withText("Ketchup")).check(matches(isDisplayed()));
        assertEquals(row_count+1, gl.getRowCount());
    }

    @Test
    public void txtPriceNumbers() throws Exception {
        //onView(withId(R.id.addItemButton)).perform(click());
        //onView(withId(R.id.txtCheckBox)).check(matches(isDisplayed()));
    }

    @Test
    public void txtQuantityNumbers() throws Exception {
        //onView(withId(R.id.addItemButton)).perform(click());
        //onView(withId(R.id.txtCheckBox)).check(matches(isDisplayed()));
    }
    */

    @Test
    public void listViewFragmentLayout() {
        onView(withId(R.id.txtCheckBox)).check(matches(isDisplayed()));
        onView(withId(R.id.txtProductName)).check(matches(isDisplayed()));
        onView(withId(R.id.txtCategoryName)).check(matches(isDisplayed()));
        onView(withId(R.id.txtPrice)).check(matches(isDisplayed()));
        onView(withId(R.id.txtQuantity)).check(matches(isDisplayed()));
    }

    @Test
    public void listGridIfPresent() {
        assertNotNull(onView(withText(R.id.listGridLayout)));
    }

    @Test
    public void addItemButtonListener() throws Exception {
        onView(withId(R.id.addItemButton)).check(matches(isDisplayed()));
        onView(withId(R.id.addItemButton)).perform(click());

        onView(withId(R.id.listGridLayout)).check(ViewAssertions.matches (Matchers.withRowCount(2)));
    }

    @Test
    public void is_title_set() {
        onView(withId(R.id.listGridLayout)).check(matches(isDisplayed()));

        ActionBar actionBar = ((AppCompatActivity)mainActivityTestRule.getActivity()).getSupportActionBar();

        assertNotNull(actionBar);

        CharSequence title = actionBar.getTitle();

        assertEquals(s_Title, title);
    }
}
