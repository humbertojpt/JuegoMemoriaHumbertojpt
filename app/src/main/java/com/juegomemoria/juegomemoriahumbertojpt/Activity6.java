package com.juegomemoria.juegomemoriahumbertojpt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android.os.Handler;
import android.widget.Toast;

import java.util.Random;

public class Activity6 extends AppCompatActivity implements View.OnClickListener, SensorEventListener {

    private Button boton1[] = new Button[24];
    private int parejas1[];
    private int pairs[]= new int[24];
    private int pares[] = new int[2];
    private Button botoncomparacion[] =new Button[2];
    private TextView texto,textEncon;
    public int i=0, encontrada=0, turnos=0, vecesjugadas;
    MediaPlayer mp ;
    protected SharedPreferences mSharedPreferences;
    protected String skey = "Puntaje";
    protected String juegos = "juegos";

    private static final String [] BUTTONS_STATES={"button_1","button_2","button_3","button_4","button_5","button_6",
            "button_7","button_8","button_9","button_10","button_11","button_12",
            "button_13","button_14","button_15","button_16","button_17","button_18",
            "button_19","button_20","button_21","button_22","button_23","button_24"};
    private static final String BUTTON_CHECK_TEXT="btnChk1Txt";
    private static final String BUTTON_CHECK_ID="btnID";
    private static final String RANDOM_NUMBERS="randomNumbers";
    private static final String TV_MESSAGE="tvMessage";
    private static final String TV_WIN="tvWin";
    private static final String VALUE_I="valueI";
    private static final String CHECK="check";
    private static final String PAIRS="pairs";

    //Declaracion para los sensores
    private SensorManager mSensorManager;
    private Sensor mAcelSensor;
    private static final int MIN_SHAKE_ACCELERATION = 10;
    private float[] mGravity = { 0.0f, 0.0f, 0.0f };
    private float[] mLinearAcceleration = { 0.0f, 0.0f, 0.0f };
    private static final int X = 0;
    private static final int Y = 1;
    private static final int Z = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity6);
        texto=(TextView)findViewById(R.id.textView);
        textEncon=(TextView)findViewById(R.id.encontradas);
        for(int i=0;i<24;i++)
            boton1[i]=(Button)findViewById(R.id.btn+i);

        for(int i=0;i<24;i++)
            boton1[i].setOnClickListener(this);

        mp = MediaPlayer.create(this, R.raw.audio2);
        mp.start();

        parejas1 =ParejasRandom(pairs);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mSharedPreferences.getString(skey, "0");
        mSharedPreferences.getInt(juegos, 0);
        if(mSharedPreferences.getInt(juegos, 0)!=0){
            vecesjugadas = mSharedPreferences.getInt(juegos, 0);
            vecesjugadas++;
        }else{
            vecesjugadas=1;
        }
        SharedPreferences.Editor editor1 = mSharedPreferences.edit();
        editor1.putInt(juegos, vecesjugadas);
        editor1.commit();

        if(savedInstanceState != null){
            i=savedInstanceState.getInt(VALUE_I);
            pares[0]=savedInstanceState.getInt(CHECK);
            encontrada=savedInstanceState.getInt(PAIRS);
            for(int i=0;i<boton1.length;i++) {
                if(savedInstanceState.getInt(BUTTONS_STATES[i])==View.INVISIBLE){
                    boton1[i].setVisibility(View.INVISIBLE);
                }else{
                    boton1[i].setVisibility(View.VISIBLE);
                }
                if(savedInstanceState.getInt(BUTTON_CHECK_ID)==boton1[i].getId()){
                    botoncomparacion[0]=boton1[i];
                    botoncomparacion[0].setText(savedInstanceState.getString(BUTTON_CHECK_TEXT));
                }
            }
            parejas1 =savedInstanceState.getIntArray(RANDOM_NUMBERS);
            textEncon.setText(savedInstanceState.getString(TV_MESSAGE));
            texto.setText(savedInstanceState.getString(TV_WIN));
        }
    }

    public static int Random(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return randomNum;
    }

    public int [] ParejasRandom(int [] tableSize){
        int i=0;
        int p=1;
        int t=0;
        int k=0;
        int [] OPair=new int[tableSize.length];
        int [] pair=new int[tableSize.length];
        int [] auxTemp=new int[tableSize.length];
        while(i<tableSize.length) {
            int j = 0;
            while(j<2){
                int v=Random(0,tableSize.length-1);
                if(pair[v]!=0){
                    auxTemp[k]=p;
                    k++;
                }
                while(pair[v]==0 && t!=v || v==0 && t==0 && pair[v]==0){
                    pair[v]=p;
                    t=v;
                }
                j++;
            }
            if(i<(tableSize.length/2)-1){
                p++;
            }else{
                break;
            }
            i++;
        }
        int indice2=0;
        for(int indice=0;indice<auxTemp.length;indice++){
            if(pair[indice]!=0){
                OPair[indice]=pair[indice];
            }else if(pair[indice]==0){
                if(auxTemp[indice2]!=0){
                    OPair[indice]=auxTemp[indice2];
                    indice2++;
                }
            }
        }
        return OPair;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState){
        for(int i=0;i<boton1.length;i++){
            saveInstanceState.putInt(BUTTONS_STATES[i],boton1[i].getVisibility());
        }
        if(botoncomparacion[0]!=null){
            saveInstanceState.putString(BUTTON_CHECK_TEXT, botoncomparacion[0].getText().toString());
            saveInstanceState.putInt(BUTTON_CHECK_ID, botoncomparacion[0].getId());
        }
        saveInstanceState.putIntArray(RANDOM_NUMBERS, parejas1);
        saveInstanceState.putString(TV_MESSAGE,textEncon.getText().toString());
        saveInstanceState.putInt(VALUE_I,i);
        saveInstanceState.putInt(CHECK, pares[0]);
        saveInstanceState.putInt(PAIRS,encontrada);
        super.onSaveInstanceState(saveInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.restartapp:
                Log.d("veces jugadas", vecesjugadas + "");
                Intent intent=getIntent();
                finish();
                startActivity(intent);
                mp.stop();
                return true;
            case R.id.stopMusic:
                mp.stop();
                return true;
            case R.id.historial:
                Intent intent1 = new Intent(this, historial.class);
                startActivity(intent1);
                return true;
            case R.id.backprincipal:
                Intent intent2 = new Intent(this, MainPrincipal.class);
                startActivity(intent2);
                return true;
            case R.id.ayuda:
                Intent intent5 = new Intent(this, DialogActivity.class);
                startActivity(intent5);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public Boolean gameOver(Button [] buttons){
        for(int i=0; i<buttons.length;i++){
            if(buttons[i].getVisibility()==View.VISIBLE) {
                return false;
            }
        }
        return true;
    }
    @Override
    public void onClick(View view) {

        if(i<2) {
            switch (view.getId()) {
                case R.id.btn:
                    boton1[0].setText(String.valueOf((parejas1[0])));
                    pares[i] = (parejas1[0]);
                    botoncomparacion[i] = boton1[0];
                    break;
                case R.id.btn1:
                    boton1[1].setText(String.valueOf(parejas1[1]));
                    pares[i] = parejas1[1];
                    botoncomparacion[i] = boton1[1];
                    break;
                case R.id.btn2:
                    boton1[2].setText(String.valueOf(parejas1[2]));
                    pares[i] = parejas1[2];
                    botoncomparacion[i] = boton1[2];
                    break;
                case R.id.btn3:
                    boton1[3].setText(String.valueOf(parejas1[3]));
                    pares[i] = parejas1[3];
                    botoncomparacion[i] = boton1[3];
                    break;
                case R.id.btn4:
                    boton1[4].setText(String.valueOf(parejas1[4]));
                    pares[i] = parejas1[4];
                    botoncomparacion[i] = boton1[4];
                    break;
                case R.id.btn5:
                    boton1[5].setText(String.valueOf(parejas1[5]));
                    pares[i] = parejas1[5];
                    botoncomparacion[i] = boton1[5];
                    break;
                case R.id.btn6:
                    boton1[6].setText(String.valueOf(parejas1[6]));
                    pares[i] = parejas1[6];
                    botoncomparacion[i] = boton1[6];
                    break;
                case R.id.btn7:
                    boton1[7].setText(String.valueOf(parejas1[7]));
                    pares[i] = parejas1[7];
                    botoncomparacion[i] = boton1[7];
                    break;
                case R.id.btn8:
                    boton1[8].setText(String.valueOf(parejas1[8]));
                    pares[i] = parejas1[8];
                    botoncomparacion[i] = boton1[8];
                    break;
                case R.id.btn9:
                    boton1[9].setText(String.valueOf(parejas1[9]));
                    pares[i] = parejas1[9];
                    botoncomparacion[i] = boton1[9];
                    break;
                case R.id.btn10:
                    boton1[10].setText(String.valueOf(parejas1[10]));
                    pares[i] = parejas1[10];
                    botoncomparacion[i] = boton1[10];
                    break;
                case R.id.btn11:
                    boton1[11].setText(String.valueOf(parejas1[11]));
                    pares[i] = parejas1[11];
                    botoncomparacion[i] = boton1[11];
                    break;
                case R.id.btn12:
                    boton1[12].setText(String.valueOf(parejas1[12]));
                    pares[i] = parejas1[12];
                    botoncomparacion[i] = boton1[12];
                    break;
                case R.id.btn13:
                    boton1[13].setText(String.valueOf(parejas1[13]));
                    pares[i] = parejas1[13];
                    botoncomparacion[i] = boton1[13];
                    break;
                case R.id.btn14:
                    boton1[14].setText(String.valueOf(parejas1[14]));
                    pares[i] = parejas1[14];
                    botoncomparacion[i] = boton1[14];
                    break;
                case R.id.btn15:
                    boton1[15].setText(String.valueOf(parejas1[15]));
                    pares[i] = parejas1[15];
                    botoncomparacion[i] = boton1[15];
                    break;
                case R.id.btn16:
                    boton1[16].setText(String.valueOf(parejas1[16]));
                    pares[i] = parejas1[16];
                    botoncomparacion[i] = boton1[16];
                    break;
                case R.id.btn17:
                    boton1[17].setText(String.valueOf(parejas1[17]));
                    pares[i] = parejas1[17];
                    botoncomparacion[i] = boton1[17];
                    break;
                case R.id.btn18:
                    boton1[18].setText(String.valueOf(parejas1[18]));
                    pares[i] = parejas1[18];
                    botoncomparacion[i] = boton1[18];
                    break;
                case R.id.btn19:
                    boton1[19].setText(String.valueOf(parejas1[19]));
                    pares[i] = parejas1[19];
                    botoncomparacion[i] = boton1[19];
                    break;
                case R.id.btn20:
                    boton1[20].setText(String.valueOf(parejas1[20]));
                    pares[i] = parejas1[20];
                    botoncomparacion[i] = boton1[20];
                    break;
                case R.id.btn21:
                    boton1[21].setText(String.valueOf(parejas1[21]));
                    pares[i] = parejas1[21];
                    botoncomparacion[i] = boton1[21];
                    break;
                case R.id.btn22:
                    boton1[22].setText(String.valueOf(parejas1[22]));
                    pares[i] = parejas1[22];
                    botoncomparacion[i] = boton1[22];
                    break;
                default:
                    boton1[23].setText(String.valueOf(parejas1[23]));
                    pares[i] = parejas1[23];
                    botoncomparacion[i] = boton1[23];
                    break;
            }
            i++;
        }
        int sw=0;
        if(i==2) {
            if (pares[0] == pares[1] && botoncomparacion[0].getId() != botoncomparacion[1].getId()) {
                encontrada++;
                //  Handler h = new Handler();
                //   h.postDelayed(new Runnable() {
                //       @Override
                //       public void run() {
                botoncomparacion[0].setVisibility(View.INVISIBLE);
                botoncomparacion[1].setVisibility(View.INVISIBLE);
                //     }
                //  },200);
            } else {
                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        botoncomparacion[0].setText("");
                        botoncomparacion[1].setText("");
                    }
                },100);

            }
            i = 0;
            turnos++;
            textEncon.setText("Encontradas: " + encontrada + " Turnos: " + turnos);
            if (turnos>=60){
                texto.setText("Has perdido la partida!");
                for(int i=0; i<boton1.length;i++){
                    boton1[i].setVisibility(View.INVISIBLE);
                    sw=1;
                }
            }
        }
        if (sw!=1){
            if(gameOver(boton1)){
                texto.setText("HAS GANADO!");
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString(skey, encontrada+"");
                editor.commit();
                Log.d("main", encontrada + "");
            }
        }
    }

    //sensores
    public void startCapture() {
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        mAcelSensor = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mSensorManager.registerListener(this, mAcelSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stopCapturing() {
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this, mAcelSensor);
        } else {
            // Toast.makeText(getApplicationContext(),
            //       "mSensorManager null", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopCapturing();
        mp.stop();
    }

    @Override
    public void onResume() {
        super.onResume();
        startCapture();
    }

    @Override
    public void onStop() {
        mp.stop();
        super.onStop();
        stopCapturing();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        setCurrentAcceleration(event);
        float maxLinearAcceleration = getMaxCurrentLinearAcceleration();

        // Check if the acceleration is greater than our minimum threshold
        if (maxLinearAcceleration > MIN_SHAKE_ACCELERATION) {
            Toast.makeText(this, "SE reinicio el juego", Toast.LENGTH_SHORT).show();
            Intent intent=getIntent();
            finish();
            startActivity(intent);

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void setCurrentAcceleration(SensorEvent event) {


        final float alpha = 0.8f;

        // Gravity components of x, y, and z acceleration
        mGravity[X] = alpha * mGravity[X] + (1 - alpha) * event.values[X];
        mGravity[Y] = alpha * mGravity[Y] + (1 - alpha) * event.values[Y];
        mGravity[Z] = alpha * mGravity[Z] + (1 - alpha) * event.values[Z];

        // Linear acceleration along the x, y, and z axes (gravity effects removed)
        mLinearAcceleration[X] = event.values[X] - mGravity[X];
        mLinearAcceleration[Y] = event.values[Y] - mGravity[Y];
        mLinearAcceleration[Z] = event.values[Z] - mGravity[Z];

        /*
         *  END SECTION from Android developer site
         */
    }

    private float getMaxCurrentLinearAcceleration() {
        // Start by setting the value to the x value
        float maxLinearAcceleration = mLinearAcceleration[X];

        // Check if the y value is greater
        if (mLinearAcceleration[Y] > maxLinearAcceleration) {
            maxLinearAcceleration = mLinearAcceleration[Y];
        }

        // Check if the z value is greater
        if (mLinearAcceleration[Z] > maxLinearAcceleration) {
            maxLinearAcceleration = mLinearAcceleration[Z];
        }

        // Return the greatest value
        return maxLinearAcceleration;
    }
}