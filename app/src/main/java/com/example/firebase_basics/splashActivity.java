package com.example.firebase_basics;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class splashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        Thread background = new Thread()
        {
            public void run()
            {
                try
                {
                    Thread.sleep(3000);
                    Intent intent = new Intent(splashActivity.this, loginActivity.class);
                    startActivity(intent);
                    finish();
                }
                catch (Exception e)
                {
                    Log.e("SplashScreen : " , "Thread error ");
                }
            }
        };

        background.start();
    }
}
