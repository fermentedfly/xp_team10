package at.tugraz.xp10;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import java.util.ArrayList;
import java.util.HashMap;

import at.tugraz.xp10.firebase.Database;
import at.tugraz.xp10.firebase.DatabaseEventListener;
import at.tugraz.xp10.firebase.DatabaseListValueEventListener;
import at.tugraz.xp10.firebase.DatabaseValueEventListener;
import at.tugraz.xp10.model.Category;
import at.tugraz.xp10.model.ModelBase;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(JUnit4.class)
@PrepareForTest({ FirebaseDatabase.class})
public class DatabaseUnitTest {

    private DatabaseReference mockedDatabaseReference;
    private  DataSnapshot mockedDataSnapshot;

    @Before
    public void before() {
        mockedDatabaseReference = Mockito.mock(DatabaseReference.class);

        when(mockedDatabaseReference.child(anyString())).thenReturn(mockedDatabaseReference);
        when(mockedDatabaseReference.push()).thenReturn(mockedDatabaseReference);

        FirebaseDatabase mockedFirebaseDatabase = Mockito.mock(FirebaseDatabase.class);
        when(mockedFirebaseDatabase.getReference()).thenReturn(mockedDatabaseReference);

        PowerMockito.mockStatic(FirebaseDatabase.class);
        when(FirebaseDatabase.getInstance()).thenReturn(mockedFirebaseDatabase);

        mockedDataSnapshot = Mockito.mock(DataSnapshot.class);
        when(mockedDataSnapshot.getValue(any(Class.class))).thenReturn(null);
        ArrayList<DataSnapshot> children = new ArrayList<>();
        children.add(mockedDataSnapshot);
        when(mockedDataSnapshot.getChildren()).thenReturn(children);
        when(mockedDataSnapshot.getKey()).thenReturn("bar");
    }

    @Test
    public void installListListenerTest() {

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                ValueEventListener valueEventListener = (ValueEventListener) invocation.getArguments()[0];
                valueEventListener.onDataChange(mockedDataSnapshot);
                valueEventListener.onCancelled(null);
                return null;
            }
        }).when(mockedDatabaseReference).addValueEventListener(any(ValueEventListener.class));

        Database db = Database.getInstance("foo");
        DatabaseListValueEventListener listener = mock(DatabaseListValueEventListener.class);

        db.installListListener(ModelBase.class, listener);
        verify(listener).onNewData(Matchers.<HashMap<String, ? extends ModelBase>>any());
    }

    @Test
    public void installValueListenerTest() {

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                ValueEventListener valueEventListener = (ValueEventListener) invocation.getArguments()[0];
                valueEventListener.onDataChange(mockedDataSnapshot);
                valueEventListener.onCancelled(null);
                return null;
            }
        }).when(mockedDatabaseReference).addValueEventListener(any(ValueEventListener.class));

        Database db = Database.getInstance("foo");
        DatabaseValueEventListener listener = mock(DatabaseValueEventListener.class);

        db.installValueListener(ModelBase.class, "bar", listener);
        verify(listener).onNewData(null);
        HashMap<DatabaseEventListener, ValueEventListener> listeners = db.getListeners();
        assertEquals(listeners.size(), 1);
        assertTrue(listeners.containsKey(listener));
    }

    @Test
    public void pushValueTest () {
        Database db = Database.getInstance("foo");
        db.pushValue(new Category());
        verify(mockedDatabaseReference).push();
        verify(mockedDatabaseReference).setValue(any(ModelBase.class));
    }

    @Test
    public void setValueTest() {
        Database db = Database.getInstance("foo");
        db.setValue("bar", any(ModelBase.class));
        verify(mockedDatabaseReference).child("bar");
        verify(mockedDatabaseReference).setValue(any(ModelBase.class));
    }

    @Test
    public void getNewKeyTest() {
        Database db = Database.getInstance("foo");
        when(mockedDatabaseReference.getKey()).thenReturn("bar");
        String key = db.getNewKey();
        assertEquals("bar", key);
    }

    @Test
    public void deleteValueTest() {
        Database db = Database.getInstance("foo");
        db.deleteValue("bar");
        verify(mockedDatabaseReference).child("bar");
        verify(mockedDatabaseReference).removeValue();
    }

    @Test
    public void uninstallAllListenersTest() {
        Database db = Database.getInstance("foo");
        DatabaseValueEventListener listener = mock(DatabaseValueEventListener.class);
        db.installValueListener(ModelBase.class, "bar", listener);

        assertEquals(db.getListeners().size(), 1);

        db.uninstallAllListeners();
        verify(mockedDatabaseReference, atLeastOnce()).removeEventListener(any(ValueEventListener.class));
        assertEquals(db.getListeners().size(), 0);


    }
}
