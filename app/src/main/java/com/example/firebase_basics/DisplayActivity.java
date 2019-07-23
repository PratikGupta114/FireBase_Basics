package com.example.firebase_basics;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DisplayActivity extends AppCompatActivity
{
    private TextView display_name, display_address, display_contact, display_DOB, display_aadhaar;
    private String name, address, contact, DOB, aadhaar;
    private FirebaseDatabase userDatabase;
    private DatabaseReference userDatabaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private Button logoutButton, editButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        // check if the current user is not authenticated !
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        if(currentUser == null)
        {
            Intent intent = new Intent(DisplayActivity.this , loginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        display_name = findViewById(R.id.tv_nameValue);
        display_address = findViewById(R.id.tv_addressValue);
        display_aadhaar = findViewById(R.id.tv_aadhaarValue);
        display_contact = findViewById(R.id.tv_contactValue);
        display_DOB = findViewById(R.id.tv_DOBValue);
        logoutButton = findViewById(R.id.bt_logoutButton);
        editButton = findViewById(R.id.bt_editButton);

        display_name.setText("");
        display_DOB.setText("");
        display_contact.setText("");
        display_aadhaar.setText("");
        display_address.setText("");

        // if the user is authenticated then create a database reference and retrieve the data.
        userDatabase = FirebaseDatabase.getInstance();
        userDatabaseReference = userDatabase.getReference();

        userDatabaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                User user = dataSnapshot.child(currentUser.getUid()).getValue(User.class);

                name = user.getUserName();
                address = user.getUserAddress();
                contact = user.getUserContactNumber();
                DOB = user.getUserDOB();
                aadhaar = user.getUserAadhaarNumber();

                display_name.setText(name);
                display_aadhaar.setText(aadhaar);
                display_address.setText(address);
                display_contact.setText(contact);
                display_DOB.setText(DOB);

                Log.i("DisplayActivity", " Message Received !" );
                Log.i("DisplayActivity", "Username : "+name );
                Log.i("DisplayActivity", "Address : "+address );
                Log.i("DisplayActivity", "DOB : "+DOB );
                Log.i("DisplayActivity", "Contact : "+contact );
                Log.i("DisplayActivity", "Aadhaar : "+aadhaar );
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                if(!isInternetAvailable())
                {
                    Toast.makeText(getApplicationContext() , "Check your internet connection" , Toast.LENGTH_SHORT).show();
                    Log.e("DisplayActivity","Lost internet connectivity");
                }
                else
                {
                    Toast.makeText(getApplicationContext() , "Unable to retrieve Data" , Toast.LENGTH_SHORT).show();
                    Log.e("DisplayActivity","Unable to retrieve data from Firebase");
                }
            }
        });

        // logout button functionality
        logoutButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                firebaseAuth.signOut();
                Intent intent = new Intent(DisplayActivity.this , loginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        // edit button functionality
        editButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });

    }

    private boolean isInternetAvailable()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return ( cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting() );
    }
}
