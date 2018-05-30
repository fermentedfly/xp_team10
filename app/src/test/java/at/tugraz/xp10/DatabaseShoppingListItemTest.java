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
import at.tugraz.xp10.firebase.ShoppingListItems;
import at.tugraz.xp10.model.ModelBase;
import at.tugraz.xp10.model.ShoppingListItem;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(JUnit4.class)
@PrepareForTest({ Database.class})
public class DatabaseShoppingListItemTest {

    private Database mockedDatabase;

    private static final HashMap<String, ShoppingListItem> TestData;
    static {
        TestData = new HashMap<>();
        TestData.put("-LCcgnf6nfs4KOty-oAn", new ShoppingListItem("foo", 1., "kg", "cat 1", false, ""));
        TestData.put("-LCchNPtr43TgU0sflIo", new ShoppingListItem("bar", 1., "l", "cat 2", false, ""));
        TestData.put("-LCchOPfUHwNBMw_oWT3", new ShoppingListItem("baz", 1., "packs", "cat 3", false, ""));
        TestData.put("-LCd31jsrRr_SH6q9JSL", new ShoppingListItem("foo", 1., "gram", "cat 4", false, ""));
    }

    @Before
    public void before() {
        mockedDatabase = Mockito.mock(Database.class);
        PowerMockito.mockStatic(Database.class);
        when(Database.getInstance(anyString())).thenReturn(mockedDatabase);
    }

    @Test
    public void selectShoppingListTest()
    {
        ShoppingListItems DBRef = new ShoppingListItems();
        DBRef.selectShoppingList("foo");
    }

    @Test
    public void getShoppingListItemsTest() {
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                DatabaseListValueEventListener valueEventListener = (DatabaseListValueEventListener) invocation.getArguments()[1];
                valueEventListener.onNewData(TestData);

                return null;
            }
        }).when(mockedDatabase).installListListener(any(Class.class), any(DatabaseListValueEventListener.class));

        ShoppingListItems DBRef = new ShoppingListItems();
        DBRef.selectShoppingList("foo");
        DBRef.getShoppingListItems(new DatabaseListValueEventListener() {
            @Override
            public <T extends ModelBase> void onNewData(HashMap<String, T> data) {
                for (T c : data.values())
                {
                    Assert.assertTrue(TestData.containsValue(c));
                }
            }
        });
    }

    @Test
    public void addItemToShoppingListTest()
    {
        ShoppingListItems DBRef = new ShoppingListItems();
        DBRef.selectShoppingList("foo");
        DBRef.addItemToShoppingList("test", 0.5, "foo", "bar", true);
        verify(mockedDatabase).getNewKey();
        verify(mockedDatabase).setValue(anyString(), any(ShoppingListItem.class));
    }

    @Test
    public void deleteItemFromShoppingListTest()
    {
        ShoppingListItems DBRef = new ShoppingListItems();
        DBRef.selectShoppingList("foo");
        DBRef.deleteItemFromShoppingList("baz");
        verify(mockedDatabase).deleteValue("baz");
    }

    @Test
    public void updateItemInShoppingListTest()
    {
        ShoppingListItems DBRef = new ShoppingListItems();
        DBRef.selectShoppingList("foo");
        DBRef.updateItemInShoppingList("foo", "test", 0.5, "foo", "bar", true);
        verify(mockedDatabase).setValue(eq("foo"), any(ShoppingListItem.class));
    }
}
