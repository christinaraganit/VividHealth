package ca.bcit.vividhealth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PresetWorkouts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workouts);
    }

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
}