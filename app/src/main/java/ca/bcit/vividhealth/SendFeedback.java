package ca.bcit.vividhealth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SendFeedback extends AppCompatActivity {

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
    }

}