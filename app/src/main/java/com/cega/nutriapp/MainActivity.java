package com.cega.nutriapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

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

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        stp = findViewById(R.id.step_count);

        in = new Intent(getApplicationContext(), StepService.class);
        registerReceiver(broadcastReceiver, new IntentFilter(StepService.BROADCAST_ACTION));
        isServiceStopped = false;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_start:
                start(true);
                break;
            case R.id.action_stop:
                start(false);
                break;
            case R.id.action_rest:
                Toast.makeText(this,"nosirvo xd",Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
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

    public void start(boolean aux){
        if(aux){
            startService(new Intent(getBaseContext(), StepService.class));
        } else {
            stopService(new Intent(getBaseContext(), StepService.class));
        }

    }



}
