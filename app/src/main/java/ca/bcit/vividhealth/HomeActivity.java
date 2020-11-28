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
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
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

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.LinearLayout.*;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    private FirebaseAuth mAuth;
    private Object FirebaseAuthInvalidUserException;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private FirebaseUser firebaseUser;
    private String TAG = "";
    LinearLayout home_layout;
    private Menu menu;

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



        drawer.bringToFront();
        drawer.requestLayout();
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
                System.out.println("Home clicked");
                intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_profile:
                System.out.println("Profile clicked");
                intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_reminders:
                intent = new Intent(this, NewReminderActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_workouts:
                System.out.println("Nav workouts clicked");
//                intent = new Intent(this, WorkoutActivity.class);
                break;
            case R.id.nav_about:
                System.out.println("About clicked");
//                intent = new Intent(this, AboutActivity.class);
                break;
            case R.id.nav_feedback:
                System.out.println("Feedback clicked");
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
        final LinearLayout home_layout = findViewById(R.id.home_layout);

        database.collection("Users").document(firebaseUser.getUid()).collection("Reminders").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                // Building all the dp values
                                int dp_8 = (int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 8, getResources()
                                                .getDisplayMetrics());

                                int dp_16 = (int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 16, getResources()
                                                .getDisplayMetrics());

                                int dp_24 = (int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                                                .getDisplayMetrics());

                                int dp_28 = (int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 28, getResources()
                                                .getDisplayMetrics());

                                int dp_32 = (int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 32, getResources()
                                                .getDisplayMetrics());

                                // Card View
                                LinearLayout.LayoutParams cardParams =
                                        new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
                                cardParams.setMargins(dp_32, dp_8, dp_32, dp_8);
                                CardView card = new CardView(getBaseContext());
                                card.setLayoutParams(cardParams);
                                card.setCardBackgroundColor(getColor(R.color.colorAccent));
                                card.setRadius(dp_8);
                                card.setCardElevation(dp_8);

                                // Linear Layout
                                LinearLayout.LayoutParams linearParams =
                                        new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
                                LinearLayout linearLayout = new LinearLayout(getBaseContext());
                                linearLayout.setOrientation(VERTICAL);
                                linearLayout.setLayoutParams(linearParams);

                                // Title TextView
                                LinearLayout.LayoutParams titleParams =
                                        new LinearLayout.LayoutParams(WRAP_CONTENT,WRAP_CONTENT);
                                titleParams.setMargins(dp_16, dp_16, dp_16, 0);
                                TextView title = new TextView(getBaseContext());
                                title.setLayoutParams(titleParams);
                                title.setTextColor(getColor(R.color.colorText));
                                title.setTypeface(Typeface.DEFAULT_BOLD);
                                title.setTextSize(20);
                                title.setText(document.getData().get("title").toString());

                                // Repetition TextView
                                LinearLayout.LayoutParams repeatParams =
                                        new LinearLayout.LayoutParams(WRAP_CONTENT,WRAP_CONTENT);
                                repeatParams.setMargins(dp_16, 0, dp_16, 0);
                                TextView repeat = new TextView(getBaseContext());
                                repeat.setLayoutParams(repeatParams);
                                repeat.setText(String.format("Remind me %s", document.getData()
                                        .get("repeat")));
                                repeat.setTextColor(getColor(R.color.colorText));

                                // At TextView
                                LinearLayout.LayoutParams atParams =
                                        new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
                                atParams.setMargins(dp_16, 0, dp_16, dp_8);
                                TextView atText = new TextView(getBaseContext());
                                atText.setLayoutParams(atParams);
                                atText.setTextColor(getColor(R.color.colorText));
                                atText.setText(String.format("At %s:%s", document.getData()
                                        .get("time_hour"), document.getData()
                                        .get("time_minute")));

                                // Button
                                LinearLayout.LayoutParams btnParams =
                                        new LinearLayout.LayoutParams(MATCH_PARENT, dp_28);
                                btnParams.setMargins(dp_16, 0, dp_16, dp_16);
                                Button button = new Button(getBaseContext());
                                button.setLayoutParams(btnParams);
                                button.setBackground(getDrawable(R.drawable.editbutton));

                                ViewGroup.LayoutParams params = button.getLayoutParams();
                                params.height= WRAP_CONTENT;

                                button.setText(R.string.reminder_edit_btn);
                                button.setLayoutParams(params);
                                button.setTextColor(getColor(R.color.colorPrimaryLight));

                                linearLayout.addView(title);
                                linearLayout.addView(repeat);
                                linearLayout.addView(atText);
                                linearLayout.addView(button);

                                card.addView(linearLayout);
                                home_layout.addView(card);


                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


}