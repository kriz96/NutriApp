package com.cega.nutriapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "SensorEvent";

    private Toolbar toolbar;
    private TextView stp;
    private Intent in;
    private boolean isServiceStopped;
    private String countedStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        in = new Intent(
                getApplicationContext(), StepService.class);


        stp = findViewById(R.id.step_count);


        startService(new Intent(getBaseContext(), StepService.class));

        registerReceiver(broadcastReceiver, new IntentFilter(StepService.BROADCAST_ACTION));
        isServiceStopped = false;

    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            updateViews(intent);
        }
    };

    private void updateViews(Intent intent) {
        // retrieve data out of the intent.
        countedStep = intent.getStringExtra("Counted_Step");
        Log.d(TAG, String.valueOf(countedStep));

        stp.setText(String.valueOf(countedStep));

    }


}
