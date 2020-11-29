package ca.bcit.vividhealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditReminder extends AppCompatActivity {

    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    String reminderID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reminder);
        Bundle bundle = getIntent().getExtras();
        reminderID = bundle.get("ID").toString();

        final TextView title = findViewById(R.id.old_title);
        final RadioGroup radioGroup = findViewById(R.id.radioGroup);
        final TimePicker timePicker = findViewById(R.id.r_time);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        database.collection("Users").document(firebaseUser.getUid())
                .collection("Reminders")
                .document(reminderID)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                title.setText(documentSnapshot.get("title").toString());
                if(documentSnapshot.get("repeat").toString().equals("Every week")){
                    radioGroup.check(R.id.radioButton);
                } else if(documentSnapshot.get("repeat").toString().equals("Every 3 days")){
                    radioGroup.check(R.id.radioButton1);
                } else if(documentSnapshot.get("repeat").toString().equals("Every day")){
                    radioGroup.check(R.id.radioButton2);
                } else if(documentSnapshot.get("repeat").toString().equals("Every 12 hours")){
                    radioGroup.check(R.id.radioButton3);
                } else if(documentSnapshot.get("repeat").toString().equals("Every 3 hours")){
                    radioGroup.check(R.id.radioButton4);
                }

                timePicker.setHour(documentSnapshot.get("time_hour").hashCode());
                timePicker.setMinute(documentSnapshot.get("time_minute").hashCode());


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getBaseContext(), "Failed to get reminder. " +
                        "Please check your internet connection.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    public void onEditReminder(View view) {
        Map<String, Object> reminder = new HashMap<>();

        final EditText title = findViewById(R.id.old_reminder_title);
        final TimePicker time = findViewById(R.id.r_time);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        RadioButton radioButton = findViewById(radioGroup.getCheckedRadioButtonId());

        if(title.getText().toString().trim().isEmpty()){
            Toast.makeText(getBaseContext(), "Please give your reminder a title",
                    Toast.LENGTH_SHORT).show();
        }

        reminder.put("title",title.getText().toString());
        reminder.put("repeat", radioButton.getText());
        reminder.put("time_hour", time.getHour());
        reminder.put("time_minute", time.getMinute());
        reminder.put("timestamp", FieldValue.serverTimestamp());

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        assert firebaseUser != null;

        database.collection("Users").document(firebaseUser.getUid())
                .collection("Reminders")
                .document(reminderID).update(reminder)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getBaseContext(), "Reminder Edited", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getBaseContext(), "Failed to edit reminder," +
                        " please check your internet connection.", Toast.LENGTH_SHORT).show();
            }
        });
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}