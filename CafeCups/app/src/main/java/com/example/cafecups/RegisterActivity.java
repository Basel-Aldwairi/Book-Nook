package com.example.cafecups;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cafecups.utils.AndroidUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class RegisterActivity extends AppCompatActivity {

    private TextView emailInput, passwordInput, confirmPasswordInput;
    private Button registerBtn,goToLoginBtn;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        emailInput = findViewById(R.id.register_Email);
        passwordInput = findViewById(R.id.register_password);
        confirmPasswordInput = findViewById(R.id.register_confirm_password);
        registerBtn = findViewById(R.id.register_btn);
        goToLoginBtn = findViewById(R.id.go_to_login_btn);
        progressBar = findViewById(R.id.register_progress_bar);
        setInProgress(false);
        mAuth = FirebaseAuth.getInstance();

        emailInput.setText(getIntent().getStringExtra("email"));
        passwordInput.setText(getIntent().getStringExtra("password"));

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateDataAndDoRegister();
            }
        });
        goToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,LoginEmailActivity.class);
                intent.putExtra("email",emailInput.getText().toString().trim());
                intent.putExtra("password",passwordInput.getText().toString().trim());
                startActivity(intent);
                finish();
            }
        });
    }
    private void setInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            goToLoginBtn.setVisibility(View.GONE);
            registerBtn.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            goToLoginBtn.setVisibility(View.VISIBLE);
            registerBtn.setVisibility(View.VISIBLE);
        }
    }
    private void validateDataAndDoRegister(){
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();

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
        if(!confirmPassword.equals(password)){
            confirmPasswordInput.setError("Password doesn't match");
            confirmPasswordInput.requestFocus();
            passwordInput.setText("");
            confirmPasswordInput.setText("");
            return;
        }
        doRegister(email,password);
    }
    private void doRegister(String email, String password){
        setInProgress(true);
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    sendVerificationEmail(email);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(e instanceof FirebaseAuthUserCollisionException){
                    setInProgress(false);
                    emailInput.setError("Email Already Registered");
                    emailInput.requestFocus();
                }
                else {
                    setInProgress(false);
                    AndroidUtil.showToast(RegisterActivity.this,"Oops! Something went wrong");
                }
            }
        });
    }
    private void sendVerificationEmail(String email){
        if(mAuth.getCurrentUser()!=null){
            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        setInProgress(false);
                        AndroidUtil.showToast(RegisterActivity.this,"A verification email was sent to " + email);
                    }
                    else{
                        setInProgress(false);
                        AndroidUtil.showToast(RegisterActivity.this,"Oops! Failed to send verification email");
                    }
                }
            });
        }
    }
}