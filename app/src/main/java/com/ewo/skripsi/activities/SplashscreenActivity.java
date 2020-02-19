package com.ewo.skripsi.activities;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ewo.skripsi.R;
import com.ewo.skripsi.utilities.AppConstants;
import com.ewo.skripsi.utilities.SharedPrefManager;

import java.util.UUID;


public class SplashscreenActivity extends AppCompatActivity {

    MaterialDialog materialDialog;
    SharedPrefManager sharedPrefManager;

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(SplashscreenActivity.this, MainActivity.class);
            SplashscreenActivity.this.startActivity(intent);
            SplashscreenActivity.this.finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        cek_permit();

        String requiredPermission = "android.permission.ACCESS_FINE_LOCATION";
        int checkVal = SplashscreenActivity.this.checkCallingOrSelfPermission(requiredPermission);

        if (checkVal==PackageManager.PERMISSION_GRANTED){

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(SplashscreenActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }, 1000);

        }



    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent i = new Intent(SplashscreenActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();

                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            }
        }
    }

    void cek_permit(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    AppConstants.LOCATION_REQUEST);

        } else {

        }
    }



}

