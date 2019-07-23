package com.example.firebase_basics;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class loginActivity extends Activity
{
    private EditText emailID_query,password_query;
    private Button loginButton;
    private TextView signUP_Text,errorText;
    private String emailID,password;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    private FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser != null)
        {
            Intent intent = new Intent(loginActivity.this , ProfileActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        emailID_query = findViewById(R.id.et_emailID);
        password_query = findViewById(R.id.et_password);
        loginButton = findViewById(R.id.bt_loginButton);
        signUP_Text = findViewById(R.id.tv_signUpText);
        errorText  = findViewById(R.id.tv_errorText1);
        progressBar = findViewById(R.id.pb_login);


        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                emailID = emailID_query.getText().toString().trim();
                password = password_query.getText().toString().trim();

                if(emailID.isEmpty() && password.isEmpty())
                {
                    errorText.setText("*Fields Cannot be Empty");
                    emailID_query.requestFocus();
                }
                else if(emailID.isEmpty())
                {
                    errorText.setText("*Email ID required");
                    emailID_query.requestFocus();
                }
                else if(password.isEmpty())
                {
                    errorText.setText("*Password required !");
                    password_query.requestFocus();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(emailID).matches())
                {
                    errorText.setText("Incorrect Email ID");
                    emailID_query.requestFocus();
                }
                else
                {
                    errorText.setText("");
                    progressBar.setVisibility(View.VISIBLE);
                    signInUser();
                }
            }
        });
        signUP_Text.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(loginActivity.this , signUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void signInUser()
    {
        firebaseAuth.signInWithEmailAndPassword(emailID,password)
                .addOnCompleteListener(loginActivity.this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        progressBar.setVisibility(View.INVISIBLE);
                        if(task.isSuccessful())
                        {
                            Log.i("loginActivity :" , "logIn with email ID and password : success");
                            Toast.makeText(getApplicationContext(),"Login : success",Toast.LENGTH_SHORT).show();

                            // move to a new activity and clear all the activities from the top of the stack.
                            Intent intent = new Intent(loginActivity.this , ProfileActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            if(task.getException() instanceof FirebaseAuthInvalidUserException)
                            {
                                Toast.makeText(getApplicationContext(),"Invalid Email ID or password",Toast.LENGTH_SHORT).show();
                                Log.e("loginActivity" , "Login Failed : Invalid Email ID or password ");
                            }
                            else if(task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                            {
                                Toast.makeText(getApplicationContext(),"Invalid Email ID or Password",Toast.LENGTH_SHORT).show();
                                Log.e("loginActivity" , "Login Failed : Invalid Email ID or password ");
                            }
                            else if(task.getException() instanceof FirebaseNetworkException)
                            {
                                Log.e("loginActivity" , "Login Failed : A network error ! ");
                                Toast.makeText(getApplicationContext(), "Please check your network connection", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Log.w("loginActivity :", "logIn with email ID and password : failed " + task.getException().getClass().getName());
                                Log.w("loginActivity" , "Message : "+task.getException().getMessage());
                                Toast.makeText(getApplicationContext(), "Login : failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}
