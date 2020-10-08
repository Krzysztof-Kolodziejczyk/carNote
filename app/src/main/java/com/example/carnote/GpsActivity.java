package com.example.carnote;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;
import java.util.concurrent.TimeUnit;


// aktywnosc liczenia czasu potrzebnego na rozpedzenie auto od 0 do 100km/h
public class GpsActivity extends AppCompatActivity implements LocationListener {

    private int bestTimeto100;
    private int secTo100;
    private Date startTime;
    private TextView currentSpeed;
    private TextView bestSpeed;
    private Button button;
    private LocationManager locationManager;
    private boolean wasCounted;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.gps_layout);

        currentSpeed = (TextView) findViewById(R.id.current_speed);
        bestSpeed = (TextView) findViewById(R.id.best_speed);

        button = (Button) findViewById(R.id.button2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime = new Date();
                wasCounted = false;


            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);



        if(savedInstanceState != null)
        {
            // odzyskac ostatnie odczyty z  okna po ubiciu
            // odzyskajmy rekord
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    // na pierwszy plan
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500L, 2.0f, this);

    }


    @Override
    // na utratę pierwszego planu
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }


    @Override
    // na zapisanie stanu okna
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        // tu zapisac do bundla stan aktualnej pozycji
        // czy tez ostatnie odczyty
        // zapiszmy tu tez nasz rekord
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        float speed = location.getSpeed();
        float kmhspeed = speed*3600/1000;
        currentSpeed.setText("bieżąca prędkość " + kmhspeed + " km/h");

        if(kmhspeed >= 100 && !wasCounted)
        {
            long diffMill = new Date().getTime() - startTime.getTime();
            long diffMillSec = TimeUnit.MILLISECONDS.toSeconds(diffMill);
            bestSpeed.setText("Rekord ostatni " + diffMillSec + " s");
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
