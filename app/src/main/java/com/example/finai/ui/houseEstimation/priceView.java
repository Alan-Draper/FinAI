package com.example.finai.ui.houseEstimation;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finai.R;
import com.example.finai.objects.HousePrice;
import com.example.finai.objects.Loan;
import com.example.finai.objects.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class priceView extends Fragment {
    TextView price;
    ImageView loanPic;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mHouseRef = mRootRef.child("HousePriceEstimations");


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_price_view, container, false);
        price = root.findViewById(R.id.costLabel);
        loanPic = root.findViewById(R.id.loanPic);

        loanPic.setOnClickListener(v -> {
            Navigation.findNavController(root).navigate(R.id.action_priceView_to_loanApplication);
        });

        price.setText(getArguments().getString("housePrice"));



        return root;
    }

}
