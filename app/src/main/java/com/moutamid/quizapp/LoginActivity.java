package com.moutamid.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.moutamid.quizapp.databinding.ActivityLoginBinding;
import com.moutamid.quizapp.databinding.ActivitySignUpBinding;
import com.moutamid.quizapp.utils.Constants;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private String email,password;
    private FirebaseUser firebaseUser;
    ProgressDialog pd;
    private DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = Constants.databaseReference().child("Users");
        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validInfo()) {
                    pd = new ProgressDialog(LoginActivity.this);
                    pd.setMessage("Creating Account....");
                    pd.show();
                    LoginUser();
                }

            }
        });
        binding.signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void LoginUser() {
        FirebaseAuth mAuth = Constants.auth();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            pd.dismiss();
                            sendActivityToDashboard();
                            Toast.makeText(LoginActivity.this, "Logged In",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            pd.dismiss();
                            Toast.makeText(LoginActivity.this, "Wrong Email and Password",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        e.printStackTrace();
                    }
                });



    }

    private void sendActivityToDashboard() {

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }

    public boolean validInfo() {

        email = binding.emailNumber.getText().toString();
        password = binding.password.getText().toString();

        if (email.isEmpty()) {
            binding.emailNumber.setError("Input email!");
            binding.emailNumber.requestFocus();
            return false;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailNumber.setError("Please input valid email!");
            binding.emailNumber.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            binding.password.setError("Input password!");
            binding.password.requestFocus();
            return false;
        }

        return true;
    }
}