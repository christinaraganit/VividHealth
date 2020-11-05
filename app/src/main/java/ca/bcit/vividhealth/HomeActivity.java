package ca.bcit.vividhealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Object FirebaseAuthInvalidUserException;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Checks if user is logged in, if not returns to the login page
        try{
            throw mAuth.getCurrentUser().reload().getException();
        } catch (Exception e) {
            mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser.reload() == FirebaseAuthInvalidUserException){
                System.out.println("no user");
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }

        }

        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_home);

        // Firebase Auth


    }

    public void onNewReminder(View v) {
        Intent intent = new Intent(this, NewReminderActivity.class);
        startActivity(intent);
    }

    public void onWorkOutPlan(View v) {
        Intent i = new Intent(this, WorkoutPlan.class);
        startActivity(i);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // on Start check if user logged in
        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                if (currentUser == null){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }
}