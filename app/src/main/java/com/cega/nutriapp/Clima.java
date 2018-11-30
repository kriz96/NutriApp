package com.cega.nutriapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.cega.nutriapp.NotifyApp.CHANNEL_ID;

public class Clima extends Service {
    private static final int ID = 1;

    public Clima() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String url = "http://api.openweathermap.org/data/2.5/weather?id=3996069&APPID=dd92793240681be550eba15e28ee7961&units=metric";
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject main_object = response.getJSONObject("main");
                    JSONArray array = response.getJSONArray("weather");
                    JSONObject object = array.getJSONObject(0);
                    Double temp = main_object.getDouble("temp");
                    int description = object.getInt("id");

                    Log.d("clima", String.valueOf(description));
                    conditions(temp, description);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jor);
        return START_STICKY;
    }

    private void conditions(Double temp, int description) {
        if (description >= 200 && description <= 232) {
            createNotify("Parece ser un dia tormentoso, que tal un descanso?.");
        } else if (description > 800) {
            if (description == 803 || description == 804) {
                createNotify("Está nublado, deberías coger un paraguas.\n");
            } else if (description == 801 || description == 802) {
                createNotify("Hay pocas nubes aprovecha  y sal a caminar gordis.");
            }
        } else if (description == 701 || description == 741 || description == 721) {
            createNotify("Hay niebla, creo que estas entrando a Silent Hill, mejor corre.");
        } else if (description == 711) {
            createNotify("Alerta roja entrando a chernobyl.");
        } else if (description == 731 || description == 761) {
            createNotify("Si no eres un polvorón evita salir de casa.");
        } else if (description == 751) {
            createNotify("Estilo de tierra, jutsu tormenta de arena");
        } else if (description >= 503 && description <= 531) {
            createNotify("Está lloviendo, disfruta el placer de estar encerrado.");
        } else if (description >= 300 && description <= 321 || description == 500 || description == 501) {
            createNotify("Parece haber un poco de lluvia, te gusta caminar bajo la lluvia?.");
        } else if (description == 800) {
            if (temp <= 15.0) {
                createNotify("Que frío hace y si hacemos un muñeco?.");
            } else if (temp >= 35.0) {
                createNotify("Temperaturas altas, toma precauciones.");
            } else {
                createNotify("Hoy está despejado, es día perfecto para bajar calorías sin pretextos.");
            }
        }
    }

    public void createNotify(String texto) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, CHANNEL_ID);
        notification.setAutoCancel(true);

        notification.setSmallIcon(R.drawable.ic_launcher_logo);
        notification.setTicker("NutriApp");
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle("NutriApp ");
        notification.setContentText(texto);
        notification.setStyle(new NotificationCompat.BigTextStyle()
                .bigText(texto));
        notification.setContentIntent(pendingIntent);
        notification.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notification.setLights(Color.GREEN, 1000, 1000);
        notification.setVibrate(new long[]{0, 100, 100});
        notification.setDefaults(Notification.DEFAULT_SOUND);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(ID, notification.build());
    }

}
