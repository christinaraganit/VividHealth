package ca.bcit.vividhealth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class NewReminderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reminder);
    }

    public void onCreateNewReminder(View v) {


        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);

        String messageText="Your reminder was created.";
        Toast toast = Toast.makeText(this, messageText, Toast.LENGTH_SHORT);
        toast.show();
        finish();
    }
}