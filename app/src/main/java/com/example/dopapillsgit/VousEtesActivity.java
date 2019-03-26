package com.example.dopapillsgit;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.database.DatabaseReference;

public class VousEtesActivity extends AppCompatActivity {

    /********************************** Attributs de la classe*************************************/


    /**********************************Variables****************************************/
    public EditText editTextAgeId,editTextPoidsId,editTextTailleId;
    Button btnSuivant,btnSexe;
    ToggleButton toggleHomme,toggleFemme;
    private DatabaseReference mDatabase;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vousetes);

        /********************************** Initialisation des attributs*************************************/


        /**********************************Variables****************************************/
        editTextAgeId = findViewById(R.id.age);
        editTextPoidsId= findViewById(R.id.poids);
        editTextTailleId= findViewById(R.id.taille);
        btnSuivant = findViewById(R.id.buttonVousEtes);

        toggleHomme = (ToggleButton) findViewById(R.id.button_homme);
        toggleHomme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btnSexe= findViewById(R.id.button_homme);
                } else {
                    // The toggle is disabled
                }
            }
        });

        toggleFemme = (ToggleButton) findViewById(R.id.button_femme);
        toggleFemme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btnSexe= findViewById(R.id.button_femme);
                } else {

                }
            }
        });

        btnSuivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userAge = editTextAgeId.getText().toString();
                final String userPoids = editTextPoidsId.getText().toString();
                final String userTaille = editTextTailleId.getText().toString();
                final String userSexe = btnSexe.getText().toString();
                if (userAge.isEmpty()) {
                    editTextAgeId.setError("Veuillez entrer votre âge!");
                    editTextAgeId.requestFocus();
                } else if (userPoids.isEmpty()) {
                    editTextPoidsId.setError("Veuillez entrer votre poids!");
                    editTextPoidsId.requestFocus();
                }else if(userTaille.isEmpty()){
                    editTextTailleId.setError("Veuillez entrer votre taille!");
                    editTextTailleId.requestFocus();
                } else if (userAge.isEmpty() && userPoids.isEmpty()&&userTaille.isEmpty()) {
                    Toast.makeText(VousEtesActivity.this, "Fields Empty!", Toast.LENGTH_SHORT).show();
                } else if (!(userAge.isEmpty() && userPoids.isEmpty()&&userTaille.isEmpty())) {
                    Intent i = getIntent();
                    String strnom = "";
                    String strprenom = "";
                    if(i !=null){
                        if (i.hasExtra("edittextnom")&&i.hasExtra("edittextprenom")){
                            strnom = i.getStringExtra("edittextnom");
                            strprenom=i.getStringExtra("edittextprenom");
                        }
                    }

                    Intent intent =new Intent(VousEtesActivity.this, InscriptionActivity.class);
                    intent.putExtra(InscriptionActivity.POIDS, userPoids);
                    intent.putExtra(InscriptionActivity.TAILLE, userTaille);
                    intent.putExtra(InscriptionActivity.SEXE, userSexe);
                    intent.putExtra(InscriptionActivity.AGE, userAge);
                    intent.putExtra(InscriptionActivity.NOM, strnom);
                    intent.putExtra(InscriptionActivity.PRENOM, strprenom);
                    startActivity(intent);

                }
            }
        });
    }
}
