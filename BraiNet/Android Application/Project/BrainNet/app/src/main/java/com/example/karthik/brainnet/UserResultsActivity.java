package com.example.karthik.brainnet;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.os.BatteryManager;
import android.content.Context;

import static android.content.Context.BATTERY_SERVICE;

/**
 * Created by Karthik on 28-11-2017.
 */

public class UserResultsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.useractivityresults);
        String result = getIntent().getStringExtra("UserLabel");
        TextView view = (TextView) findViewById(R.id.UserLabel);
        view.setText(result);
        String execTime = getIntent().getStringExtra("FogExectionTime");
        view = (TextView) findViewById(R.id.fogTime);
        view.setText(execTime+" ms");
        view = (TextView) findViewById(R.id.battery);
        String battery = getIntent().getStringExtra("battery");
        view.setText(battery+" % ");




        //Slog.i(TAG, "Remaining energy = " + energy + "nWh");
    }



}

