package com.cega.nutriapp;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;


public class StepCounter extends Service implements SensorEventListener {

    private int count;
    private SensorManager sensorManager;
    private Sensor countSensor;
    private Steps stp;


    @Override
    public void onCreate() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        stp = new Steps();

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Iniciar_Sensores();
        return START_STICKY;
    }


    // Metodo para iniciar el acceso a los sensores
    protected void Iniciar_Sensores() {
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER),
                SensorManager.SENSOR_DELAY_UI);
    }

    // Metodo para parar la escucha de los sensores
    private void Parar_Sensores() {
        sensorManager.unregisterListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER));
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Parar_Sensores();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        synchronized (this){
            switch (event.sensor.getType()){
                case Sensor.TYPE_STEP_COUNTER:
                    Log.d("sensor00", String.valueOf((int)event.values[0]));
                    count = (int) event.values[0];
                    break;
            }
        }

    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
