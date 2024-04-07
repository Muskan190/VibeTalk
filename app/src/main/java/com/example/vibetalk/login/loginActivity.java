package com.example.vibetalk.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vibetalk.Common.Util;
import com.example.vibetalk.MainActivity;
import com.example.vibetalk.MessageActivity;
import com.example.vibetalk.R;
import com.example.vibetalk.password.ResetPasswordActivity;
import com.example.vibetalk.signup.SignupActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

public class loginActivity extends AppCompatActivity {
    private TextInputEditText etEmail,etPassword;
    private String email,password;
    private View progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail=findViewById(R.id.etEmail);
        etPassword=findViewById(R.id.etPassword);
        progressBar=findViewById(R.id.progressBar);
    }
    public void tvSignupClick(View v)
    {
        startActivity(new Intent(this, SignupActivity.class));
    }

    public void btnLoginClick(View v)
    {
        email=etEmail.getText().toString().trim();
        password=etPassword.getText().toString().trim();

        if(email.equals(""))
        {
            etEmail.setError(getString(R.string.enter_email));
        }
        else if(password.equals(""))
        {
            etPassword.setError(getString(R.string.enter_password));
        }
        else {
            if(Util.connectionAvailable(this)) {
                progressBar.setVisibility(View.VISIBLE);
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            startActivity(new Intent(loginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            //Toast.makeText( context: LoginActivity.this, text:"Login Failed : "+task.getException(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(loginActivity.this, "Login Failed: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
            else {
                startActivity(new Intent(loginActivity.this, MessageActivity.class));
            }


        }
    }
    public void tvResetPasswordClick(View view)
    {
        startActivity(new Intent(loginActivity.this, ResetPasswordActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();

        if(firebaseUser!=null)
        {

               FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
                   @Override
                   public void onSuccess(String s) {
                       Util.updateDeviceToken(loginActivity.this,s);

                   }
               });

            startActivity((new Intent(loginActivity.this,MainActivity.class)));
            finish();
        }
    }
}