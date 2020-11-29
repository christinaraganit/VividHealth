package ca.bcit.vividhealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserInfoActivity extends AppCompatActivity {
    EditText user_name;
    EditText user_age;
    EditText user_weight;
    FirebaseUser firebaseUser;
    private final FirebaseFirestore database = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


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
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    finish();
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
}