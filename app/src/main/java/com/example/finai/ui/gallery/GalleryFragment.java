package com.example.finai.ui.gallery;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.finai.FirebaseLogin;
import com.example.finai.MainActivity;
import com.example.finai.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    Button signout;

    @SuppressLint("RestrictedApi")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        signout = root.findViewById(R.id.button2);

        signout.setOnClickListener(v-> {

                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext() ,FirebaseLogin.class));
        });
        return root;




    }
}