package ca.bcit.vividhealth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PresetCoreStrength extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core_strength);
    }

    public void onHome(View v) {
        Intent i = new Intent(this, PresetWorkouts.class);
        startActivity(i);
    }
}