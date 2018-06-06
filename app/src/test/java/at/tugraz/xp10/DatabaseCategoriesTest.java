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

import at.tugraz.xp10.firebase.Categories;
import at.tugraz.xp10.firebase.Database;
import at.tugraz.xp10.firebase.DatabaseListValueEventListener;
import at.tugraz.xp10.model.Category;
import at.tugraz.xp10.model.ModelBase;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(JUnit4.class)
@PrepareForTest({ Database.class})
public class DatabaseCategoriesTest {

    private Database mockedDatabase;

    private static final HashMap<String, Category> TestData;
    static {
        TestData = new HashMap<>();
        TestData.put("-LCcgnf6nfs4KOty-oAn", new Category("foo"));
        TestData.put("-LCchNPtr43TgU0sflIo", new Category("bar"));
        TestData.put("-LCchOPfUHwNBMw_oWT3", new Category("baz"));
        TestData.put("-LCd31jsrRr_SH6q9JSL", new Category("xxx"));
    }

    @Before
    public void before() {
        mockedDatabase = Mockito.mock(Database.class);
        PowerMockito.mockStatic(Database.class);
        when(Database.getInstance(Mockito.anyString())).thenReturn(mockedDatabase);
    }

    @Test
    public void getListOfCategoriesTest() {
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                DatabaseListValueEventListener valueEventListener = (DatabaseListValueEventListener) invocation.getArguments()[1];
                valueEventListener.onNewData(TestData);

                return null;
            }
        }).when(mockedDatabase).installListListener(any(Class.class), any(DatabaseListValueEventListener.class));

        Categories fb = new Categories();
        fb.getCategories(new DatabaseListValueEventListener() {
            @Override
            public <T extends ModelBase> void onNewData(HashMap<String, T> data) {
                for (T c : data.values())
                {
                    Assert.assertTrue(TestData.containsValue((Category) c));
                }
            }
        });
    }

    @Test
    public void putCategoryTest()
    {
        Categories cats = new Categories();
        Category test = new Category("foo");
        cats.putCategory(test);

        verify(mockedDatabase).pushValue(test);

    }
}
