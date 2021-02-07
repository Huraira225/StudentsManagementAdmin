package com.example.adminloginactivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        final SharedPreferences preferences = getSharedPreferences("admin", Context.MODE_PRIVATE);
        final String checkLoginState=preferences.getString("isadminLogin","adminLogin");

        final Handler[] handler = {new Handler()};
        handler[0].postDelayed(new Runnable() {
            @Override
            public void run() {

                    if (checkLoginState.equals("adminLogin")) {
                        Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                        startActivity(intent);
                    }else if(checkLoginState.equals("adminLogout")){
                        Intent intent = new Intent(SplashScreenActivity.this, AdminLoginActivity.class);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(SplashScreenActivity.this, AdminLoginActivity.class);
                        startActivity(intent);
                    }

                }

        },2000);

    }

}
