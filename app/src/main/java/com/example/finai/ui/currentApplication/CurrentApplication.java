package com.example.finai.ui.currentApplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finai.FirebaseLogin;
import com.example.finai.Loan;
import com.example.finai.R;
import com.example.finai.ui.gallery.GalleryViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class CurrentApplication extends Fragment {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    TextView lOfficer, loanAmount, loanTerm, currentStatus;
    ImageView paypal, status;


    @SuppressLint("RestrictedApi")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_current_application, container, false);
        lOfficer = root.findViewById(R.id.loanOfficerText);
        loanAmount = root.findViewById(R.id.CurrentloanAmountText);
        loanTerm = root.findViewById(R.id.CurrentloanTermText);
        paypal = root.findViewById(R.id.paypal);
        status = root.findViewById(R.id.status);
        currentStatus = root.findViewById(R.id.CurrentStatus);
        String UID = auth.getUid();
        Query findNew = mRootRef.child("LoanApplications").orderByChild("userID").equalTo(UID).limitToLast(1);
            findNew.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot singleSnapshot : snapshot.getChildren()){
                        Loan loan = singleSnapshot.getValue(Loan.class);
                        lOfficer.setText(loan.getLoanOfficer());
                        loanAmount.setText(loan.getLoanAmount());
                        loanTerm.setText(loan.getLoanTerm());;
                        if(loan.getLoanStatus().equals("Pre Approved")) {
                            currentStatus.setText(loan.getLoanStatus());
                            status.setImageResource(R.drawable.check);
                        } else if(loan.getLoanStatus().equals("Rejected")) {
                            currentStatus.setText(loan.getLoanStatus());
                            status.setImageResource(R.drawable.cancel);
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {


                }
            });

        return root;

    }
}
