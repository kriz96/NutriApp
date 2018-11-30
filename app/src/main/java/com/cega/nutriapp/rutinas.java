package com.cega.nutriapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class rutinas extends AppCompatActivity {
    TextView tvRutina;
    Button btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutinas);
        tvRutina = (TextView) findViewById(R.id.rutina);
        btnBack = (Button) findViewById(R.id.button);
    }

    public void regresarMain(View view){
        //regresar al main activity
        Intent intent = new Intent (view.getContext(), MainActivity.class);
        startActivityForResult(intent, 0);
    }
}
