package ca.bcit.vividhealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "";
    private FirebaseAuth mAuth;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

    }

    public void onSignUp(View view) {

        EditText userEmail = findViewById(R.id.email);
        final EditText userPassword = findViewById(R.id.password);

        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Email or Password is left blank",
                    Toast.LENGTH_SHORT).show();
        } else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {


                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                final FirebaseUser user = mAuth.getCurrentUser();

                                Calendar calendar = Calendar.getInstance();
                                calendar.setTimeInMillis(System.currentTimeMillis());

                                final Map<String, Object> userMap = new HashMap<>();
                                userMap.put("Name", "");
                                userMap.put("Age", "");
                                userMap.put("Weight", 0);
                                userMap.put("Email", user.getEmail());
                                userMap.put("timestamp", FieldValue.serverTimestamp());
                                userMap.put("waterCount", 0);
                                userMap.put("postureCount", 0);
                                userMap.put("date", calendar.getTime());



                                database.collection("Users").document(user.getUid())
                                        .set(userMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){

                                            Intent intent = new Intent(getApplicationContext(),
                                                    UserInfoActivity.class);
                                            MainActivity.getInstance().finish();
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Log.w(TAG, "Error setting documents.",
                                                    task.getException());

                                            Toast.makeText(SignUpActivity.this,
                                                    "There was an error creating your user" +
                                                            "profile. Please check your internet" +
                                                            "connection and try again.",
                                                    Toast.LENGTH_SHORT).show();

                                            user.delete()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Log.d(TAG, "User account deleted.");
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                });


                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure",
                                        task.getException());
                                Toast.makeText(SignUpActivity.this,
                                        "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }

                            // ...
                        }
                    });
        }



    }
}