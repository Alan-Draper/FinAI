package com.example.finai;

import android.widget.Spinner;

import androidx.annotation.NonNull;

import com.example.finai.objects.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class DbHelper {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference userRef = mRootRef.child("users");
    //gets the logged in user from the database
    DatabaseReference cUser = userRef.child(auth.getUid());

    public static boolean checkUser(DatabaseReference reference) {
        final Boolean[] test = new Boolean[1];
        test[0] = false;
        Query findNew = reference;
        findNew.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    if (data.getKey().equals("gender")) {
                        test[0] = true;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return test[0];
    }


    public static void getUser(DatabaseReference reference, Spinner genderBox, Spinner maritalStatusBox, Spinner dependantsBox, Spinner educationBox, Spinner employmentBox) {
        Query findNew = reference;
        findNew.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User u = snapshot.getValue(User.class);
                    if (!u.getGender().equals("Male")) {
                        genderBox.setSelection(1);
                    } else {
                        genderBox.setSelection(0);
                    }
                    if (!u.getMaritalStatus().equals("Single")) {
                        maritalStatusBox.setSelection(0);
                    } else {
                        maritalStatusBox.setSelection(1);
                    }
                    switch (u.getDependants()) {
                        case "0":
                            dependantsBox.setSelection(0);
                            break;
                        case "1":
                            dependantsBox.setSelection(1);
                            break;
                        case "2":
                            dependantsBox.setSelection(2);
                            break;
                        default:
                            dependantsBox.setSelection(3);
                            break;
                    }
                    if (!u.getEmploymentStatus().equals("Employed")) {
                        employmentBox.setSelection(1);
                    } else {
                        employmentBox.setSelection(0);
                    }
                    if (!u.getEducation().equals("Graduated")) {
                        educationBox.setSelection(1);
                    } else {
                        educationBox.setSelection(0);
                    }
                }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
    }
}
