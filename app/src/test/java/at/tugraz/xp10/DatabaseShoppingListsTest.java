package at.tugraz.xp10;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import java.util.HashMap;

import at.tugraz.xp10.firebase.Database;
import at.tugraz.xp10.firebase.DatabaseListValueEventListener;
import at.tugraz.xp10.firebase.DatabaseValueEventListener;
import at.tugraz.xp10.firebase.ShoppingLists;
import at.tugraz.xp10.model.ModelBase;
import at.tugraz.xp10.model.ShoppingList;

import static junit.framework.Assert.assertNotSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(JUnit4.class)
@PrepareForTest({ Database.class})
public class DatabaseShoppingListsTest {

    private Database mockedDatabase;

    private static final HashMap<String, ShoppingList> TestData;

    static {
        TestData = new HashMap<>();
        TestData.put("-LCcgnf6nfs4KOty-oAn", new ShoppingList("foo", "foo", "foo"));
        TestData.put("-LCchNPtr43TgU0sflIo", new ShoppingList("bar", "bar", "bar"));
        TestData.put("-LCchOPfUHwNBMw_oWT3", new ShoppingList("baz", "baz", "baz"));
        TestData.put("-LCd31jsrRr_SH6q9JSL", new ShoppingList("xxx", "xxx", "xxx"));
    }

    @Before
    public void before() {
        mockedDatabase = Mockito.mock(Database.class);
        PowerMockito.mockStatic(Database.class);
        when(Database.getInstance(Mockito.anyString())).thenReturn(mockedDatabase);
    }

    @Test
    public void getShoppingListTest() {
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                DatabaseListValueEventListener valueEventListener = (DatabaseListValueEventListener) invocation.getArguments()[1];
                valueEventListener.onNewData(TestData);

                return null;
            }
        }).when(mockedDatabase).installListListener(any(Class.class), any(DatabaseListValueEventListener.class));

        ShoppingLists fb = new ShoppingLists();
        fb.getShoppingList("test", new DatabaseValueEventListener() {
            @Override
            public <T extends ModelBase> void onNewData(T data) {
                Assert.assertTrue(TestData.containsValue(data));
            }
        });
    }
    @Test
    public void uninstallAllListenersTest() {

        ShoppingLists shoppingList = new ShoppingLists();
        shoppingList.uninstallAllListeners();

        assertNotSame(null, mockedDatabase);
    }

    @Test
    public void updateShoppingListTest() {
        ShoppingLists lists = new ShoppingLists();
        ShoppingList test = new ShoppingList("foo", "foo", "hallo");
        lists.updateShoppingList(null, test);
        verify(mockedDatabase).pushValue(test);

        lists.updateShoppingList("test", test);
        verify(mockedDatabase).pushValue(test);
    }
}
