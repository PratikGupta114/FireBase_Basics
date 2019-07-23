package com.example.firebase_basics;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class ProfileActivity extends Activity
{
    private EditText name_query, contactNumber_query, address_query, aadhaar_query, DOB_query;
    private String name, contactNumber, address, aadhaar_number,DOB;
    private Button buttonSubmit;
    private ProgressBar progressBar;
    private DatePickerDialog datePickerDialog;
    private TextView errorText,welcomeText;
    private FirebaseDatabase userDatabase;
    private DatabaseReference userDatabaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private String currentUserEmailID ;
    private String currentUserUID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        currentUserEmailID = "";

        if(firebaseUser != null)
        {
            currentUserEmailID = firebaseUser.getEmail();
            currentUserUID = firebaseUser.getUid();
        }
        else
        {
            Intent intent = new Intent(ProfileActivity.this , loginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        userDatabase = FirebaseDatabase.getInstance();
        userDatabaseReference = userDatabase.getReference();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        name_query = findViewById(R.id.et_NameQuery);
        contactNumber_query = findViewById(R.id.et_contactQuery);
        address_query = findViewById(R.id.et_addressQuery);
        aadhaar_query = findViewById(R.id.et_aadharQuery);
        DOB_query = findViewById(R.id.et_DOBQuery);
        buttonSubmit = findViewById(R.id.bt_profile);
        progressBar = findViewById(R.id.pb_profile);
        errorText = findViewById(R.id.tv_errorText3);
        welcomeText = findViewById(R.id.tv_welcomeMessage);
        welcomeText.setText("Welcome "+currentUserEmailID);

        // On click listener for Picking Up the Date
        DOB_query.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Calendar calendar = Calendar.getInstance();
                final int year1 = calendar.get(Calendar.YEAR);
                final int month1 = calendar.get(Calendar.MONTH);
                final int date1 = calendar.get(Calendar.DATE);

                datePickerDialog = new DatePickerDialog(ProfileActivity.this, new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth)
                    {
                        DOB_query.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                    }
                },year1,month1,date1);
                datePickerDialog.show();
            }
        });

        // On click listener for submitting the data.
        buttonSubmit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                name = name_query.getText().toString().trim();
                contactNumber = contactNumber_query.getText().toString().trim();
                address = address_query.getText().toString().trim();
                aadhaar_number = aadhaar_query.getText().toString().trim();
                DOB = DOB_query.getText().toString().trim();

                if(name.isEmpty() || contactNumber.isEmpty() || address.isEmpty() || aadhaar_number.isEmpty() || DOB.isEmpty())
                {
                    errorText.setText("*Fields cannot be empty");
                    DOB_query.requestFocus();
                }
                else if(!Patterns.PHONE.matcher(contactNumber).matches())
                {
                    errorText.setText("Enter a valid contact number");
                    contactNumber_query.requestFocus();
                }
                else
                {
                    progressBar.setVisibility(ProgressBar.VISIBLE);
                    errorText.setText("");
                    saveUserData();
                }
            }
        });
    }
    private void saveUserData()
    {
        if(!isInternetAvailable())
        {
            Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(ProgressBar.INVISIBLE);
        }
        else
        {
            User user = new User(name, contactNumber, DOB, address, aadhaar_number);
            userDatabaseReference.child(currentUserUID).setValue(user);
            Toast.makeText(getApplicationContext(), "Saving", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(ProgressBar.INVISIBLE);

            Intent intent = new Intent(ProfileActivity.this , DisplayActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    private boolean isInternetAvailable()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return ( cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting() );
    }
}
