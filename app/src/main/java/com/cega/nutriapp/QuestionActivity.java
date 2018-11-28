package com.cega.nutriapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class QuestionActivity extends AppCompatActivity {
    public static final String INFO = "user_credencial";

    private TextView nombre, edad, sexo, altura, cintura, peso, enfermedad, fisica;
    private String n,e,s,a,c,p,en,f;
    String name;
    String age;
    String sex;
    String alt;
    String cin;
    String pes;
    String enf;
    String fis;

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getSex() {
        return sex;
    }

    public String getAlt() {
        return alt;
    }

    public String getCin() {
        return cin;
    }

    public String getPes() {
        return pes;
    }

    public String getEnf() {
        return enf;
    }

    public String getFis() {
        return fis;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        nombre = findViewById(R.id.name);
        edad = findViewById(R.id.age);
        sexo = findViewById(R.id.sex);
        altura = findViewById(R.id.alt);
        cintura = findViewById(R.id.cintura);
        peso = findViewById(R.id.peso);
        enfermedad = findViewById(R.id.sick);
        fisica = findViewById(R.id.fisica);

        cargarPreferencias();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString("peso",pes);
        intent.putExtras(bundle);


    }

    public void cargarPreferencias(){
        SharedPreferences info = getSharedPreferences(INFO, Context.MODE_PRIVATE);

        name = info.getString("nombre","");
        age = info.getString("edad","");
        sex = info.getString("sexo","Importante");
        alt = info.getString("altura","");
        cin = info.getString("cintura","");
        pes = info.getString("peso","");
        enf = info.getString("enfermedad", "");
        fis = info.getString("fisica","");


        nombre.setHint(String.valueOf(name));
        edad.setHint(String.valueOf(age));
        sexo.setHint(String.valueOf(sex));
        altura.setHint(String.valueOf(alt));
        cintura.setHint(String.valueOf(cin));
        peso.setHint(String.valueOf(pes));
        enfermedad.setHint(String.valueOf(enf));
        fisica.setHint(String.valueOf(fis));


    }

    public void guardar(View view) {
        guardarPreferencias();
    }

    private void guardarPreferencias() {
        SharedPreferences info = getSharedPreferences(INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = info.edit();

        n = nombre.getText().toString();
        e = edad.getText().toString();
        s = sexo.getText().toString();
        a = altura.getText().toString();
        c = cintura.getText().toString();
        p = peso.getText().toString();
        en = enfermedad.getText().toString();
        f = fisica.getText().toString();

        editor.putString("nombre", n);
        editor.putString("edad", e);
        editor.putString("sexo", s);
        editor.putString("altura", a);
        editor.putString("cintura", c);
        editor.putString("peso", p);
        editor.putString("enfermedad", en);
        editor.putString("fisica",f);

        nombre.setHint(String.valueOf(n));
        edad.setHint(String.valueOf(e));
        sexo.setHint(String.valueOf(s));
        altura.setHint(String.valueOf(a));
        cintura.setHint(String.valueOf(c));
        peso.setHint(String.valueOf(p));
        enfermedad.setHint(String.valueOf(en));
        fisica.setHint(String.valueOf(f));

        editor.commit();
    }
}
