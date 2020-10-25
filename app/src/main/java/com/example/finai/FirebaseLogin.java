package com.example.finai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

public class FirebaseLogin extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private static final String TAG = "";
    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_login);
        startActivityForResult(
                // Get an instance of AuthUI based on the default app
                AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(Arrays.asList(
                        new AuthUI.IdpConfig.GoogleBuilder().build(),
                        new AuthUI.IdpConfig.EmailBuilder().build()))
                        .setTheme(R.style.LoginTheme)
                        .setIsSmartLockEnabled(false)
                        .setLogo(R.drawable.logo).build(), RC_SIGN_IN);
    }


    private User writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);
        mRootRef.child("users").child(userId).setValue(user);
        return user;
    }


    public void showHome(User user) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("CurrentUser", user);
        startActivity(intent);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
                // Successfully signed in
                if (resultCode == RESULT_OK) {
                    showHome(this.writeNewUser(auth.getUid(), auth.getCurrentUser().getDisplayName(), auth.getCurrentUser().getEmail()));
                    finish();
                } else {
                    // Sign in failed
                    if (response == null) {
                        // User pressed back button
                        Toast.makeText(FirebaseLogin.this, R.string.sign_in_cancelled, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                        Toast.makeText(FirebaseLogin.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Toast.makeText(FirebaseLogin.this, R.string.unknown_error, Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Sign-in error: ", response.getError());
                }
            }
        }
    }