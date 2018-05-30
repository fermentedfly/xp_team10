package at.tugraz.xp10;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

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
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(JUnit4.class)
@PrepareForTest({ FirebaseAuth.class, FirebaseDatabase.class})
public class DatabaseLoginTest {

    private FirebaseAuth mockedAuth;
    private FirebaseDatabase mockedDB;

    @Before
    public void before() {
        mockedAuth = Mockito.mock(FirebaseAuth.class);
        PowerMockito.mockStatic(FirebaseAuth.class);
        when(FirebaseAuth.getInstance()).thenReturn(mockedAuth);

        mockedDB = Mockito.mock(FirebaseDatabase.class);
        PowerMockito.mockStatic(FirebaseDatabase.class);
        when(FirebaseDatabase.getInstance()).thenReturn(mockedDB);
    }
}
