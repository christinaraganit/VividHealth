package ca.bcit.vividhealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Objects;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.LinearLayout.*;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle mToggle;
    private FirebaseAuth mAuth;
    private NavigationView navigationView;
    private Object FirebaseAuthInvalidUserException;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private FirebaseUser firebaseUser;
    private String TAG = "";
    LinearLayout reminders_container;
    private Menu menu;
    int waterTracker;
    int postureTracker;
    TextView waterTrackerText;
    TextView postureTrackerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Checks if user is logged in, if not returns to the login page
        try {
            throw Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).reload().getException());
        } catch (Exception e) {
            mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser.reload() == FirebaseAuthInvalidUserException) {
                System.out.println("no user");
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }

        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        waterTracker = 0;
        postureTracker = 0;

        waterTrackerText = findViewById(R.id.water_count);
        postureTrackerText = findViewById(R.id.posture_count);

        LinearLayout water = findViewById(R.id.waterImg);
        LinearLayout posture = findViewById(R.id.postureImg);

        final TextView greeting = findViewById(R.id.greeting);
        reminders_container = findViewById(R.id.reminders_container);

        firebaseUser = mAuth.getCurrentUser();
        database.collection("Users").document(firebaseUser.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            System.out.println("Document: " + document.getData());
                            if (document.getData() == null){
                                Intent intent = new Intent(getBaseContext(), UserInfoActivity.class);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext()
                                        , "Your user profile is empty", Toast.LENGTH_SHORT).show();
                                finish();

                            } else {
                                greeting.setText(String.format("Hello, %s", document.get("Name").toString()));
                                greeting.setVisibility(View.VISIBLE);

                                waterTracker = Integer.parseInt(document.get("waterCount").toString());
                                postureTracker = Integer.parseInt(document.get("postureCount").toString());
                                waterTrackerText.setText(document.get("waterCount").toString());
                                postureTrackerText.setText(document.get("postureCount").toString());


                            }
                        }
                    }
                });

        water.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                waterTracker = waterTracker + 1;
                waterTrackerText.setText(String.format("%s", waterTracker));
                database.collection("Users").document(firebaseUser.getUid())
                        .update("waterCount", waterTracker)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

            }
        });

        posture.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                postureTracker = postureTracker + 1;
                postureTrackerText.setText(String.format("%s", postureTracker));
                database.collection("Users").document(firebaseUser.getUid())
                        .update("postureCount", postureTracker)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

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
    public boolean onOptionsItemSelected(MenuItem item) {
        navigationView.setCheckedItem(R.id.nav_toggle_mode);
        navigationView.getMenu().performIdentifierAction(R.id.nav_toggle_mode, 0);
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        Fragment fragment = null;
        Intent intent = null;

        switch (id) {
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
                intent = new Intent(this, PresetWorkouts.class);
                startActivity(intent);
                break;
            case R.id.nav_about:
                intent = new Intent(this, AboutUs.class);
                startActivity(intent);
                break;
            case R.id.nav_feedback:
                intent = new Intent(this, SendFeedback.class);
                startActivity(intent);
                break;
            case R.id.nav_toggle_mode:
                MenuItem menuItem1 = navigationView.getMenu().findItem(R.id.nav_toggle_mode); // This is the menu item that contains your switch
                Switch drawerSwitch = (Switch) menuItem1.getActionView().findViewById(R.id.drawer_switch);
                drawerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            Toast.makeText(HomeActivity.this, "Switch turned on", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(HomeActivity.this, "Switch turned off", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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

    //This will be moved when workout page created
    public void onSittingBreak(View v) {
        Intent i = new Intent(this, PresetSittingBreak.class);
        startActivity(i);
    }

    public void onGetActive(View v) {
        Intent i = new Intent(this, PresetGetActive.class);
        startActivity(i);
    }

    public void onMorningCompliment(View v) {
        Intent i = new Intent(this, PresetMorningCompliment.class);
        startActivity(i);
    }

    public void onCoreStrength(View v) {
        Intent i = new Intent(this, PresetCoreStrength.class);
        startActivity(i);
    }

    public void onLegWakeup(View v) {
        Intent i = new Intent(this, PresetLegWakeup.class);
        startActivity(i);
    }

    public void onWorkouts(View v) {
        Intent i = new Intent(this, PresetWorkouts.class);
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
                if (currentUser == null) {
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


    @Override
    protected void onResume() {
        super.onResume();
        reminders_container.removeAllViews();
        loadReminders();
    }




    public void loadReminders() {
        final LinearLayout home_layout = findViewById(R.id.home_layout);


        database.collection("Users").document(firebaseUser.getUid()).collection("Reminders").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (final QueryDocumentSnapshot document : task.getResult()) {

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



                                //Set Up Alarm
                                String title_t = document.getData().get("title").toString().trim();
                                int hour = Integer.parseInt(document.getData()
                                        .get("time_hour").toString());
                                int minute = Integer.parseInt(document.getData()
                                        .get("time_minute").toString());

                                //Set up Notification
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTimeInMillis(System.currentTimeMillis());
                                calendar.set(Calendar.SECOND, 0);
                                calendar.set(Calendar.HOUR_OF_DAY, hour);
                                calendar.set(Calendar.MINUTE, minute);
                                new AlarmReceiver(getApplicationContext(), hour, minute, title_t,
                                        document.getData().get("repeat").toString(), calendar);


                                // Card View
                                LinearLayout.LayoutParams cardParams =
                                        new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
                                cardParams.setMargins(dp_32, dp_8, dp_32, dp_16);
                                final CardView card = new CardView(getBaseContext());
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
                                        new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
                                titleParams.setMargins(dp_16, dp_16, dp_16, 0);
                                TextView title = new TextView(getBaseContext());
                                title.setLayoutParams(titleParams);
                                title.setTextColor(getColor(R.color.colorText));
                                title.setTypeface(Typeface.DEFAULT_BOLD);
                                title.setTextSize(20);
                                title.setText(title_t);



                                // Repetition TextView
                                LinearLayout.LayoutParams repeatParams =
                                        new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
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
                                String minutes = "" + minute;
                                if (minute < 10){
                                    minutes = "0" + minute;
                                }
                                atText.setText(String.format("At %s:%s", document.getData()
                                        .get("time_hour"), minutes));

                                // Button
                                LinearLayout.LayoutParams btnParams =
                                        new LinearLayout.LayoutParams(MATCH_PARENT, dp_28);
                                btnParams.setMargins(dp_16, 0, dp_16, dp_8);
                                Button button = new Button(getBaseContext());
                                button.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(getBaseContext(), EditReminder.class);
                                        intent.putExtra("ID", document.getId());
                                        startActivity(intent);

                                    }
                                });
                                button.setLayoutParams(btnParams);
                                button.setBackground(getDrawable(R.drawable.editbutton));
                                ViewGroup.LayoutParams params = button.getLayoutParams();
                                params.height = WRAP_CONTENT;
                                button.setText(R.string.reminder_edit_btn);
                                button.setLayoutParams(params);
                                button.setTextColor(getColor(R.color.colorPrimaryLight));

                                LinearLayout.LayoutParams deleteBtnParams =
                                        new LinearLayout.LayoutParams(MATCH_PARENT, dp_28);
                                deleteBtnParams.setMargins(dp_16, 0, dp_16, dp_16);
                                Button delete_button = new Button(getBaseContext());
                                delete_button.setLayoutParams(deleteBtnParams);
                                delete_button.setBackground(getDrawable(R.drawable.editbutton));
                                ViewGroup.LayoutParams deleteBtnMoreParams = delete_button.getLayoutParams();
                                deleteBtnMoreParams.height = WRAP_CONTENT;
                                delete_button.setText(R.string.delete_reminder_btn);
                                delete_button.setLayoutParams(params);
                                delete_button.setTextColor(getColor(R.color.colorPrimaryLight));
                                delete_button.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);


                                        builder.setCancelable(true);
                                        builder.setTitle("Delete Reminder");
                                        builder.setMessage("Are you sure you want to delete this reminder?");
                                        builder.setPositiveButton("Confirm",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        database.collection("Users")
                                                                .document(firebaseUser.getUid())
                                                                .collection("Reminders")
                                                                .document(document.getId())
                                                                .delete()
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                                                        Toast.makeText(getBaseContext(), "Reminder Deleted.", Toast.LENGTH_SHORT).show();
                                                                        reminders_container.removeView(card);
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Log.w(TAG, "Error deleting document", e);
                                                                        Toast.makeText(getBaseContext(), "Failed to delete reminder.", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });

                                                    }
                                                });
                                        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });

                                        AlertDialog dialog = builder.create();
                                        dialog.show();

                                        dialog.getButton(dialog.BUTTON_NEGATIVE).setTextColor(getColor(R.color.colorText));
                                        dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(getColor(R.color.colorPrimary));

                                    }
                                });
                                linearLayout.addView(title);
                                linearLayout.addView(repeat);
                                linearLayout.addView(atText);
                                linearLayout.addView(button);
                                linearLayout.addView(delete_button);

                                card.addView(linearLayout);
                                reminders_container.addView(card);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}