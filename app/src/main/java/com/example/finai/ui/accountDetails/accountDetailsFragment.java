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
    //gets the logged in user from the database
    DatabaseReference cUser = userRef.child(auth.getUid());
    Button updateUser;
    Spinner genderBox, maritalStatusBox, dependantsBox, educationBox, employmentBox;

    @SuppressLint("RestrictedApi")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        updateUser = root.findViewById(R.id.button2);
        genderBox = root.findViewById(R.id.genderText);
        getUser();
        maritalStatusBox = root.findViewById(R.id.marital_Status_Box);
        dependantsBox = root.findViewById(R.id.dependants_box);
        educationBox = root.findViewById(R.id.educated_box);
        employmentBox = root.findViewById(R.id.employmentStatusBox);;
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

    public void getUser() {
        Query findNew = cUser;
        findNew.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //for (DataSnapshot child : snapshot.getValue()) {
                    User u = snapshot.getValue(User.class);
                    if(!u.getGender().equals("Male")){
                        genderBox.setSelection(1);
                    } else {
                        genderBox.setSelection(0);
                    }
                    if(!u.getMaritalStatus().equals("Single")){
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
                    if(!u.getEmploymentStatus().equals("Employed")){
                        employmentBox.setSelection(1);
                    } else {
                        employmentBox.setSelection(0);
                    }
                    if(!u.getEducation().equals("Graduated")){
                        genderBox.setSelection(1);
                    } else {
                        genderBox.setSelection(0);
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
    }
}