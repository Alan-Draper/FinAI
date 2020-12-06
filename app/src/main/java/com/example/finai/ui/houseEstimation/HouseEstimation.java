package com.example.finai.ui.houseEstimation;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.finai.R;
import com.example.finai.objects.HousePrice;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager;
import com.google.firebase.ml.custom.FirebaseCustomRemoteModel;

import org.tensorflow.lite.Interpreter;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.content.ContentValues.TAG;

public class HouseEstimation extends Fragment {

    Interpreter interpreter;
    Button submitHouseApplication;
    TextView sqFtLiving, latitude, sqFtAbove, sqFtBasement, longitude, bedrooms, bathrooms, yearBuilt, floors, zipcode, sqFtLiving15;
    Spinner waterfrontBox, grade, view, condition;
    String waterfront;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference houseRef = mRootRef.child("HousePriceEstimations");
    FirebaseAuth auth = FirebaseAuth.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_house_prices, container, false);
       FirebaseCustomRemoteModel remoteModel =
                new FirebaseCustomRemoteModel.Builder("HousePriceTest").build();
        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
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

        sqFtLiving = root.findViewById(R.id.squareFootLivingText);
        latitude = root.findViewById(R.id.latitudeText);
        latitude.setOnClickListener(v -> Toast.makeText(getActivity(), "Latitude must be between 47.1555 and 47.7780", Toast.LENGTH_LONG).show());
        sqFtAbove = root.findViewById(R.id.squareFootAboveText);
        sqFtBasement = root.findViewById(R.id.squareFootBasementText);
        waterfrontBox = root.findViewById(R.id.waterfront_box);
        grade = root.findViewById(R.id.grade_box);
        longitude = root.findViewById(R.id.longitudeText);
        longitude.setOnClickListener(v -> Toast.makeText(getActivity(), "Latitude must be between -121.315 and -122.515", Toast.LENGTH_LONG).show());
        view = root.findViewById(R.id.view_box);
        condition = root.findViewById(R.id.condition_box);
        bedrooms = root.findViewById(R.id.bedroomsText);
        bathrooms = root.findViewById(R.id.bathroomsText);
        yearBuilt = root.findViewById(R.id.yearBuiltText);
        floors =root.findViewById(R.id.floorsText);
        zipcode= root.findViewById(R.id.zipcodeText);
        zipcode.setOnClickListener(v -> Toast.makeText(getActivity(), "Zipcode must be between 98000 and 98200", Toast.LENGTH_LONG).show());
        sqFtLiving15 = root.findViewById(R.id.squareFootLiving15Text);

        submitHouseApplication = root.findViewById(R.id.submitHouseEstimation);

        submitHouseApplication.setOnClickListener(v -> {

            if (waterfrontBox.getSelectedItem().toString().equals("Yes")) {
                waterfront = "1";
            } else {
                waterfront = "0";
            }


            ByteBuffer data = ByteBuffer.allocateDirect(60).order(ByteOrder.nativeOrder());
            int bufferSize = 1000 * Float.SIZE / Byte.SIZE;
            ByteBuffer dataout = ByteBuffer.allocateDirect(bufferSize).order(ByteOrder.nativeOrder());
            data.putFloat(Float.parseFloat(sqFtLiving.getText().toString()));
            data.putFloat(Float.parseFloat(latitude.getText().toString()));
            data.putFloat(Float.parseFloat(sqFtAbove.getText().toString()));
            data.putFloat(Float.parseFloat(sqFtBasement.getText().toString()));
            data.putFloat(Float.parseFloat(waterfront));
            data.putFloat(Float.parseFloat(grade.getSelectedItem().toString()));
            data.putFloat(Float.parseFloat(longitude.getText().toString()));
            data.putFloat(Float.parseFloat(view.getSelectedItem().toString()));
            data.putFloat(Float.parseFloat(condition.getSelectedItem().toString()));
            data.putFloat(Float.parseFloat(bedrooms.getText().toString()));
            data.putFloat(Float.parseFloat(bathrooms.getText().toString()));
            data.putFloat(Float.parseFloat(yearBuilt.getText().toString()));
            data.putFloat(Float.parseFloat(floors.getText().toString()));
            data.putFloat(Float.parseFloat(zipcode.getText().toString()));
            data.putFloat(Float.parseFloat(sqFtLiving15.getText().toString()));

            interpreter.run(data, dataout);
            dataout.rewind();
            FloatBuffer costs = dataout.asFloatBuffer();
            float housePrice = costs.get();
           // Log.i(TAG, String.format("%s: %1.2f", "probability", housePrice));

            writeNewHouseApplication(houseRef.push().getKey(),
                    auth.getUid(),
                    sqFtLiving.getText().toString(),
                    latitude.getText().toString(),
                    sqFtAbove.getText().toString(),
                    sqFtBasement.getText().toString(),
                    waterfront,
                    grade.getSelectedItem().toString(),
                    longitude.getText().toString(),
                    view.getSelectedItem().toString(),
                    condition.getSelectedItem().toString(),
                    bedrooms.getText().toString(),
                    bathrooms.getText().toString(),
                    yearBuilt.getText().toString(),
                    floors.getText().toString(),
                    zipcode.getText().toString(),
                    sqFtLiving15.getText().toString(),
                    String.valueOf(housePrice)
            );
            Bundle pricePass = new Bundle();
            pricePass.putString("housePrice", String.valueOf(housePrice));
            Navigation.findNavController(root).navigate(R.id.action_houseEstimation_to_priceView,pricePass);


        });

        return root;
    }

    private void writeNewHouseApplication(String housePriceID, String userId, String sqFtLiving, String latitude, String sqFtAbove, String sqFtBasement, String waterfront, String grade, String longitude, String view, String condition, String bedrooms, String bathrooms, String yearBuilt, String floors, String zipcode, String sqFtLiving15, String price) {

        HousePrice house = new HousePrice(housePriceID, userId, sqFtLiving,latitude,sqFtAbove,sqFtBasement,waterfront,grade,longitude,view,condition,bedrooms,bathrooms,yearBuilt,floors,zipcode,sqFtLiving15, price);
        houseRef.child(housePriceID).setValue(house);
    }
}