package at.tugraz.xp10;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

import java.util.ArrayList;
import java.util.HashMap;

import at.tugraz.xp10.firebase.Categories;
import at.tugraz.xp10.firebase.CategoriesValueEventListener;
import at.tugraz.xp10.model.Category;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(JUnit4.class)
@PrepareForTest({ FirebaseDatabase.class})
public class ManageCategoriesUnitTest {

    private DatabaseReference mockedDatabaseReference;
    private static final HashMap<String, Object> TestData;
    static {
        TestData = new HashMap<>();
        TestData.put("-LCcgnf6nfs4KOty-oAn", new Category("foo"));
        TestData.put("-LCchNPtr43TgU0sflIo", new Category("bar"));
        TestData.put("-LCchOPfUHwNBMw_oWT3", new Category("baz"));
        TestData.put("-LCd31jsrRr_SH6q9JSL", new Category("xxx"));
    }

    @Before
    public void before() {
        mockedDatabaseReference = Mockito.mock(DatabaseReference.class);

        FirebaseDatabase mockedFirebaseDatabase = Mockito.mock(FirebaseDatabase.class);
        when(mockedFirebaseDatabase.getReference()).thenReturn(mockedDatabaseReference);

        PowerMockito.mockStatic(FirebaseDatabase.class);
        when(FirebaseDatabase.getInstance()).thenReturn(mockedFirebaseDatabase);
    }

    @Test
    public void getListOfCategoriesTest() {
        when(mockedDatabaseReference.child(anyString())).thenReturn(mockedDatabaseReference);

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ValueEventListener valueEventListener = (ValueEventListener) invocation.getArguments()[0];

                DataSnapshot mockedDataSnapshot = Mockito.mock(DataSnapshot.class);
                when(mockedDataSnapshot.getValue()).thenReturn(TestData);

                ArrayList<DataSnapshot> mockedSnapshots = new ArrayList<>();
                for (String key : TestData.keySet())
                {
                    DataSnapshot ms = Mockito.mock(DataSnapshot.class);
                    when(ms.getValue(Category.class)).thenReturn(((Category)(TestData.get(key))));
                    when(mockedDataSnapshot.child(key)).thenReturn(ms);
                    mockedSnapshots.add(ms);
                }

                valueEventListener.onDataChange(mockedDataSnapshot);

                return null;
            }
        }).when(mockedDatabaseReference).addValueEventListener(any(ValueEventListener.class));

        Categories fb = new Categories();
        fb.getCategories(new CategoriesValueEventListener() {
            @Override
            public void onNewData(ArrayList<Category> Categories) {
                for (Category c : Categories)
                {
                    Assert.assertTrue(TestData.containsValue(c));
                }
            }
        });
    }

}
