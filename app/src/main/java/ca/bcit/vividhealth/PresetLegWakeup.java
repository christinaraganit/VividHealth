package ca.bcit.vividhealth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PresetLegWakeup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leg_wakeup);
    }

    public void onHome(View v) {
        Intent i = new Intent(this, PresetWorkouts.class);
        startActivity(i);
    }
}