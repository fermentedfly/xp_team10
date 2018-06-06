package at.tugraz.xp10;

import android.view.View;
import android.widget.GridLayout;

import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;


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
/*@RunWith(AndroidJUnit4.class)
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

    *//*********************************** addItemButton ***********************************//*

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

    *//*********************************** listGridLayout ***********************************//*

    @Test
    public void listViewFragmentLayout() {
//        onView(withId(R.id.txtCheckBox)).check(matches(isDisplayed()));
//        onView(withId(R.id.txtProductName)).check(matches(isDisplayed()));
//        onView(withId(R.id.txtCategoryName)).check(matches(isDisplayed()));
//        onView(withId(R.id.txtPrice)).check(matches(isDisplayed()));
//        onView(withId(R.id.txtQuantity)).check(matches(isDisplayed()));
    }

    @Test
    public void listGridIfPresent() {
        //assertNotNull(onView(withText(R.id.listGridLayout)));
    }

    @Test
    public void addItemButtonListener() throws Exception {
        onView(withId(R.id.addItemButton)).check(matches(isDisplayed()));
        onView(withId(R.id.addItemButton)).perform(click());

        //onView(withId(R.id.listGridLayout)).check(ViewAssertions.matches (Matchers.withRowCount(2)));
    }

    @Test
    public void is_title_set() {
        //onView(withId(R.id.listGridLayout)).check(matches(isDisplayed()));

        ActionBar actionBar = ((AppCompatActivity)mainActivityTestRule.getActivity()).getSupportActionBar();

        assertNotNull(actionBar);

        CharSequence title = actionBar.getTitle();

        assertEquals(s_Title, title);
    }
}*/
