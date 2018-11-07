package com.cega.nutriapp;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private Toolbar toolbar;
    private int count;
    private SensorManager sensorManager;
    private TextView stp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        iniciar_Sensores();
        stp = findViewById(R.id.steps_count);


    }

    // Metodo para iniciar el acceso a los sensores
    protected void iniciar_Sensores() {
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER),
                SensorManager.SENSOR_DELAY_UI);
    }

    // Metodo para parar la escucha de los sensores
    private void parar_Sensores() {
        sensorManager.unregisterListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER));
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        iniciar_Sensores();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        synchronized (this){
            switch (event.sensor.getType()){
                case Sensor.TYPE_STEP_COUNTER:
                    Log.d("sensor00", String.valueOf((int)event.values[0]));
                    count = (int) event.values[0];
                    stp.setText(String.valueOf(count));
                    break;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


}
