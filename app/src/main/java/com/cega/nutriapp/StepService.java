package com.cega.nutriapp;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class StepService extends Service implements SensorEventListener {

    int currentStepsDetected;
    Sensor stepCounterSensor;
    Sensor stepDetectorSensor;


    int stepCounter;
    int newStepCounter;

    boolean serviceStopped;
    private SensorManager sensorManager;

    Intent intent;
    // A string that identifies what kind of action is taking place.
    private static final String TAG = "StepService";
    public static final String BROADCAST_ACTION = "com.cega.nutriapp.steps";
    // Create a handler - that will be used to broadcast our data, after a specified amount of time.
    private final Handler handler = new Handler();
    // Declare and initialise counter - for keeping a record of how many times the service carried out updates.

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "Servicio creado...");
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        synchronized (this){
            if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
                newStepCounter = (int) event.values[0];
            }
            Log.v("Service Counter", String.valueOf(newStepCounter));
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Servicio iniciado...");

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        sensorManager.registerListener(this, stepCounterSensor, 0);

        newStepCounter = 0;

        serviceStopped = false;


        handler.removeCallbacks(updateBroadcastData);
        // call our handler with or without delay.
        handler.post(updateBroadcastData); // 0 seconds

        return START_STICKY;
    }

    private Runnable updateBroadcastData = new Runnable() {
        public void run() {
            if (!serviceStopped) {
                broadcastSensorValue();
                handler.postDelayed(this, 1000);
            }
        }
    };

    private void broadcastSensorValue() {
        Log.d(TAG, "Data to Activity");
        // add step counter to intent.
        intent.putExtra("Counted_Step_Int", newStepCounter);
        intent.putExtra("Counted_Step", String.valueOf(newStepCounter));
        // call sendBroadcast with that intent  - which sends a message to whoever is registered to receive it.
        sendBroadcast(intent);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        serviceStopped = true;

        Log.d(TAG, "Servicio destruido...");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}