package com.cega.nutriapp;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.spec.ECField;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements
        SensorEventListener, StepListener,
        NavigationView.OnNavigationItemSelectedListener,
        CuadroDialog.StartStep{
    private static final String TAG = "SensorEvent";
    private static final String STEPS = "Counted_STEP";
    public static final String INFO = "user_credencial";

    private TextView stp, upCal, upCalTol;
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private int numSteps;
    private int sleep_stp;
    private Double peso;
    private Double cal;

    /* Cronometro */
    private BroadcastReceiver threadcrono;
    private int time;

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_drawer);

        upCal = findViewById(R.id.cal);
        upCalTol = findViewById(R.id.caltotal);

        cargarSteps();

        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        stp = findViewById(R.id.step_count);

        drawerLayout = findViewById(R.id.drawer_ly);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.open_draw, R.string.close_draw);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        // Traer instancia del sensor
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);

        // Iniciar sensor al abrir
        openDialog();

        // Actualizar Cal Totales
        loadCalPref();

        // Notificaion

        activeNotification();

    }


    private void activeNotification() {
        Calendar calendar = Calendar.getInstance();
        PendingIntent pi = PendingIntent.getService(this, 0,
                new Intent(this, Clima.class),PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_HALF_HOUR, pi);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(STEPS, numSteps);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        numSteps = savedInstanceState.getInt(STEPS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void step(long timeNs) {
        numSteps++;
        stp.setText(String.valueOf(numSteps));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_start:
                startSensor();
                // Tiempo de Actividad
                updateTime();
                break;
            case R.id.action_stop:
                stopTime();
                sensorManager.unregisterListener(MainActivity.this);
                break;
            case R.id.action_restart:
                time = 0;
                numSteps = 0;
                Toast.makeText(this, "nosirvo xd", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_info:
                Intent info = new Intent(this, QuestionActivity.class);
                startActivity(info);
                //Toast.makeText(this,"nosirvo xd", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_help:
                Toast.makeText(this, "nosirvo xd", Toast.LENGTH_SHORT).show();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    private void sleep() {
        SharedPreferences sleep = getSharedPreferences("Pasos_Descanso", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sleep.edit();
        editor.putInt("sleep_step", numSteps);
        editor.commit();
    }

    public void cargarSteps() {
        SharedPreferences sleep = getSharedPreferences("Pasos_Descanso", Context.MODE_PRIVATE);
        sleep_stp = sleep.getInt("sleep_step", numSteps);
        numSteps = sleep_stp;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        sleep();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        sensorManager.unregisterListener(MainActivity.this);
        super.onDestroy();
    }

    public void updateTime() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        threadcrono = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                time++;
                Log.d("cronometro", "Min: "+String.valueOf(time));
                calorias();
                setUpCalPref();
            }
        };

        registerReceiver(threadcrono, intentFilter);
    }

    private void stopTime() {
        unregisterReceiver(threadcrono);
    }

    public void calorias(){
       try {
           // Datos Usuario
           SharedPreferences info = getSharedPreferences(INFO, Context.MODE_PRIVATE);
           peso = Double.parseDouble(info.getString("peso",""));
           cal = 0.029 * peso * 2.2 * time;
           double floor = Math.floor(cal);
           Log.d("cronometro", "Cal: " +String.format("%.0f", floor));
           upCal.setText(String.valueOf(floor));
       } catch (Exception e){

       }
    }

    public void setUpCalPref(){
        SharedPreferences info = getSharedPreferences("Calorias", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = info.edit();

        String total = String.valueOf(Math.floor(cal));

        editor.putString("totales", total);
        editor.commit();

    }

    public void loadCalPref(){
        SharedPreferences info = getSharedPreferences("Calorias", Context.MODE_PRIVATE);
        String total = info.getString("totales","0");
        Double calTotal = Double.parseDouble(total);

        upCalTol.setText(String.valueOf("Ultima Sesión: "+Math.floor(calTotal)));
    }

    public void openDialog(){
        CuadroDialog cuadroDialog = new CuadroDialog();
        cuadroDialog.show(getSupportFragmentManager(), "Dialogo");
    }

    public void startSensor(){
        sensorManager.registerListener(MainActivity.this, accel, SensorManager.SENSOR_DELAY_FASTEST);
        Toast.makeText(this, "Comencemos!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void action(boolean start) {
        if(start){
            startSensor();
            updateTime();
            sensorManager.registerListener(MainActivity.this, accel, SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            Toast.makeText(this, "Abre el menú para iniciar", Toast.LENGTH_LONG).show();
        }
    }

}
