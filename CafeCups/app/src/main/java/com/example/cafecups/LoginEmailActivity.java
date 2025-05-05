package com.example.cafecups;

import static android.view.View.GONE;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginEmailActivity extends AppCompatActivity {


    private EditText emailInput;
    private EditText passwordInput;
    private Button loginBtn,goToRegisterationBtn;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_email);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        emailInput = findViewById(R.id.login_Email);
        passwordInput = findViewById(R.id.login_password);
        loginBtn = findViewById(R.id.login_btn);
        goToRegisterationBtn = findViewById(R.id.go_to_registration_btn);
        progressBar = findViewById(R.id.login_progress_bar);
        progressBar.setVisibility(GONE);

        mAuth = FirebaseAuth.getInstance();
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateDataAndDoLogin();
            }
        });
        goToRegisterationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginEmailActivity.this,RegisterActivity.class));
                finish();
            }
        });
    }

    private void validateDataAndDoLogin(){
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if(email.isEmpty()){
            emailInput.setError("Enter an Email");
            emailInput.requestFocus();
            return;
        }
        if(email.length() < 10){
            emailInput.setError("Enter a valid Email");
            emailInput.requestFocus();
            return;
        }
        if(password.isEmpty()){
            passwordInput.setError("Enter a password");
            passwordInput.requestFocus();
            return;
        }
        if(password.length() < 8 || password.length() > 16){
            passwordInput.setError("Password length between 8 and 16");
            passwordInput.requestFocus();
            return;
        }
        doLogin(email,password);
    }

    private void doLogin(String email, String password){
        //AndroidUtil.showToast(LoginEmailActivity.this,"dologin");


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null && user.isEmailVerified()) {
                            // Email verified, proceed to the main app
                            Toast.makeText(LoginEmailActivity.this,
                                    "Login successful!",
                                    Toast.LENGTH_SHORT).show();
                            // Intent to open your HomeActivity
                            Intent intent = new Intent(LoginEmailActivity.this, LoginUsernameActivity.class);
                            startActivity(intent);
                            finish();  // Optional: close login screen
                        } else {
                            // Email not verified
                            Toast.makeText(LoginEmailActivity.this,
                                    "Please verify your email before logging in.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Login failed
                        Toast.makeText(LoginEmailActivity.this,
                                "Authentication failed: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}