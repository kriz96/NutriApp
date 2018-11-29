package com.cega.nutriapp;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class Clima extends Service {

    private String temp;
    private String description;
    private String mainDescrip;

    private NotificationManager notificationManager;
    private static final int ID_NOTIFICATION = 1234;

    public Clima() {
    }

    @Override
    public void onCreate() {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        find_weather();
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    // Patron de vibracion
    long vibrate[] = {0, 100, 100};

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getBaseContext())
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle("Caminemos")
                .setContentText(description)
                .setVibrate(vibrate)
                .setWhen(System.currentTimeMillis());

        // Lanzar Notificacion
        notificationManager.notify(ID_NOTIFICATION, builder.build());

        return START_STICKY;
    }

    public void find_weather(){
        String url = "http://api.openweathermap.org/data/2.5/weather?q=Mexicali,mx&APPID=dd92793240681be550eba15e28ee7961&units=metric";

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject main_object = response.getJSONObject("main");
                    JSONArray array = response.getJSONArray("weather");
                    JSONObject object = array.getJSONObject(0);
                    temp = String.valueOf(main_object.getDouble("temp"));
                    description = object.getString("description");
                    mainDescrip = object.getString("main");

                } catch (Exception e){
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
    }

    @Override
    public void onDestroy() {
        notificationManager.cancel(ID_NOTIFICATION);
        super.onDestroy();
    }
}
