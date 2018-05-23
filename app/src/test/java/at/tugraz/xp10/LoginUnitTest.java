package at.tugraz.xp10;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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

import java.util.concurrent.Executor;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(JUnit4.class)
@PrepareForTest({ FirebaseAuth.class})
public class LoginUnitTest {

    LoginActivity loginActivity;
    private FirebaseAuth mockedFirebaseAuth;
    private FirebaseUser mockedUser;

    @Before
    public void before() {
        mockedFirebaseAuth = Mockito.mock(FirebaseAuth.class);
        mockedUser = Mockito.mock(FirebaseUser.class);

        PowerMockito.mockStatic(FirebaseAuth.class);
        when(FirebaseAuth.getInstance()).thenReturn(mockedFirebaseAuth);
        loginActivity = new LoginActivity();
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
    public void signIn() {
        when(mockedFirebaseAuth.signInWithEmailAndPassword(anyString(), anyString())).thenReturn(new Task<AuthResult>() {
            @Override
            public boolean isComplete() {
                return false;
            }

            @Override
            public boolean isSuccessful() {
                return false;
            }

            @Override
            public AuthResult getResult() {
                return null;
            }

            @Override
            public <X extends Throwable> AuthResult getResult(@NonNull Class<X> aClass) throws X {
                return null;
            }

            @Nullable
            @Override
            public Exception getException() {
                return null;
            }

            @NonNull
            @Override
            public Task<AuthResult> addOnSuccessListener(@NonNull OnSuccessListener<? super AuthResult> onSuccessListener) {
                return null;
            }

            @NonNull
            @Override
            public Task<AuthResult> addOnSuccessListener(@NonNull Executor executor, @NonNull OnSuccessListener<? super AuthResult> onSuccessListener) {
                return null;
            }

            @NonNull
            @Override
            public Task<AuthResult> addOnSuccessListener(@NonNull Activity activity, @NonNull OnSuccessListener<? super AuthResult> onSuccessListener) {
                return null;
            }

            @NonNull
            @Override
            public Task<AuthResult> addOnFailureListener(@NonNull OnFailureListener onFailureListener) {
                return null;
            }

            @NonNull
            @Override
            public Task<AuthResult> addOnFailureListener(@NonNull Executor executor, @NonNull OnFailureListener onFailureListener) {
                return null;
            }

            @NonNull
            @Override
            public Task<AuthResult> addOnFailureListener(@NonNull Activity activity, @NonNull OnFailureListener onFailureListener) {
                return null;
            }
        });
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