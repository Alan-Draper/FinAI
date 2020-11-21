package com.example.finai.ui.appRating;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.finai.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RatingFragment extends Fragment {
    RatingBar ratingBar;
    Button ratingButton;
    DatabaseReference userRating;
    float userRatingValue;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_rating, container, false);

        String UID = auth.getUid();
        userRating = mRootRef.child("users").child(UID).child("appRating");
        userRating.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    userRatingValue = snapshot.getValue(Float.class);
                }
                    ratingBar = root.findViewById(R.id.ratingBar);
                    ratingBar.setRating(userRatingValue);
                    String toastMessage = "Thank you for rating us " + String.valueOf(userRatingValue) + " stars!";
                    Toast.makeText(getActivity(), toastMessage, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        ratingButton = root.findViewById(R.id.ratingButton);
        ratingButton.setOnClickListener(arg0 -> {
            userRating.setValue(ratingBar.getRating());
        });

        return root;
    }
}