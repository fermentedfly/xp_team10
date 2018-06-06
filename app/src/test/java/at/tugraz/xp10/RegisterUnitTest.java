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

import at.tugraz.xp10.firebase.Login;
import at.tugraz.xp10.firebase.UserValueEventListener;
import at.tugraz.xp10.firebase.Users;
import at.tugraz.xp10.model.User;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(JUnit4.class)
@PrepareForTest({ FirebaseDatabase.class})
public class RegisterUnitTest {

    private Login mockedLogin;
    private static final HashMap<String, Object> TestData;
    static {
        TestData = new HashMap<>();
        TestData.put("fi4bsxjFdzUa7b6mZGIOIK55Gay2", new User("einfacheinfach@superwebsite.com",
                "Einfach", "Einfacher"));
    }

    @Before
    public void before() {
        mockedLogin = Mockito.mock(Login.class);
        //mockedDatabaseReference = Mockito.mock(DatabaseReference.class);


        //FirebaseDatabase mockedFirebaseDatabase = Mockito.mock(FirebaseDatabase.class);
        //when(mockedFirebaseDatabase.getReference()).thenReturn(mockedDatabaseReference);

        //PowerMockito.mockStatic(FirebaseDatabase.class);
        //when(FirebaseDatabase.getInstance()).thenReturn(mockedFirebaseDatabase);
    }

    @Test
    public void getListOfUsersTest() {
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
                    when(ms.getValue(User.class)).thenReturn(((User)(TestData.get(key))));
                    when(mockedDataSnapshot.child(key)).thenReturn(ms);
                    mockedSnapshots.add(ms);
                }

                valueEventListener.onDataChange(mockedDataSnapshot);

                return null;
            }
        }).when(mockedDatabaseReference).addValueEventListener(any(ValueEventListener.class));

        Users fb = new Users();
        fb.getUser("fi4bsxjFdzUa7b6mZGIOIK55Gay2", new UserValueEventListener() {
            public void onNewData(User user) {
                Assert.assertTrue(TestData.containsValue(user));
            }
        });
    }
}