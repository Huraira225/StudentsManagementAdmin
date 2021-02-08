package com.example.adminloginactivity.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adminloginactivity.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    String Email,Password;
    EditText etEmail, etPassword;
    FirebaseAuth auth;
    ProgressBar progressBar;
    TextView btnReset;
    Button btnLogin;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        etEmail = (EditText) findViewById(R.id.editText_email);
        etPassword = (EditText) findViewById(R.id.editText_password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnLogin = (Button) findViewById(R.id.button_LogIn);
        btnReset = (TextView) findViewById(R.id.textView_forgetpassword);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance("https://databaseregisterationuser-default-rtdb.firebaseio.com/").getReference("admin");




        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(LoginScreenActivity.this, ForgetPasswordActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Email = etEmail.getText().toString().trim();
                Password = etPassword.getText().toString().trim();

                if (TextUtils.isEmpty(Email)) {
                    etEmail.setError("email address is required");
                    etEmail.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                    etEmail.setError("Please provide valid email");
                    etEmail.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(Password)) {
                    etPassword.setError("password is required");
                    etPassword.requestFocus();
                    return;
                }

                if (Password.length() < 6) {
                    etPassword.setError("Password too short, enter minimum 6 characters!");
                    etPassword.requestFocus();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                //authenticate user
                auth.signInWithEmailAndPassword(Email, Password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {

                                    Toast.makeText(LoginActivity.this,"Authentication failed, check your email and password or sign up", Toast.LENGTH_LONG).show();

                                } else {
                                    SharedPreferences preferences = getSharedPreferences("admin", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("isadminLogin", "adminLogin");
                                    editor.commit();
                                    Toast.makeText(LoginActivity.this,"User login in this Activity", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });
    }
}
