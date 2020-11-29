package ca.bcit.vividhealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class NewReminderActivity extends AppCompatActivity {

    private final String TAG = "";
    private FirebaseAuth mAuth;
    private Object FirebaseAuthInvalidUserException;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Checks if user is logged in, if not returns to the login page
        try{
            throw Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).reload().getException());
        } catch (Exception e) {
            mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            assert currentUser != null;
            if (currentUser.reload() == FirebaseAuthInvalidUserException){
                System.out.println("no user");
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }

        }
        setContentView(R.layout.activity_new_reminder);
    }

    public void onCreateNewReminder(View v) {
        Map<String, Object> reminder = new HashMap<>();

        final EditText title = findViewById(R.id.new_reminder_title);
        final TimePicker time = findViewById(R.id.r_time);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        RadioButton radioButton = findViewById(radioGroup.getCheckedRadioButtonId());

        reminder.put("title",title.getText().toString());
        reminder.put("repeat", radioButton.getText());
        reminder.put("time_hour", time.getHour());
        reminder.put("time_minute", time.getMinute());
        reminder.put("timestamp", FieldValue.serverTimestamp());

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        assert firebaseUser != null;
        database.collection("Users").document(firebaseUser.getUid()).collection("Reminders")
                .add(reminder)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);
                        String messageText="Your reminder was created.";
                        Toast toast = Toast.makeText(getApplicationContext(), messageText, Toast.LENGTH_SHORT);
                        toast.show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document", e);
            }
        });
    }

}
