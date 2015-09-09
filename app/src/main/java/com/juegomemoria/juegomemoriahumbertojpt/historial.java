package com.juegomemoria.juegomemoriahumbertojpt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class historial extends AppCompatActivity {
    private TextView puntajeTextView, vecesjugadasTextView;
    protected SharedPreferences mSharedPreferences;
    protected String skey = "Puntaje";
    protected String juegos = "juegos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);
        puntajeTextView = (TextView) findViewById(R.id.textView4);
        vecesjugadasTextView = (TextView) findViewById(R.id.textView);
        //Intent intent = getIntent();
       // String message = intent.getStringExtra(MainActivity.encontrada);
        mSharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        String text = mSharedPreferences.getString(skey, "0");
        int vecesjugadas = mSharedPreferences.getInt(juegos, 0);
        puntajeTextView.setText(text);
        vecesjugadasTextView.setText(vecesjugadas+"");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_historial, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public void back (View view){
        historial.this.finish();
    }
}
