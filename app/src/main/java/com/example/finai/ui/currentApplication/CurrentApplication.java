package com.example.finai.ui.currentApplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finai.objects.Loan;
import com.example.finai.R;
import com.example.finai.objects.LoanOfficerApplications;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class CurrentApplication extends Fragment {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference loanOfficerRef = mRootRef.child("loanOfficer");
    TextView lOfficer, loanAmount, loanTerm, currentStatus;
    ImageView paypal, status, phone;


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
        phone = root.findViewById(R.id.phoneCallImage);
        String UID = auth.getUid();
        Query findNew = mRootRef.child("LoanApplications").orderByChild("userID").equalTo(UID).limitToLast(1);
            findNew.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot singleSnapshot : snapshot.getChildren()){
                        Loan loan = singleSnapshot.getValue(Loan.class);
                        findLoanOfficerName(loan.getLoanOfficer(),lOfficer, phone);
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

    public void findLoanOfficerName(String LoanID, TextView loanName, ImageView phone) {
        Query findNew = loanOfficerRef.orderByKey().equalTo(LoanID);
        findNew.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot singleSnapshot : snapshot.getChildren()){
                    LoanOfficerApplications loanoff = singleSnapshot.getValue(LoanOfficerApplications.class);
                    loanName.setText(loanoff.getUsername());
                    phone.setOnClickListener(v -> {
                        Intent dial = new Intent();
                        dial.setAction("android.intent.action.DIAL");
                        dial.setData(Uri.parse("tel:"+loanoff.getPhoneNumber()));
                        startActivity(dial);

                    })  ;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
    }
}
