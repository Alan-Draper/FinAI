package com.example.finai;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.example.finai.objects.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    String username, emailAddress;
    TextView name, email;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.my_account, R.id.loanApplication, R.id.houseEstimation)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Collecting the currently logged in users details
        Intent getuser = getIntent();
        User cUser = getuser.getParcelableExtra("CurrentUser");
        View headerView = navigationView.getHeaderView(0);
        name = headerView.findViewById(R.id.nameBox);
        email = headerView.findViewById(R.id.emailBox);
        username = cUser.getUsername();
        emailAddress = cUser.getEmail();
        name.setText(username);
        email.setText(emailAddress);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    //method for signing out on the nav bar
    public void signOut(MenuItem item) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext() ,FirebaseLogin.class));
    }

    public void ringFinAI(MenuItem item) {
        //method to allow calling of the bank, no known number but functionality is there
        Intent dial = new Intent();
        dial.setAction("android.intent.action.DIAL");
        dial.setData(Uri.parse("tel:00000000000"));
        startActivity(dial);
    }

    public void emailFinai(MenuItem item) {
        //intent to allow users to email the bank directly
        String mailTo="finai@finai.com";
        Intent email_intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",mailTo, null));
        email_intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject text here");
        email_intent.putExtra(android.content.Intent.EXTRA_TEXT,"Body text here");
        startActivity(Intent.createChooser(email_intent, "Choose an Email client :"));
        }
}