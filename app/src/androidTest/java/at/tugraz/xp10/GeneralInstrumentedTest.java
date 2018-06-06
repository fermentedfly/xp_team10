package at.tugraz.xp10;

import android.app.Activity;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.pchmn.materialchips.ChipView;
import com.pchmn.materialchips.ChipsInput;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;
import java.util.Iterator;

import at.tugraz.xp10.adapter.AllListRecyclerViewAdapter;
import at.tugraz.xp10.adapter.ShoppingListItemListAdapter;
import at.tugraz.xp10.firebase.Login;
import at.tugraz.xp10.firebase.LoginValueEventListener;
import at.tugraz.xp10.fragments.AllListFragment;
import at.tugraz.xp10.model.User;

import static android.support.test.InstrumentationRegistry.getContext;
import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static at.tugraz.xp10.Utils.EditTextMatchers.withErrorText;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.core.AllOf.allOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@RunWith(AndroidJUnit4.class)
public class GeneralInstrumentedTest {

    private User mUser;

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(LoginActivity.class);

    private boolean loggedIn = false;

    @Before
    public  void setUp() throws InterruptedException {

        onView(withId(R.id.email)).perform(typeText("admin@xp10.com"));
        onView(withId(R.id.password)).perform(typeText("admin123"));
        closeSoftKeyboard();
        onView(withId(R.id.login_button)).perform(click());
//        Login login = new Login();
////        if(!loggedIn) {
//            FirebaseAuth.getInstance().signInWithEmailAndPassword("admin@xp10.com", "admin123");
//
//            loggedIn = true;
////        }
       Thread.sleep(3000);
    }

    @After
    public void tearDown() throws InterruptedException {
        FirebaseAuth.getInstance().signOut();
    }

    @Test
    public void useAppContext() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("at.tugraz.xp10", appContext.getPackageName());
    }

    @Test
    public void test_if_grid_view_present() throws Exception {
        assertNotNull(onView(withText(R.id.all_list_recyclerview)));
    }

    @Test
    public void clickAddListButton() throws Exception {
        onView(withId(R.id.button_add_list)).check(matches(isDisplayed()));
    }
    @Test
    public void checkIfListsDisplayed() throws Exception {

        onView(withId(R.id.button_add_list)).check(matches(isDisplayed()));

        AllListRecyclerViewAdapter frag = ((AllListFragment) ((MainActivity)getActivityInstance()).getSupportFragmentManager().getFragments().get(0)).getmAdapter();
        assertEquals(2,frag.getItemCount());

    }

    @Test
    public void checkClickOnList() throws Exception {

        onView(withId(R.id.all_list_recyclerview)).perform(RecyclerViewActions.actionOnItem(hasDescendant(withText("Test")),click()));
        Thread.sleep(2000);
    }

    @Test
    public void checkSideMenu() throws Exception {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_manage_friends));
        Thread.sleep(1000);
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_lists));
        Thread.sleep(1000);
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_manage_categories));
        Thread.sleep(1000);
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_user_settings));
        Thread.sleep(1000);
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_logout));
        Thread.sleep(1000);
    }

    @Test
    public void AddFriendWrong() throws Exception {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_manage_friends));
        Thread.sleep(1000);

        onView(withId(R.id.textedit_friend_email)).perform(typeText("admin@xp10"));
        onView(withId(R.id.button_add_friend)).perform(click());
        onView(withId(R.id.textedit_friend_email)).check(matches(withErrorText(R.string.invalid_email)));
    }

    @Test
    public void AddAndDeleteFriend() throws Exception {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_manage_friends));
        Thread.sleep(1000);

        onView(withId(R.id.textedit_friend_email)).perform(typeText("admin@xp10.com"));
        closeSoftKeyboard();
        Thread.sleep(500);
        onView(withId(R.id.button_add_friend)).perform(click());
        onView(withId(R.id.textedit_friend_email)).perform(typeText("admin@xp10.com"));
        closeSoftKeyboard();
        Thread.sleep(1000);
        onView(withId(R.id.delete_button)).perform(click());
        Thread.sleep(500);
        onView(withId(R.id.button_add_friend)).perform(click());
        onView(withId(R.id.textedit_friend_email)).perform(typeText("admin@xp10.com"));
        closeSoftKeyboard();
        Thread.sleep(1000);
        onView(withId(R.id.button_add_friend)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.label)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.delete_button)).perform(click());

        // onView(withText("admin")).perform(click());
    }

    @Test
    public void checkEditList() throws Exception {
        onView(withId(R.id.all_list_recyclerview)).perform(RecyclerViewActions.actionOnItem(hasDescendant(withText("Test")),click()));
        Thread.sleep(2000);
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        Thread.sleep(500);
        onView(withText("Settings")).perform(click());
        Thread.sleep(3000);
        onView(withId(R.id.list_setting_save)).perform(click());
    }

    @Test
    public void checkEditListItemsSave() throws Exception {
        onView(withId(R.id.all_list_recyclerview)).perform(RecyclerViewActions.actionOnItem(hasDescendant(withText("Test")),click()));
        Thread.sleep(2000);
        onView(withText("Murauer")).perform(longClick());
        Thread.sleep(1000);
        onView(withId(R.id.lvSaveButton)).perform(click());
    }

    @Test
    public void checkEditListItemsCancel() throws Exception {
        onView(withId(R.id.all_list_recyclerview)).perform(RecyclerViewActions.actionOnItem(hasDescendant(withText("Test")),click()));
        Thread.sleep(2000);
        onView(withText("Murauer")).perform(longClick());
        Thread.sleep(1000);
        onView(withId(R.id.lvCancelButton)).perform(click());
    }

    @Test
    public void checkAddItems() throws Exception {
        onView(withId(R.id.all_list_recyclerview)).perform(RecyclerViewActions.actionOnItem(hasDescendant(withText("Delete")),click()));
        Thread.sleep(2000);
        onView(withId(R.id.addItemButton)).perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.item_name)).perform(typeText("ListToDelete"));
        onView(withId(R.id.item_quantity)).perform(typeText("1"));
        closeSoftKeyboard();

        onView(withId(R.id.lvSaveButton)).perform(click());
        Thread.sleep(1000);


        onView(withId(R.id.shopping_list_item_purchased)).check(matches(allOf( isEnabled(), isClickable()))).perform(
                new ViewAction() {
                    @Override
                    public Matcher<View> getConstraints() {
                        return ViewMatchers.isEnabled(); // no constraints, they are checked above
                    }

                    @Override
                    public String getDescription() {
                        return "";
                    }

                    @Override
                    public void perform(UiController uiController, View view) {
                        view.performClick();
                    }
                }
        );

        onView(withText("ListToDelete")).perform(longClick());
        Thread.sleep(500);

        onView(withId(R.id.item_delete)).check(matches(allOf( isEnabled(), isClickable()))).perform(
                new ViewAction() {
                    @Override
                    public Matcher<View> getConstraints() {
                        return ViewMatchers.isEnabled(); // no constraints, they are checked above
                    }

                    @Override
                    public String getDescription() {
                        return "";
                    }

                    @Override
                    public void perform(UiController uiController, View view) {
                        view.performClick();
                    }
                }
        );

    }

    @Test
    public void insertNewListTitleError() throws Exception {

       onView(withId(R.id.button_add_list)).perform(click());
       Thread.sleep(1000);
       onView(withId(R.id.list_setting_save)).perform(click());
       onView(withId(R.id.list_setting_save)).perform(click());
       onView(withId(R.id.list_setting_title)).check(matches(withErrorText(R.string.error_field_required)));

    }

    @Test
    public void checkDifferentLongClicks() throws Exception {
        onView(withId(R.id.all_list_recyclerview)).perform(RecyclerViewActions.actionOnItem(hasDescendant(withText("Test")),click()));
        Thread.sleep(2000);
        onView(withText("Murauer")).perform(longClick());
        Thread.sleep(1000);
        onView(withId(R.id.lvCancelButton)).perform(click());

        Thread.sleep(1000);
        onView(withText("20")).perform(longClick());
        Thread.sleep(1000);
        onView(withId(R.id.lvCancelButton)).perform(click());

    }

    @Test
    public void addCategory() throws Exception {
        Thread.sleep(1000);
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_manage_categories));
        Thread.sleep(1000);
        onView(withId(R.id.addCategoryButton)).perform(click());
        Thread.sleep(1000);
        onView(withText("Submit")).perform(click());
        onView(withText("Cancel")).perform(click());

    }




    private Activity getActivityInstance(){
        final Activity[] currentActivity = {null};

        getInstrumentation().runOnMainSync(new Runnable(){
            public void run(){
                Collection<Activity> resumedActivity = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED);
                Iterator<Activity> it = resumedActivity.iterator();
                currentActivity[0] = it.next();
            }
        });

        return currentActivity[0];
    }


}
