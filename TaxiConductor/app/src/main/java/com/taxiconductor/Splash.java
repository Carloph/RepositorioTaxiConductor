package com.taxiconductor;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {

    private static long TIME_SPLASH_SCREEN = 5000;
    private final int REQUEST_ID_ACCESS_COURSE_FINE_LOCATION = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= 23) {
                    int accessCoarsePermission
                            = ContextCompat.checkSelfPermission(Splash.this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
                    int accessFinePermission
                            = ContextCompat.checkSelfPermission(Splash.this, android.Manifest.permission.ACCESS_FINE_LOCATION);
                    Intent intent_ini = new Intent(Splash.this, Login.class);
                    startActivity(intent_ini);
                    if (accessCoarsePermission != PackageManager.PERMISSION_GRANTED
                            || accessFinePermission != PackageManager.PERMISSION_GRANTED) {
                        // The Permissions to ask user.
                        String[] permissions = new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                android.Manifest.permission.ACCESS_FINE_LOCATION};
                        // Show a dialog asking the user to allow the above permissions.
                        ActivityCompat.requestPermissions(Splash.this, permissions,
                                REQUEST_ID_ACCESS_COURSE_FINE_LOCATION);
                    }
                }
                finish();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, TIME_SPLASH_SCREEN);
    }
}
