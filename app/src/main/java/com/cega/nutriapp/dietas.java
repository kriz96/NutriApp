package com.cega.nutriapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class dietas extends AppCompatActivity {
    TextView tvDia,tvDesayuno,tvComida,tvCena;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dietas);
        tvDia = (TextView) findViewById(R.id.dia);
        tvDesayuno = (TextView) findViewById(R.id.aqui_desayuno);
        tvComida = (TextView) findViewById(R.id.aqui_comida);
        tvCena = (TextView) findViewById(R.id.aqui_cena);
        btnBack = (Button) findViewById(R.id.back);
    }

    public void regresarMain(View view){
        //regresar al main activity
        Intent intent = new Intent (view.getContext(), MainActivity.class);
        startActivityForResult(intent, 0);

    }
}
