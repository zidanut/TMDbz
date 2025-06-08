package com.example.saz_ppb_prak7.presentation.ui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;

import androidx.test.core.app.ApplicationProvider;

import com.example.saz_ppb_prak7.data.repository.LoginRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;

@Config(sdk = 33)
@RunWith(RobolectricTestRunner.class)
public class LoginTest {
    @Mock
    private LoginRepository mockLoginRepository;

    @Mock
    private Task<AuthResult> mockAuthResultTask;

    private login loginActivity;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApplicationId("1:1234567890:android:abcdef")
                .setApiKey("dummy_api_key")
                .setProjectId("dummy_project_id")
                .build();

        try {
            FirebaseApp.initializeApp(context, options);
        } catch (IllegalStateException e) {

        }
        MockitoAnnotations.openMocks(this);

        loginActivity = Robolectric.buildActivity(login.class).create().get();

        loginActivity.loginRepository = mockLoginRepository;
    }

    @Test
    public void loginUser_whenSuccess_shouldStartMainActivity() {
        String email = "test@example.com";
        String password = "password123";

        loginActivity.etUsernameOrEmail.setText(email);
        loginActivity.etPassword.setText(password);

        when(mockLoginRepository.loginWithEmail(email, password))
                .thenReturn(mockAuthResultTask);

        doAnswer(invocation -> {
            OnCompleteListener<AuthResult> listener = invocation.getArgument(0);
            listener.onComplete(mockAuthResultTask);
            return null;
        }).when(mockAuthResultTask).addOnCompleteListener(any(OnCompleteListener.class));

        when(mockAuthResultTask.isSuccessful()).thenReturn(true);

        loginActivity.loginUser();

        Shadows.shadowOf(Looper.getMainLooper()).idle();

        Intent startedIntent = Shadows.shadowOf(loginActivity).getNextStartedActivity();
        assertNotNull(startedIntent);
        assertEquals(MainActivity.class.getCanonicalName(), startedIntent.getComponent().getClassName());
    }

    @Test
    public void loginUser_whenEmptyFields_shouldShowToastAndNotLogin() {
        loginActivity.etUsernameOrEmail.setText("");
        loginActivity.etPassword.setText("");

        loginActivity.loginUser();

        verify(mockLoginRepository, never()).loginWithEmail(anyString(), anyString());
    }
}
