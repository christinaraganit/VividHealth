package ca.bcit.vividhealth;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    private FirebaseAuth mAuth;
    private Object FirebaseAuthInvalidUserException;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private FirebaseUser firebaseUser;
    private String TAG = "";
    LinearLayout home_layout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Checks if user is logged in, if not returns to the login page
        try{
            throw Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).reload().getException());
        } catch (Exception e) {
            mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser.reload() == FirebaseAuthInvalidUserException){
                System.out.println("no user");
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }

        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final TextView greeting = findViewById(R.id.greeting);

        firebaseUser = mAuth.getCurrentUser();
        database.collection("Users").document(firebaseUser.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            greeting.setText(String.format("Hello, %s", document.get("Name").toString()));
                            greeting.setVisibility(View.VISIBLE);

                        }
                    }
                });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.nav_open_drawer,
                R.string.nav_close_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // Firebase Auth


        // Placing user reminders on the home screen
        home_layout = findViewById(R.id.home_layout);
        loadReminders();



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        Fragment fragment = null;
        Intent intent = null;

        switch(id) {
            case R.id.nav_home:
                intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_profile:
                intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_reminders:
//                intent = new Intent(this, ReminderActivity.class);
                break;
            case R.id.nav_workouts:
//                intent = new Intent(this, WorkoutActivity.class);
                break;
            case R.id.nav_about:
//                intent = new Intent(this, AboutActivity.class);
                break;
            case R.id.nav_feedback:
//                intent = new Intent(this, FeedbackActivity.class);
                break;
            case R.id.nav_toggle_mode:
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onNewReminder(View v) {
        Intent intent = new Intent(this, NewReminderActivity.class);
        startActivity(intent);
    }

    public void onWorkOutPlan(View v) {
        Intent i = new Intent(this, WorkoutPlan.class);
        startActivity(i);
    }

    public void onSittingBreak(View v) {
        Intent i = new Intent(this, SittingBreak.class);
        startActivity(i);
    }

    public void onGetActive(View v) {
        Intent i = new Intent(this, GetActive.class);
        startActivity(i);
    }

    public void onMorningCompliment(View v) {
        Intent i = new Intent(this, MorningCompliment.class);
        startActivity(i);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // on Start check if user logged in
        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                if (currentUser == null){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    public void onProfileClick(View view) {
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(intent);
    }

    public void loadReminders(){
        database.collection("Users").document(firebaseUser.getUid()).collection("Reminders").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                LinearLayout.LayoutParams cardlayoutParams = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                                CardView cardView = new CardView(getApplicationContext());

                                LinearLayout linearLayout = new LinearLayout(getApplicationContext());
                                linearLayout.setLayoutParams(cardlayoutParams);
                                linearLayout.setOrientation(LinearLayout.VERTICAL);




                                TextView title = new TextView(getApplicationContext());
                                TextView repeat = new TextView(getApplicationContext());
                                TextView time = new TextView(getApplicationContext());
                                Button editBtn = new Button(getApplicationContext());

                                editBtn.setText("EDIT THIS REMINDER");
                                editBtn.setBackgroundColor(getColor(R.color.colorPrimary));
                                LinearLayout.LayoutParams btnlayoutParams = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                btnlayoutParams.setMargins(16,0,16,16);
                                editBtn.setLayoutParams(btnlayoutParams);
                                editBtn.setTextColor(Color.WHITE);

                                editBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                    }
                                });



                                cardlayoutParams.setMargins(32, 24, 32, 24);
                                cardView.setLayoutParams(cardlayoutParams);
                                cardView.setCardBackgroundColor(getColor(R.color.colorAccent));
                                cardView.setCardElevation(8);
                                cardView.setRadius(8);

                                title.setText(document.getData().get("title").toString());
                                title.setTextSize(20);
                                title.setTextColor(Color.BLACK);
                                title.setTypeface(Typeface.DEFAULT_BOLD);
                                repeat.setText("Remind me " + document.getData().get("repeat").toString());

                                String time_string = "At " + document.getData().get("time_hour").toString()
                                        + ":" + document.getData().get("time_minute");

                                time.setText(time_string);

                                linearLayout.addView(title);
                                linearLayout.addView(repeat);
                                linearLayout.addView(time);

                                linearLayout.addView(editBtn);

                                cardView.addView(linearLayout);

                                home_layout.addView(cardView);

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


}