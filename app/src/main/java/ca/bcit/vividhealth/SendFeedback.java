package ca.bcit.vividhealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class SendFeedback extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_feedback);
        final EditText field1 = (EditText) findViewById(R.id.field1);
        final EditText field2 = (EditText) findViewById(R.id.field2);
        Button btn = (Button) findViewById(R.id.send_feedback);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/html");
                i.putExtra(Intent.EXTRA_EMAIL, new String("VividHealth@gmail.com"));
                i.putExtra(Intent.EXTRA_SUBJECT, "Feedback for VividHealth");
                i.putExtra(Intent.EXTRA_TEXT, "Topic: " + field1.getText() + "\nMessage: " +
                        field2.getText());
                try {
                    startActivity(Intent.createChooser(i, "Please select Email"));
                }
                catch (android.content.ActivityNotFoundException e) {
                    Toast.makeText(SendFeedback.this, "There are no Email Clients",
                            Toast.LENGTH_SHORT).show();
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