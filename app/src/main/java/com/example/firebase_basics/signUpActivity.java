package com.example.firebase_basics;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class signUpActivity extends Activity
{
    private TextView errorText , signInText;
    private Button signUpButton;
    private EditText emailID_query , password_query , confirmPassword_query;
    private String emailID,password,confirmPassword;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        errorText = findViewById(R.id.tv_errorText2);
        signInText = findViewById(R.id.tv_signInLink);
        emailID_query = findViewById(R.id.et_emailID2);
        password_query = findViewById(R.id.et_password2);
        confirmPassword_query = findViewById(R.id.et_confirmPassword);
        signUpButton = findViewById(R.id.bt_signUpButton);
        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.pb_signUp);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailID = emailID_query.getText().toString().trim();
                password = password_query.getText().toString().trim();
                confirmPassword = confirmPassword_query.getText().toString().trim();
                if(emailID.isEmpty() && password.isEmpty() && confirmPassword.isEmpty())
                {
                    errorText.setText("*Fields cannot be empty !");
                }
                else if(emailID.isEmpty())
                {
                    errorText.setText("*Email ID required !");
                }
                else if(password.isEmpty())
                {
                    errorText.setText("*Password Required");
                }
                else if(confirmPassword.isEmpty())
                {
                    errorText.setText("*Confirm password");
                }
                else if(!password.equals(confirmPassword))
                {
                    errorText.setText("*Passwords Do not match !");
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(emailID).matches())
                {
                    errorText.setText("*Enter a correct Email ID");
                }
                else if(password.length() < 6)
                {
                    errorText.setText("*Password should be of at least 6 characters");
                }
                else
                {
                    errorText.setText("");
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser();
                }
            }
        });

        signInText.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(signUpActivity.this , loginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void registerUser()
    {
        firebaseAuth.createUserWithEmailAndPassword(emailID,password)
                .addOnCompleteListener(signUpActivity.this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        progressBar.setVisibility(View.INVISIBLE);
                        if(task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(),"SignUp : success",Toast.LENGTH_SHORT).show();
                            Log.i("SignUpActivity :" , "Sign Up : success ");

                            // open the new activity and remove all activities from the top of the stack
                            Intent intent = new Intent(signUpActivity.this , ProfileActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            if(task.getException() instanceof FirebaseAuthUserCollisionException)
                            {
                                Toast.makeText(getApplicationContext(),"User already exists",Toast.LENGTH_SHORT).show();
                                Log.e("SignUpActivity :","Sign Up failed : User already exists");
                            }
                            else if(task.getException() instanceof FirebaseAuthWeakPasswordException)
                            {
                                Toast.makeText(getApplicationContext() , "Invalid/Weak Password",Toast.LENGTH_SHORT).show();
                                Log.e("SignUpActivity :","Sign Up failed : Invalid / Weak Password ");
                            }
                            else if(task.getException() instanceof FirebaseNetworkException)
                            {
                                Toast.makeText(getApplicationContext() , "Please check your network connection",Toast.LENGTH_SHORT).show();
                                Log.e("SignUpActivity :","Sign Up failed : Network Error ");
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Sign Up failed",Toast.LENGTH_SHORT).show();
                                Log.e("SignUpActivity :","Sign Up failed : "+task.getException().getClass().getName());
                                Log.e("SignUpActivity :","Sign Up failed : "+task.getException().getMessage());
                            }
                        }
                    }
                });
    }
}
