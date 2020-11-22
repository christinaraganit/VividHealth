package ca.bcit.vividhealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    static MainActivity mainActivity;
    private static final String TAG = "";
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Checks if user is logged in, if not returns to the login page
        try{
            throw mAuth.getCurrentUser().reload().getException();
        } catch (Exception e) {
            mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null){
                System.out.println("User Found");
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
            }

        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        mainActivity = this;
    }

    public void onLogin(final View v) {
        final ProgressBar progressBar = findViewById(R.id.indeterminateBar);
        final EditText userEmail = findViewById(R.id.edit_text_email);
        final EditText userPassword = findViewById(R.id.edit_text_password);
        final TextView signup = findViewById(R.id.signUp_button);

        userEmail.setVisibility(View.GONE);
        userPassword.setVisibility(View.GONE);
        signup.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        v.setVisibility(View.GONE);
        progressBar.animate();

        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Email or Password is left blank",
                    Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            userEmail.setVisibility(View.VISIBLE);
            userPassword.setVisibility(View.VISIBLE);
            signup.setVisibility(View.VISIBLE);
            v.setVisibility(View.VISIBLE);

        } else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                progressBar.setVisibility(View.GONE);
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent intent = new Intent(getApplicationContext(),
                                        HomeActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                // If sign in fails, display a message to the user.
                                progressBar.setVisibility(View.GONE);
                                userEmail.setVisibility(View.VISIBLE);
                                userPassword.setVisibility(View.VISIBLE);
                                signup.setVisibility(View.VISIBLE);
                                v.setVisibility(View.VISIBLE);

                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(getApplicationContext(), "Account was not found.",
                                        Toast.LENGTH_SHORT).show();
                                // ...
                            }

                            // ...
                        }
                    });
        }
        //Intent intent = new Intent(this, HomeActivity.class);
        //startActivity(intent);
    }

    public void onSignUp(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    public static MainActivity getInstance(){
        return mainActivity;
    }
}