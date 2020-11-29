package ca.bcit.vividhealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static androidx.core.app.NotificationCompat.*;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private NavigationView navigationView;
    private ActionBarDrawerToggle mToggle;
    private static final String CHANNEL_ID = "CHANNEL_SAMPLE";
    FirebaseAuth mAuth;
    TextView profileName;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        profileName = findViewById(R.id.profile_name);

        database.collection("Users").document(firebaseUser.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            profileName.setText(document.get("Name").toString());

                        }
                    }
                });

        // Finds you button from the xml layout file
        Button createNotificationButton = findViewById(R.id.notify);

        // Waits for you to click the button
        createNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Starts the function below
                addNotification();
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        SharedPreferences sharedPreferences
                = getSharedPreferences(
                "sharedPrefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor
                = sharedPreferences.edit();
        final boolean isDarkModeOn
                = sharedPreferences
                .getBoolean(
                        "isDarkModeOn", false);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        MenuItem menuItem1 = navigationView.getMenu().findItem(R.id.nav_toggle_mode); // This is the menu item that contains your switch
        final Switch drawerSwitch = (Switch) menuItem1.getActionView().findViewById(R.id.drawer_switch);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.nav_open_drawer,
                R.string.nav_close_drawer);

        drawer.addDrawerListener(toggle);
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                if (isDarkModeOn) {
                    drawerSwitch.setChecked(true);
                    AppCompatDelegate
                            .setDefaultNightMode(
                                    AppCompatDelegate
                                            .MODE_NIGHT_YES);
                }
                else {
                    drawerSwitch.setChecked(false);
                    AppCompatDelegate
                            .setDefaultNightMode(
                                    AppCompatDelegate
                                            .MODE_NIGHT_NO);
                }
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        if (isDarkModeOn) {
            drawerSwitch.setChecked(true);
            AppCompatDelegate
                    .setDefaultNightMode(
                            AppCompatDelegate
                                    .MODE_NIGHT_YES);
        }
        else {
            drawerSwitch.setChecked(false);
            AppCompatDelegate
                    .setDefaultNightMode(
                            AppCompatDelegate
                                    .MODE_NIGHT_NO);
        }
        toggle.syncState();

        drawer.bringToFront();
        drawer.requestLayout();

//
//        buttonToggleDark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    AppCompatDelegate
//                            .setDefaultNightMode(
//                                    AppCompatDelegate
//                                            .MODE_NIGHT_YES);
//                } else {
//                    AppCompatDelegate
//                            .setDefaultNightMode(
//                                    AppCompatDelegate
//                                            .MODE_NIGHT_NO);
//                }
//            }
//        });

        mToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.enabled, R.string.disabled);
        drawer.addDrawerListener(mToggle);
        drawer.setClickable(true);
        mToggle.setDrawerIndicatorEnabled(true);
        mToggle.syncState();

        // THE CODE THAT WORKS :D
//        MenuItem menuItem1 = navigationView.getMenu().findItem(R.id.nav_toggle_mode); // This is the menu item that contains your switch
//        Switch drawerSwitch = (Switch) menuItem1.getActionView().findViewById(R.id.drawer_switch);
//        drawerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    Toast.makeText(HomeActivity.this, "Switch turned on", Toast.LENGTH_SHORT).show();
//                    AppCompatDelegate
//                            .setDefaultNightMode(
//                                    AppCompatDelegate
//                                            .MODE_NIGHT_YES);
//                } else {
//                    Toast.makeText(HomeActivity.this, "Switch turned off", Toast.LENGTH_SHORT).show();
//                    AppCompatDelegate
//                            .setDefaultNightMode(
//                                    AppCompatDelegate
//                                            .MODE_NIGHT_NO);
//                }
//            }
//        });


        drawerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppCompatDelegate
                            .setDefaultNightMode(
                                    AppCompatDelegate
                                            .MODE_NIGHT_YES);
                    editor.putBoolean(
                            "isDarkModeOn", true);
                    editor.apply();
                } else {
                    AppCompatDelegate
                            .setDefaultNightMode(
                                    AppCompatDelegate
                                            .MODE_NIGHT_NO);
                    editor.putBoolean(
                            "isDarkModeOn", false);
                    editor.apply();
                }
            }
        });
    }

    private void addNotification() {

        // Get id & message
        int notificationId = 0;

        // Call MainActivity when notification is tapped.
        Intent mainIntent = new Intent(getApplicationContext(), ProfileActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, mainIntent, 0);

        // NotificationManager
        NotificationManager notificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // For API 26 and above
            CharSequence channelName = "My Notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("TITLE")
                .setContentText("message")
                .setContentIntent(contentIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        // Notify
        notificationManager.notify(notificationId, builder.build());
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
                intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_reminders:
                intent = new Intent(this, NewReminderActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_workouts:
                intent = new Intent(this, PresetWorkouts.class);
                startActivity(intent);
                break;
            case R.id.nav_about:
                intent = new Intent(this, AboutUs.class);
                startActivity(intent);
                break;
            case R.id.nav_feedback:
                System.out.println("Feedback clicked");
                intent = new Intent(this, SendFeedback.class);
                startActivity(intent);
                break;
            case R.id.nav_toggle_mode:
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}