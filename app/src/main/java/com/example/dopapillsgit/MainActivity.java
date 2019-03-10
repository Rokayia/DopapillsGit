package com.example.dopapillsgit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button btn_seconnecter, btn_sinscrire;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_seconnecter = (Button) findViewById(R.id.boutonseconnecter);
        btn_sinscrire = (Button) findViewById(R.id.boutonsinscrire);


        btn_sinscrire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentInscription = new Intent(MainActivity.this, BienvenueActivity.class);
                startActivity(intentInscription);
            }
        });

        btn_seconnecter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentConnexion = new Intent(MainActivity.this, ConnexionActivity.class);
                startActivity(intentConnexion);
            }

        });

    }
}
