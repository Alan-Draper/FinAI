package com.example.finai.ui.accountDetails;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.finai.DbHelper;
import com.example.finai.R;
import com.example.finai.objects.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class accountDetailsFragment extends Fragment {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference userRef = mRootRef.child("users");
    DatabaseReference cUser = userRef.child(auth.getUid());
    Button updateUser;
    Spinner genderBox, maritalStatusBox, dependantsBox, educationBox, employmentBox;

    @SuppressLint("RestrictedApi")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        updateUser = root.findViewById(R.id.button2);
        genderBox = root.findViewById(R.id.genderText);
        maritalStatusBox = root.findViewById(R.id.marital_Status_Box);
        dependantsBox = root.findViewById(R.id.dependants_box);
        educationBox = root.findViewById(R.id.educated_box);
        employmentBox = root.findViewById(R.id.employmentStatusBox);
        if(DbHelper.checkUser(cUser) == true) {
            DbHelper.getUser(cUser, genderBox, maritalStatusBox, dependantsBox, educationBox, employmentBox);
        }
        //updates the user with the following values
        updateUser.setOnClickListener(v-> {
                cUser.child("gender").setValue(genderBox.getSelectedItem().toString());
                cUser.child("maritalStatus").setValue(maritalStatusBox.getSelectedItem().toString());
                cUser.child("dependants").setValue(dependantsBox.getSelectedItem().toString());
                cUser.child("education").setValue(educationBox.getSelectedItem().toString());
                cUser.child("employmentStatus").setValue(employmentBox.getSelectedItem().toString());
        });
        return root;




    }


}