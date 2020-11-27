package ca.bcit.vividhealth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MorningCompliment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_morning_compliment);
    }

    public void onHome(View v) {
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
    }
}