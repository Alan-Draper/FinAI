package com.example.finai.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.example.finai.objects.Loan;
import com.example.finai.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    Button loanButton;
    Button currentApplication;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        loanButton = root.findViewById(R.id.applyForLoan);
        currentApplication = root.findViewById(R.id.currentApplication);

        loanButton.setOnClickListener(v-> {
            Navigation.findNavController(root).navigate(R.id.action_nav_home_to_loanApplication);
        });

        currentApplication.setOnClickListener(v -> {
            String UID = auth.getUid();
            Query findNew = mRootRef.child("LoanApplications").orderByChild("userID").equalTo(UID).limitToLast(1);
            findNew.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot singleSnapshot : snapshot.getChildren()){
                        Loan loan = singleSnapshot.getValue(Loan.class);
                        if(loan != null) {
                            System.out.println(loan.getLoanID());
                            Navigation.findNavController(root).navigate(R.id.action_nav_home_to_currentApplication2);
                        }
                    }
                    }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {


                }
            });
        })  ;


        return root;
    }
}