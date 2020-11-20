package com.example.finai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.finai.objects.User;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class FirebaseLogin extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private static final String TAG = "";
    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    ArrayList<User> usertest = new ArrayList<>();
    Boolean checkUser = false;
    User checkedUser;

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
        checkExists();
    }


    private User writeNewUser(String userId, String name, String email) {
        User user = new User(userId, name, email);
        mRootRef.child("users").child(userId).setValue(user);
        return user;
    }


    public void showHome(User user) {
        Intent intent = new Intent(this, MainActivity.class);
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
                System.out.println("test arraylist size = " + usertest.size());
                for (User s : usertest){
                    if (auth.getCurrentUser().getEmail().equals(s.getEmail())) {
                        checkUser = true;
                        checkedUser = s;
                    }
                }

                if (!checkUser) {
                    showHome(this.writeNewUser(auth.getCurrentUser().getUid(), auth.getCurrentUser().getDisplayName(), auth.getCurrentUser().getEmail()));
                } else {
                    showHome(checkedUser);
                    //finish();
                }
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

    public void checkExists() {
        Query findNew = mRootRef.child("users").orderByChild("username");
        findNew.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap : snapshot.getChildren()) {
                    User l = snap.getValue(User.class);
                    System.out.println(l);
                    usertest.add(l);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }
}