package com.ewo.skripsi.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.ewo.skripsi.R;
import com.ewo.skripsi.utilities.GPSTracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;


public class LocationPickerActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    Button btnSelect;
    GPSTracker gpsTracker;
    CardView btnFixedGPS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_place_picker);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Pilih Titik Koordinat Lokasi");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        gpsTracker = new GPSTracker(this);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));
        btnSelect = findViewById(R.id.btnSelectLocation);
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng location = mMap.getCameraPosition().target;
//                Toast.makeText(LocationPickerActivity.this, String.valueOf(location.latitude+"-"+location.longitude), Toast.LENGTH_SHORT).show();
//                mMap.addMarker(new MarkerOptions().position(location).title("wow"));
                Intent resultIntent = new Intent();
                resultIntent.putExtra("latitude", String.valueOf(location.latitude));
                resultIntent.putExtra("longitude", String.valueOf(location.longitude));
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });

        btnFixedGPS = findViewById(R.id.btnFixedGPS);
        btnFixedGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GPSTracker newgps = new GPSTracker(LocationPickerActivity.this);
                LatLng newLoc = new LatLng(newgps.getLatitude(), newgps.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLng(newLoc));
            }
        });
    }




}
