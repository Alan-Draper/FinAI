package com.example.finai.ui.loan;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.finai.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager;
import com.google.firebase.ml.custom.FirebaseCustomRemoteModel;

import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class LoanApplication extends Fragment {

    private LoanApplicationViewModel mViewModel;
    Interpreter interpreter;
    Button submitLoanApplication;
    Spinner genderBox, maritalStatusBox, dependantsBox, educationBox,employmentBox, locationBox;
    TextView incomeBox, coIncomeBox, loanAmountBox, loanTermBox, creditScoreBox;
    String gender, maritalStatus,dependants,education,employment,income,coIncome,loanAmount,loanTerm,creditScore, location;

    public static LoanApplication newInstance() {
        return new LoanApplication();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.loan_application_fragment, container, false);

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

        submitLoanApplication.setOnClickListener(v -> {
            if (genderBox.getSelectedItem().toString() == "Male") {
                gender = "1";
            } else {
                gender = "0";
            }

            if (maritalStatusBox.getSelectedItem().toString() == "Married") {
                maritalStatus = "1";
            } else {
                maritalStatus = "0";
            }

            if (dependantsBox.getSelectedItem().toString() == "3+") {
                dependants = "3";
            } else {
                dependants = dependantsBox.getSelectedItem().toString();
            }

            if (employmentBox.getSelectedItem().toString() == "Employed") {
                employment = "0";
            } else {
                employment = "1";
            }

            if (educationBox.getSelectedItem().toString() == "Graduated") {
                 education = "1";
            } else {
                education = "0";
            }
            income = incomeBox.getText().toString();
            coIncome = coIncomeBox.getText().toString();
            loanAmount = loanAmountBox.getText().toString();
            loanTerm = loanTermBox.getText().toString();
            int creditscoreValue = Integer.parseInt(creditScoreBox.getText().toString());
            if (creditscoreValue >= 620) {
                creditScore = "1";
            } else {
                creditScore = "0";
            }

            if (locationBox.getSelectedItem().toString() == "Urban") {
                location = "2";
            } else if(locationBox.getSelectedItem().toString() == "Rural"){
                location = "0";
            } else {
                location = "1";
            }

            ByteBuffer data = ByteBuffer.allocateDirect(44).order(ByteOrder.nativeOrder());
            int bufferSize = 1000 * java.lang.Float.SIZE / java.lang.Byte.SIZE;
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
            Log.i(TAG, String.format("%s: %1.4f", "probability", probability));

        System.out.println(probability);

        if (probability <= 0.55) {

            Navigation.findNavController(root).navigate(R.id.action_loanApplication_to_rejected);
        } else {
            Navigation.findNavController(root).navigate(R.id.action_loanApplication_to_approved);

        }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(LoanApplicationViewModel.class);
        // TODO: Use the ViewModel
    }



}