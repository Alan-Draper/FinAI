package com.example.finai.ui.loan;

import androidx.annotation.RequiresApi;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.finai.objects.Loan;
import com.example.finai.R;
import com.example.finai.objects.LoanOfficerApplications;
import com.example.finai.objects.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager;
import com.google.firebase.ml.custom.FirebaseCustomRemoteModel;

import org.tensorflow.lite.Interpreter;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

public class LoanApplication extends Fragment {
    //interpreter for using the algorithm
    Interpreter interpreter;
    Button submitLoanApplication;
    Spinner genderBox, maritalStatusBox, dependantsBox, educationBox,employmentBox, locationBox;
    TextView incomeBox, coIncomeBox, loanAmountBox, loanTermBox, creditScoreBox;
    String gender, maritalStatus,dependants,education,employment,income,coIncome,loanAmount,loanTerm,creditScore, location, loanId, loanStatus;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference loanRef = mRootRef.child("LoanApplications");
    DatabaseReference userRef = mRootRef.child("users");
    DatabaseReference loanOfficerRef = mRootRef.child("loanOfficer");
    ArrayList<LoanOfficerApplications> lList = new ArrayList<>();
    ArrayList<User> cUser = new ArrayList<>();




    public static LoanApplication newInstance() {
        return new LoanApplication();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.loan_application_fragment, container, false);
        //grabs the model from firebase
        FirebaseCustomRemoteModel remoteModel =
                new FirebaseCustomRemoteModel.Builder("LoanTest").build();
        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                .requireWifi()
                .build();
        FirebaseModelManager.getInstance().download(remoteModel, conditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void v) {
                        FirebaseModelManager.getInstance().getLatestModelFile(remoteModel)
                                .addOnCompleteListener(task -> {
                                    File modelFile = task.getResult();
                                    if (modelFile != null) {
                                        interpreter = new Interpreter(modelFile);
                                    }
                                });
                    }
                });

        //assigning variables to all objects on the view
        lList = populateLoanList();
        genderBox = root.findViewById(R.id.genderText);
        maritalStatusBox = root.findViewById(R.id.marital_Status_Box);
        dependantsBox = root.findViewById(R.id.dependants_box);
        educationBox = root.findViewById(R.id.educated_box);
        employmentBox = root.findViewById(R.id.employmentStatusBox);
        incomeBox = root.findViewById(R.id.incomeText);
        coIncomeBox = root.findViewById(R.id.coIncomeText);
        loanAmountBox = root.findViewById(R.id.loanAmountText);
        loanTermBox = root.findViewById(R.id.loanTermText);
        creditScoreBox = root.findViewById(R.id.creditScoreText);
        locationBox = root.findViewById(R.id.location_box);
        submitLoanApplication = root.findViewById(R.id.submitLoan);
        cUser = getCurrentUser();

        //ensuring that all variables suit the input needed for the algorithm
        //needs to be clearer for end user in next version
        submitLoanApplication.setOnClickListener(v -> {

            if (genderBox.getSelectedItem().toString().equals("Male")) {
                gender = "1";
            } else {
                gender = "0";
            }

            if (maritalStatusBox.getSelectedItem().toString().equals("Married")) {
                maritalStatus = "1";
            } else {
                maritalStatus = "0";
            }

            if (dependantsBox.getSelectedItem().toString().equals("3+")) {
                dependants = "3";
            } else {
                dependants = dependantsBox.getSelectedItem().toString();
            }

            if (employmentBox.getSelectedItem().toString().equals("Employed")) {
                employment = "0";
            } else {
                employment = "1";
            }

            if (educationBox.getSelectedItem().toString().equals("Graduated")) {
                 education = "1";
            } else {
                education = "0";
            }
            income = incomeBox.getText().toString();
            coIncome = coIncomeBox.getText().toString();
            loanAmount = loanAmountBox.getText().toString();
            loanTerm = loanTermBox.getText().toString();
            //availability of a loan in seattle is not possible with a credit score of under 620
            int creditscoreValue = Integer.parseInt(creditScoreBox.getText().toString());
            if (creditscoreValue >= 620) {
                creditScore = "1";
            } else {
                creditScore = "0";
            }

            if (locationBox.getSelectedItem().toString().equals("Urban")) {
                location = "2";
            } else if(locationBox.getSelectedItem().toString().equals("Rural")){
                location = "0";
            } else {
                location = "1";
            }
            /*creating a bytebuffer for the data in and out of the interpreter
            //data shape as follows
            /*1 input(s):
                [ 1 11] <class 'numpy.float32'>

              1 output(s):
                [1 1] <class 'numpy.float32'> */

            ByteBuffer data = ByteBuffer.allocateDirect(44).order(ByteOrder.nativeOrder());
            int bufferSize = 1000 * Float.SIZE / Byte.SIZE;
            ByteBuffer dataout = ByteBuffer.allocateDirect(bufferSize).order(ByteOrder.nativeOrder());
            data.putFloat(Float.parseFloat(gender));
            data.putFloat(Float.parseFloat(maritalStatus));
            data.putFloat(Float.parseFloat(dependants));
            data.putFloat(Float.parseFloat(education));
            data.putFloat(Float.parseFloat(employment));
            data.putFloat(Float.parseFloat(income));
            data.putFloat(Float.parseFloat(coIncome));
            data.putFloat(Float.parseFloat(loanAmount));
            data.putFloat(Float.parseFloat(loanTerm));
            data.putFloat(Float.parseFloat(creditScore));
            data.putFloat(Float.parseFloat(location));

        interpreter.run(data, dataout);
            dataout.rewind();
            FloatBuffer probabilities = dataout.asFloatBuffer();
            float probability = probabilities.get();
            System.out.println(probability);

        //using the probability from the interpreter to push the loan application with the status to the database
        if (probability <= 0.5) {
            //if less then 0.5 probability of loan then it is automatically rejected
            //current acuarracy of loan model is 72 percent, will be imroved in later version
            loanStatus = "Rejected";
            writeNewLoan(loanRef.push().getKey(), auth.getUid(), genderBox.getSelectedItem().toString(),
                    maritalStatusBox.getSelectedItem().toString(),
                    dependantsBox.getSelectedItem().toString(),
                    employmentBox.getSelectedItem().toString(),
                    educationBox.getSelectedItem().toString(),
                    income,coIncome,loanAmount,loanTerm,creditScore, loanStatus, locationBox.getSelectedItem().toString(), cUser.get(0).getLoanOfficer());
            Navigation.findNavController(root).navigate(R.id.action_loanApplication_to_rejected);
        } else {
            loanStatus = "Pre Approved";
            //assigns a new loan officer using the random function below or assigns the same loan officer if a previous application exists
            System.out.println(cUser.size());
            if(cUser.get(0).getLoanOfficer().equals("none")) {
                String loanOfficer = findLoanOfficer();
                writeNewLoan(loanRef.push().getKey(), auth.getUid(), genderBox.getSelectedItem().toString(),
                        maritalStatusBox.getSelectedItem().toString(),
                        dependantsBox.getSelectedItem().toString(),
                        employmentBox.getSelectedItem().toString(),
                        educationBox.getSelectedItem().toString(),
                        income, coIncome, loanAmount, loanTerm, creditScore, loanStatus, locationBox.getSelectedItem().toString(), loanOfficer);
                System.out.println(loanOfficer);
                Navigation.findNavController(root).navigate(R.id.action_loanApplication_to_approved);
            } else {
                writeNewLoan(loanRef.push().getKey(), auth.getUid(), genderBox.getSelectedItem().toString(),
                        maritalStatusBox.getSelectedItem().toString(),
                        dependantsBox.getSelectedItem().toString(),
                        employmentBox.getSelectedItem().toString(),
                        educationBox.getSelectedItem().toString(),
                        income, coIncome, loanAmount, loanTerm, creditScore, loanStatus, locationBox.getSelectedItem().toString(), cUser.get(0).getLoanOfficer());
                getUserLoanOfficer(cUser.get(0));
                System.out.println(cUser.get(0).getLoanOfficer());
                Navigation.findNavController(root).navigate(R.id.action_loanApplication_to_approved);
            }
        }
        });

        return root;
    }

    private void writeNewLoan(String loanID, String userID, String gender, String maritalStatus, String dependants, String education, String employment, String income, String coIncome, String loanAmount, String loanTerm, String creditScore, String status, String location, String loanOfficer) {
        //writes loan to database and adds loan officer to users record
        Loan loan = new Loan(loanID,userID,gender,maritalStatus,dependants,education,employment,income,coIncome,loanAmount,loanTerm,creditScore,status, location, loanOfficer);
        loanRef.child(loanID).setValue(loan);
        userRef.child(auth.getCurrentUser().getUid()).child("loanOfficer").setValue(loanOfficer);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public String findLoanOfficer() {
        /*method to find the loan officer and assign it to the loan
        there is an openLoans record for each loan officer
        this method finds the loan officer with the fewest open loans and assigns the loan to the officer
         */

        ArrayList<LoanOfficerApplications> numApps = new ArrayList<>();
        LoanOfficerApplications lowest = new LoanOfficerApplications();
        int smallest = 170000000;
        int countSmallest = 0;
        for (LoanOfficerApplications i : lList) {
            if (i.getOpenLoans() < smallest) {
                smallest = Math.toIntExact(i.getOpenLoans());
                lowest = new LoanOfficerApplications(i.getLoanOfficerID(),i.getOpenLoans());
                countSmallest = 0;
                numApps.clear();
            } else if (i.getOpenLoans() == smallest) {
                countSmallest++;
                lowest = null;
                numApps.add(i);
            }
        }
        //if there is more then one loan officer with the same amount of openloans then it assigns it to one of them randomly
        if (countSmallest > 0) {
            int num = (int) (Math.random() * numApps.size());
            DatabaseReference openItems = FirebaseDatabase.getInstance().getReference("loanOfficer").child(numApps.get(num).getLoanOfficerID());
            int openItemsInt = Math.toIntExact(numApps.get(num).getOpenLoans());
            openItemsInt = openItemsInt+1;
            openItems.child("openLoans").setValue(openItemsInt);
            return numApps.get(num).getLoanOfficerID();
        } else {
            assert lowest != null;
            String loanOfficerID = lowest.getLoanOfficerID();
            DatabaseReference openItems = FirebaseDatabase.getInstance().getReference("loanOfficer").child(lowest.getLoanOfficerID());
            int openItemsInt = Math.toIntExact(lowest.getOpenLoans());
            openItemsInt = openItemsInt+1;
            openItems.child("openLoans").setValue(openItemsInt);
            return loanOfficerID;
        }
    }

        public ArrayList<LoanOfficerApplications> populateLoanList () {
            //returns a list of loan officers from the database to get their open loans
            String UID = auth.getUid();
            Query findNew = loanOfficerRef.orderByKey();
            findNew.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        LoanOfficerApplications l = child.getValue(LoanOfficerApplications.class);
                        String parentKey = child.getKey();
                        assert l != null;
                        LoanOfficerApplications l2 = new LoanOfficerApplications(parentKey, l.getOpenLoans());
                        lList.add(l2);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {


                }
            });

            return lList;
        }

        public ArrayList<User> getCurrentUser() {
            String UID = auth.getCurrentUser().getUid();
            Query findNew = userRef.child(UID);
            findNew.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                        User l = snapshot.getValue(User.class);
                        System.out.println(l.getEmail());
                        cUser.add(l);
                        }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            return cUser;
        }

    public void getUserLoanOfficer(User user) {
        DatabaseReference currentLoanOfficer = FirebaseDatabase.getInstance().getReference("loanOfficer").child(user.getLoanOfficer());
        String loanOfficerID = user.getLoanOfficer();
        Query findNew = loanOfficerRef.child(loanOfficerID);
        findNew.addListenerForSingleValueEvent(new ValueEventListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    LoanOfficerApplications l = snapshot.getValue(LoanOfficerApplications.class);
                    int openLoans = Math.toIntExact(l.getOpenLoans());
                    openLoans = openLoans+1;
                    currentLoanOfficer.child("openLoans").setValue(openLoans);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
    }

}