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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class accountDetailsFragment extends Fragment {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference userRef = mRootRef.child("users");
    DatabaseReference cUser = userRef.child(auth.getUid());
    ArrayList<User> ul = new ArrayList<>();

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
        employmentBox = root.findViewById(R.id.employmentStatusBox);;
       // User u = getUser();
        //System.out.println(u.getGender());
        //genderBox.setSelection(checkStrings(getString("gender"), getResources().getStringArray(R.array.gender)));
        //updates user in database
        updateUser.setOnClickListener(v-> {
                cUser.child("gender").setValue(genderBox.getSelectedItem().toString());
                cUser.child("maritalStatus").setValue(maritalStatusBox.getSelectedItem().toString());
                cUser.child("dependants").setValue(dependantsBox.getSelectedItem().toString());
                cUser.child("education").setValue(educationBox.getSelectedItem().toString());
                cUser.child("employmentStatus").setValue(employmentBox.getSelectedItem().toString());
        });
        return root;




    }

/*not working, for next sprint will add the fields being filled with current values from database on create
    public User getUser() {
        Query findNew = cUser;
        findNew.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    User u = child.getValue(User.class);
                    System.out.println(u.getGender());
                    ul.add(u);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
        return ul.get(0);
    }*/
}