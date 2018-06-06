package at.tugraz.xp10;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import at.tugraz.xp10.firebase.Database;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(JUnit4.class)
@PrepareForTest({FirebaseAuth.class, Database.class})
public class LoginUnitTest {

    private FirebaseAuth mockedFirebaseAuth;
    private FirebaseUser mockedUser;
    private LoginActivity loginActivity;
    private LoginActivity loginActivityMock;
    private Database mockedDatabase;

    @Before
    public void before() {
        mockedFirebaseAuth = Mockito.mock(FirebaseAuth.class);
        mockedUser = Mockito.mock(FirebaseUser.class);
        mockedDatabase = Mockito.mock(Database.class);

        PowerMockito.mockStatic(FirebaseAuth.class);
        when(FirebaseAuth.getInstance()).thenReturn(mockedFirebaseAuth);

        PowerMockito.mockStatic(Database.class);
        Mockito.when(Database.getInstance(Mockito.anyString())).thenReturn(mockedDatabase);

        loginActivity = new LoginActivity();
        loginActivityMock = Mockito.mock(LoginActivity.class);
    }

    @Test
    public void testLoggedInUser() {
        when(mockedFirebaseAuth.getCurrentUser()).thenReturn(null);
        assertEquals(false, loginActivity.isUserLoggedIn());

        // Mock User
        when(mockedFirebaseAuth.getCurrentUser()).thenReturn(mockedUser);
        assertEquals(true, loginActivity.isUserLoggedIn());
    }

    @Test
    public void attemptLogin() throws Exception {
        when(loginActivityMock.isPasswordValid(anyString())).thenCallRealMethod();
        when(loginActivityMock.isEmailValid(anyString())).thenCallRealMethod();
        when(loginActivityMock.attemptLogin()).thenCallRealMethod();

        doReturn("admin@xp10.com").when(loginActivityMock).getmEmail();
        doReturn("admin123").when(loginActivityMock).getmPassword();
        assertEquals(loginActivityMock.attemptLogin(), true);

        doReturn("admin@xp10.com").when(loginActivityMock).getmEmail();
        doReturn("").when(loginActivityMock).getmPassword();
        assertEquals(loginActivityMock.attemptLogin(), false);

        doReturn("").when(loginActivityMock).getmEmail();
        doReturn("admin123").when(loginActivityMock).getmPassword();
        assertEquals(loginActivityMock.attemptLogin(), false);

        doReturn("admin(at)xp10.com").when(loginActivityMock).getmEmail();
        doReturn("admin123").when(loginActivityMock).getmPassword();
        assertEquals(loginActivityMock.attemptLogin(), false);

        doReturn("admin@xp10.com").when(loginActivityMock).getmEmail();
        doReturn("foo").when(loginActivityMock).getmPassword();
        assertEquals(loginActivityMock.attemptLogin(), false);
    }

    @Test
    public void testEmailVerifier() {
        assertEquals(true, loginActivity.isEmailValid("hallo@abc.de"));
        assertEquals(false, loginActivity.isEmailValid("halloabc.de"));
    }

    @Test
    public void testPasswordVerifier() {
        assertEquals(true, loginActivity.isPasswordValid("admin123"));
        assertEquals(false, loginActivity.isPasswordValid("admin"));
        assertEquals(false, loginActivity.isPasswordValid(""));
    }

}