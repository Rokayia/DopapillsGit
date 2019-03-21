package com.example.dopapillsgit;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

public class podometre extends AppCompatActivity implements SensorEventListener {

    SensorManager sensorManager;
    TextView steps;
    Boolean running = false;

        @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);
        steps = (TextView) findViewById(R.id.steps);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    public void onResume (){
        super.onResume();
        running = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            if(countSensor !=null){
                sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        }
        else {
            Toast.makeText(this,"Aucun capteur trouvé !", Toast.LENGTH_SHORT).show();
            }
    }


    @Override
    protected void onPause() {
            super.onPause();
            running = false;
            // si on désactive l'enregistrement, le matériel cessera de détecter l'étape.
            //sensorManager/unregisterListerner(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (running) {
            steps.setText(String.valueOf(event.values[0]));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

