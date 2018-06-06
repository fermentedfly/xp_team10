package at.tugraz.xp10;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import junit.framework.Assert;

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

import java.util.HashMap;

import at.tugraz.xp10.firebase.Categories;
import at.tugraz.xp10.firebase.Database;
import at.tugraz.xp10.firebase.DatabaseListValueEventListener;
import at.tugraz.xp10.firebase.Login;
import at.tugraz.xp10.firebase.LoginValueEventListener;
import at.tugraz.xp10.model.Category;
import at.tugraz.xp10.model.ModelBase;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(JUnit4.class)
@PrepareForTest({FirebaseAuth.class, Database.class})
public class DatabaseLoginTest {

    private FirebaseAuth mockedAuth;
    private Database mockedDB;

    @Before
    public void before() {
        mockedAuth = Mockito.mock(FirebaseAuth.class);
        PowerMockito.mockStatic(FirebaseAuth.class);
        when(FirebaseAuth.getInstance()).thenReturn(mockedAuth);

        mockedDB = Mockito.mock(Database.class);
        PowerMockito.mockStatic(Database.class);
        when(Database.getInstance("foo")).thenReturn(mockedDB);
    }

    @Test
    public void setForgotPasswordMailTest()
    {
        Task<Void> mockedTask = (Task<Void>) Mockito.mock(Task.class);
        when(mockedAuth.sendPasswordResetEmail(anyString())).thenReturn(mockedTask);

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                OnSuccessListener listener = (OnSuccessListener) invocation.getArguments()[0];
                listener.onSuccess(null);

                return null;
            }
        }).when(mockedTask).addOnSuccessListener(Matchers.<OnSuccessListener<Void>>any());

        Login login = new Login();
        LoginValueEventListener listener = mock(LoginValueEventListener.class);
        login.setForgotPasswordMail("foo", listener);
        verify(listener).onSuccess();

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                OnFailureListener listener = (OnFailureListener) invocation.getArguments()[0];
                listener.onFailure(new Exception("foo"));

                return null;
            }
        }).when(mockedTask).addOnFailureListener(Matchers.<OnFailureListener>any());

        login = new Login();
        listener = mock(LoginValueEventListener.class);
        login.setForgotPasswordMail("foo", listener);
        verify(listener).onFailure(any(Exception.class));
    }

    @Test
    public void createUserWithEmailAndPasswordTest() {

        Task<AuthResult> mockedTask = (Task<AuthResult>) Mockito.mock(Task.class);
        when(mockedAuth.createUserWithEmailAndPassword(anyString(), anyString())).thenReturn(mockedTask);

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {

                return null;
            }
        }).when(mockedTask).addOnSuccessListener(any(OnSuccessListener.class));

    }
}
