package ca.bcit.vividhealth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class NewReminderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reminder);
    }

    public void onCreateNewReminder(View v) {
        EditText new_reminder_title = findViewById(R.id.new_reminder_title);
        RadioButton radioButtonWeek = findViewById(R.id.radioButtonWeek);
        RadioButton radioButton3Days = findViewById(R.id.radioButton3Days);
        RadioButton radioButtonDay = findViewById(R.id.radioButtonDay);
        RadioButton radioButton12Hours = findViewById(R.id.radioButton12Hours);
        RadioButton radioButton3Hours = findViewById(R.id.radioButton3Hours);
        EditText editTextTimeBegin = findViewById(R.id.editTextTimeBegin);
        EditText editTextTimeEnd = findViewById(R.id.editTextTimeEnd);

        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);

        String messageText="Your reminder was created.";
        Toast toast = Toast.makeText(this, messageText, Toast.LENGTH_SHORT);
        toast.show();
        finish();
    }
}