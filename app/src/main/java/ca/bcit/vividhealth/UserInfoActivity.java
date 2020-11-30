package ca.bcit.vividhealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserInfoActivity extends AppCompatActivity {
    EditText user_name;
    EditText user_age;
    EditText user_weight;
    FirebaseUser firebaseUser;
    private final FirebaseFirestore database = FirebaseFirestore.getInstance();

    TimePicker waterTime;
    NumberPicker waterFrequency;
    TimePicker postureTime;
    NumberPicker postureFrequency;

    SwitchMaterial waterReminder;
    SwitchMaterial postureReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final LinearLayout waterPrompt = findViewById(R.id.remind_water_prompt);
        final LinearLayout posturePrompt = findViewById(R.id.remind_posture_prompt);

        waterReminder = findViewById(R.id.water_reminder);
        postureReminder = findViewById(R.id.posture_reminder);

        waterTime = findViewById(R.id.water_time);
        waterFrequency = findViewById(R.id.water_frequency);
        waterFrequency.setMaxValue(60);
        waterFrequency.setMinValue(5);
        postureTime = findViewById(R.id.posture_time);
        postureFrequency = findViewById(R.id.posture_frequency);
        postureFrequency.setMaxValue(60);
        postureFrequency.setMinValue(5);



        waterReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (waterReminder.isChecked()){
                    waterPrompt.setVisibility(View.VISIBLE);
                } else{
                    waterPrompt.setVisibility(View.GONE);
                }
            }
        });

        postureReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (postureReminder.isChecked()){
                    posturePrompt.setVisibility(View.VISIBLE);
                } else{
                    posturePrompt.setVisibility(View.GONE);

                }
            }
        });



    }

    public void onNextBtn(View view) {
        user_name = findViewById(R.id.user_name);
        user_age = findViewById(R.id.user_age);
        user_weight = findViewById(R.id.user_weight);

        String name = user_name.getText().toString();
        String age = user_age.getText().toString();
        String weight = user_weight.getText().toString();

        database.collection("Users").document(firebaseUser.getUid())
                .update("Name", name,
                        "Age", age,
                        "Weight", weight).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    LinearLayout linearLayout = findViewById(R.id.info_layout);
                    ScrollView scrollView = findViewById(R.id.settings);
                    linearLayout.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);

                } else {
                    Toast.makeText(UserInfoActivity.this,
                            "There was an error creating your user" +
                                    "profile. Please check your internet" +
                                    "connection and try again.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void onFinishBtn(View view) {



        final Map<String, Object> userSettings = new HashMap<>();

        if (postureReminder.isChecked()){
            int postureFreq = postureFrequency.getValue();
            int posture_hour = postureTime.getHour();
            int posture_min = postureTime.getMinute();
            userSettings.put("postureReminder", true);
            userSettings.put("posture_hour", posture_hour);
            userSettings.put("posture_freq", postureFreq);
            userSettings.put("posture_min", posture_min);

        } else {
            userSettings.put("postureReminder", false);
        }

        if (waterReminder.isChecked()){
            int waterFreq = waterFrequency.getValue();
            int water_hour = waterTime.getHour();
            int water_min = waterTime.getMinute();
            userSettings.put("water_hour", water_hour);
            userSettings.put("water_freq", waterFreq);
            userSettings.put("water_min", water_min);
            userSettings.put("waterReminder", true);
        } else {
            userSettings.put("waterReminder", false);
        }

        database.collection("Users").document(firebaseUser.getUid())
                .collection("Settings").document("UserSettings")
                .set(userSettings).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getBaseContext(), "An Error has occured. Please check your" +
                        " internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }
}