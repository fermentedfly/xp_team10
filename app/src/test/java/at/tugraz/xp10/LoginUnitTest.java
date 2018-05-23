package at.tugraz.xp10;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(JUnit4.class)
@PrepareForTest({ FirebaseAuth.class})
public class LoginUnitTest {

    LoginActivity loginActivity;
    private FirebaseAuth mockedFirebaseAuth;

    @Before
    public void before() {
        mockedFirebaseAuth = Mockito.mock(FirebaseAuth.class);

        PowerMockito.mockStatic(FirebaseAuth.class);
        when(FirebaseAuth.getInstance()).thenReturn(mockedFirebaseAuth);
        loginActivity = new LoginActivity();
    }

    @Test
    public void testLoggedInUser() {
        when(mockedFirebaseAuth.getCurrentUser()).thenReturn(null);
        assertEquals(false, loginActivity.isUserLoggedIn());

        // Mock User
        FirebaseUser mockedUser = Mockito.mock(FirebaseUser.class);
        when(mockedFirebaseAuth.getCurrentUser()).thenReturn(mockedUser);
        assertEquals(true, loginActivity.isUserLoggedIn());
    }

    @Test
    public void signIn() {
        when(mockedFirebaseAuth.signInWithEmailAndPassword("hallo@hallo.at", "geheim")).thenReturn(null);
        loginActivity.signInWithUserAndPassword("hallo@hallo.at", "geheim");
    }


    //TODO: ask if tests are necessary
    @Test
    public void loginFail() throws Exception{

    }
    @Test
    public void loginSuccess() throws Exception {
    }
    @Test
    public void pushRegisterButton() throws Exception {

    }

    @Test
    public void testEmailVerifier() {
        boolean booleanReturn = loginActivity.isEmailValid("hallo@abc.de");
        assertEquals(true, booleanReturn);

        booleanReturn = loginActivity.isEmailValid("halloabc.de");
        assertEquals(false, booleanReturn);
    }

}