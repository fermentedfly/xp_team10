package at.tugraz.xp10;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import at.tugraz.xp10.firebase.Database;
import at.tugraz.xp10.firebase.Login;
import at.tugraz.xp10.firebase.LoginValueEventListener;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(JUnit4.class)
@PrepareForTest({FirebaseAuth.class, Database.class})
public class DatabaseLoginTest {

    private FirebaseAuth mockedAuth;
    private Database mockedDB;
    private FirebaseUser mockedFBUser;

    @Before
    public void before() {
        mockedAuth = Mockito.mock(FirebaseAuth.class);
        PowerMockito.mockStatic(FirebaseAuth.class);
        when(FirebaseAuth.getInstance()).thenReturn(mockedAuth);

        mockedDB = Mockito.mock(Database.class);
        PowerMockito.mockStatic(Database.class);
        when(Database.getInstance(anyString())).thenReturn(mockedDB);

        mockedFBUser = mock(FirebaseUser.class);
        when(mockedAuth.getCurrentUser()).thenReturn(mockedFBUser);
    }

    @Test
    public void setForgotPasswordMailTest()
    {
        Task<Void> mockedTask = (Task<Void>) Mockito.mock(Task.class);
        when(mockedAuth.sendPasswordResetEmail(anyString())).thenReturn(mockedTask);

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                OnSuccessListener<Void> listener = (OnSuccessListener<Void>) invocation.getArguments()[0];
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
                OnSuccessListener<AuthResult> listener = (OnSuccessListener<AuthResult>) invocation.getArguments()[0];
                listener.onSuccess(null);

                return null;
        }
        }).when(mockedTask).addOnSuccessListener(Matchers.<OnSuccessListener<AuthResult>>any());

        Login login = new Login();
        LoginValueEventListener listener = mock(LoginValueEventListener.class);
        login.createUserWithEmailAndPassword("email", "bar", "foo", "bar", listener);
        verify(mockedFBUser).sendEmailVerification();
        verify(mockedDB).setValue(anyString(), any());
        verify(listener).onSuccess();

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                OnFailureListener listener = (OnFailureListener) invocation.getArguments()[0];
                listener.onFailure(new Exception());

                return null;
            }
        }).when(mockedTask).addOnFailureListener(Matchers.<OnFailureListener>any());

        login = new Login();
        listener = mock(LoginValueEventListener.class);
        login.createUserWithEmailAndPassword("email", "bar", "foo", "bar", listener);
        verify(listener).onFailure(any(Exception.class));
    }

    @Test
    public void isUserLoggedInTest() {
        Login login = new Login();
        assertTrue(login.isUserLoggedIn());

        when(mockedAuth.getCurrentUser()).thenReturn(null);
        assertFalse(login.isUserLoggedIn());
    }

    @Test
    public void signInWithUserAndPasswordTest() {
        Task<AuthResult> mockedTask = (Task<AuthResult>) mock(Task.class);
        when(mockedAuth.signInWithEmailAndPassword(anyString(), anyString())).thenReturn(mockedTask);

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                OnSuccessListener<AuthResult> listener = (OnSuccessListener<AuthResult>) invocation.getArguments()[0];
                listener.onSuccess(null);

                return null;
            }
        }).when(mockedTask).addOnSuccessListener(Matchers.<OnSuccessListener<AuthResult>>any());

        Login login = new Login();
        LoginValueEventListener listener = mock(LoginValueEventListener.class);
        login.signInWithUserAndPassword("foo", "bar", listener);
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
        login.signInWithUserAndPassword("foo", "bar", listener);
        verify(listener).onFailure(any(Exception.class));
    }

    @Test
    public void getCurrentUserTest() {
        Login login = new Login();
        FirebaseUser user = login.getCurrentUser();
        assertEquals(user, mockedFBUser);
    }
}
