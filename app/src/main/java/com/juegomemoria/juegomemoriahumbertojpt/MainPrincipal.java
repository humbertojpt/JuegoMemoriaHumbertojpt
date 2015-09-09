package com.juegomemoria.juegomemoriahumbertojpt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by Client on 9/5/2015.
 */
public class MainPrincipal extends AppCompatActivity {
    public int i=0, encontrada=0, turnos=0, vecesjugadas;
    protected SharedPreferences mSharedPreferences;
    protected String skey = "Puntaje";
    protected String juegos = "juegos";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityprincipal);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mSharedPreferences.getString(skey, "0");
        mSharedPreferences.getInt(juegos, 0);
        if(mSharedPreferences.getInt(juegos, 0)!=0){
            vecesjugadas = mSharedPreferences.getInt(juegos, 0);
            vecesjugadas++;
        }else{
            vecesjugadas=1;
            Intent intent1 = new Intent(MainPrincipal.this, DialogActivity.class);
            startActivity(intent1);
        }
        SharedPreferences.Editor editor1 = mSharedPreferences.edit();
        editor1.putInt(juegos, vecesjugadas);
        editor1.commit();
    }

    public void facil(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void intermedio(View view) {
        Intent intent = new Intent(this, Activity6.class);
        startActivity(intent);
    }
    public void dificil(View view) {
        Intent intent = new Intent(this, ActivityDificult.class);
        startActivity(intent);
    }
}
