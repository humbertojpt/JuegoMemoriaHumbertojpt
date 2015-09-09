package com.juegomemoria.juegomemoriahumbertojpt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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

public class ActivityDificult extends AppCompatActivity implements View.OnClickListener, SensorEventListener {

    private Button botondificult[] = new Button[16];
    private int parejasdificult[];
    private int pairsRandom[]= new int[16];
    private int pardif[] = new int[2];
    private Button botoncompara[] =new Button[2];
    private TextView text,textEncon;
    public int i=0, encontrada=0, turnos=0, vecesjugadas;
    MediaPlayer mp ;
    protected SharedPreferences mSharedPreferences;
    protected String skey = "Puntaje";
    protected String juegos = "juegos";

    private static final String [] BUTTONS_STATES={"button_1","button_2","button_3","button_4","button_5","button_6",
            "button_7","button_8","button_9","button_10","button_11","button_12",
            "button_13","button_14","button_15","button_16"};
    private static final String BUTTON_CHECK_TEXT="btnChk1Txt";
    private static final String BUTTON_CHECK_ID="btnID";
    private static final String RANDOM_NUMBERS="randomNumbers";
    private static final String TV_MESSAGE="tvMessage";
    private static final String TV_WIN="tvWin";
    private static final String VALUE_I="valueI";
    private static final String CHECK="check";
    private static final String PAIRS="pairs";

    //declaracion para los sensores
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
        setContentView(R.layout.activity_dificult);
        text=(TextView)findViewById(R.id.textView);
        textEncon=(TextView)findViewById(R.id.encontradas);
        for(int i=0;i<16;i++) {
            botondificult[i] = (Button) findViewById(R.id.btn + i);
            botondificult[i].setBackgroundColor(Color.rgb(random(0, 100), random(0, 100), random(0, 100)));
        }
        for(int i=0;i<16;i++)
            botondificult[i].setOnClickListener(this);

        mp = MediaPlayer.create(this, R.raw.audio2);
        mp.start();

        parejasdificult =ParejasRandom(pairsRandom);

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
            pardif[0]=savedInstanceState.getInt(CHECK);
            encontrada=savedInstanceState.getInt(PAIRS);
            for(int i=0;i< botondificult.length;i++) {
                if(savedInstanceState.getInt(BUTTONS_STATES[i])==View.INVISIBLE){
                    botondificult[i].setVisibility(View.INVISIBLE);
                }else{
                    botondificult[i].setVisibility(View.VISIBLE);
                }
                if(savedInstanceState.getInt(BUTTON_CHECK_ID)== botondificult[i].getId()){
                    botoncompara[0]= botondificult[i];
                    botoncompara[0].setText(savedInstanceState.getString(BUTTON_CHECK_TEXT));
                }
            }
            parejasdificult =savedInstanceState.getIntArray(RANDOM_NUMBERS);
            textEncon.setText(savedInstanceState.getString(TV_MESSAGE));
            text.setText(savedInstanceState.getString(TV_WIN));
        }
    }

    public static int random(int min, int max) {
        // Usually this can be a field rather than a method variable
        Random rand = new Random();
        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
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
                int v=random(0, tableSize.length - 1);
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
        for(int i=0;i< botondificult.length;i++){
            saveInstanceState.putInt(BUTTONS_STATES[i], botondificult[i].getVisibility());
        }
        if(botoncompara[0]!=null){
            saveInstanceState.putString(BUTTON_CHECK_TEXT, botoncompara[0].getText().toString());
            saveInstanceState.putInt(BUTTON_CHECK_ID, botoncompara[0].getId());
        }
        saveInstanceState.putIntArray(RANDOM_NUMBERS, parejasdificult);
        saveInstanceState.putString(TV_MESSAGE,textEncon.getText().toString());
        saveInstanceState.putInt(VALUE_I,i);
        saveInstanceState.putInt(CHECK, pardif[0]);
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
                mp.stop();
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
                    botondificult[0].setText(String.valueOf((parejasdificult[0])));
                    pardif[i] = (parejasdificult[0]);
                    botoncompara[i] = botondificult[0];
                    break;
                case R.id.btn1:
                    botondificult[1].setText(String.valueOf(parejasdificult[1]));
                    pardif[i] = parejasdificult[1];
                    botoncompara[i] = botondificult[1];
                    break;
                case R.id.btn2:
                    botondificult[2].setText(String.valueOf(parejasdificult[2]));
                    pardif[i] = parejasdificult[2];
                    botoncompara[i] = botondificult[2];
                    break;
                case R.id.btn3:
                    botondificult[3].setText(String.valueOf(parejasdificult[3]));
                    pardif[i] = parejasdificult[3];
                    botoncompara[i] = botondificult[3];
                    break;
                case R.id.btn4:
                    botondificult[4].setText(String.valueOf(parejasdificult[4]));
                    pardif[i] = parejasdificult[4];
                    botoncompara[i] = botondificult[4];
                    break;
                case R.id.btn5:
                    botondificult[5].setText(String.valueOf(parejasdificult[5]));
                    pardif[i] = parejasdificult[5];
                    botoncompara[i] = botondificult[5];
                    break;
                case R.id.btn6:
                    botondificult[6].setText(String.valueOf(parejasdificult[6]));
                    pardif[i] = parejasdificult[6];
                    botoncompara[i] = botondificult[6];
                    break;
                case R.id.btn7:
                    botondificult[7].setText(String.valueOf(parejasdificult[7]));
                    pardif[i] = parejasdificult[7];
                    botoncompara[i] = botondificult[7];
                    break;
                case R.id.btn8:
                    botondificult[8].setText(String.valueOf(parejasdificult[8]));
                    pardif[i] = parejasdificult[8];
                    botoncompara[i] = botondificult[8];
                    break;
                case R.id.btn9:
                    botondificult[9].setText(String.valueOf(parejasdificult[9]));
                    pardif[i] = parejasdificult[9];
                    botoncompara[i] = botondificult[9];
                    break;
                case R.id.btn10:
                    botondificult[10].setText(String.valueOf(parejasdificult[10]));
                    pardif[i] = parejasdificult[10];
                    botoncompara[i] = botondificult[10];
                    break;
                case R.id.btn11:
                    botondificult[11].setText(String.valueOf(parejasdificult[11]));
                    pardif[i] = parejasdificult[11];
                    botoncompara[i] = botondificult[11];
                    break;
                case R.id.btn12:
                    botondificult[12].setText(String.valueOf(parejasdificult[12]));
                    pardif[i] = parejasdificult[12];
                    botoncompara[i] = botondificult[12];
                    break;
                case R.id.btn13:
                    botondificult[13].setText(String.valueOf(parejasdificult[13]));
                    pardif[i] = parejasdificult[13];
                    botoncompara[i] = botondificult[13];
                    break;
                case R.id.btn14:
                    botondificult[14].setText(String.valueOf(parejasdificult[14]));
                    pardif[i] = parejasdificult[14];
                    botoncompara[i] = botondificult[14];
                    break;
                default:
                    botondificult[15].setText(String.valueOf(parejasdificult[15]));
                    pardif[i] = parejasdificult[15];
                    botoncompara[i] = botondificult[15];
                    break;
            }
            i++;
        }
        int sw = 0;
        if(i==2) {
            if (pardif[0] == pardif[1] && botoncompara[0].getId() != botoncompara[1].getId()) {
                encontrada++;
                //  Handler h = new Handler();
                //   h.postDelayed(new Runnable() {
                //       @Override
                //       public void run() {
                botoncompara[0].setVisibility(View.INVISIBLE);
                botoncompara[1].setVisibility(View.INVISIBLE);

                //     }
                //  },200);
            } else {
                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        botoncompara[0].setText("");
                        botoncompara[1].setText("");
                    }
                },40);

            }
            i = 0;
            turnos++;
            textEncon.setText("Encontradas: " + encontrada + " Turnos: " + turnos);
            if (turnos>=20){
                text.setText("Has perdido la partida!");
                for(int i=0; i<botondificult.length;i++){
                    botondificult[i].setVisibility(View.INVISIBLE);
                    sw = 1;
                }
            }
        }
        if (sw!=1){
            if(gameOver(botondificult)){
                text.setText("HAS GANADO!");
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString(skey, encontrada + "");
                editor.commit();
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