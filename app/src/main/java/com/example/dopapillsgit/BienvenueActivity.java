package com.example.dopapillsgit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

public class BienvenueActivity extends AppCompatActivity {
    /********************************** Attributs de la classe*************************************/


    /**********************************Variables****************************************/
    public EditText editTextNomId,editTextPrenomId;
    Button btnSuivant;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /********************************** Initiation des attributs*************************************/


        /**********************************Variables****************************************/
        setContentView(R.layout.activity_bienvenue);
        editTextNomId = findViewById(R.id.nom);
        editTextPrenomId= findViewById(R.id.prenom);
        btnSuivant = findViewById(R.id.boutonsuivantBienvenue);

//aller à l'activité suivante après avoir sauvegarder les données récupérées dans cette page
        btnSuivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               final String userNom = editTextNomId.getText().toString();
               final String userPrenom = editTextPrenomId.getText().toString();

               if (userNom.isEmpty()) {
                    editTextNomId.setError("Veuillez entrer votre nom!");
                    editTextNomId.requestFocus();
                } else if (userPrenom.isEmpty()) {
                    editTextPrenomId.setError("Veuillez entrer votre prénom!");
                    editTextPrenomId.requestFocus();
                } else if (userNom.isEmpty() && userPrenom.isEmpty()) {
                    Toast.makeText(BienvenueActivity.this, "Les champs sont vides!", Toast.LENGTH_SHORT).show();
                } else if (!(userNom.isEmpty() && userPrenom.isEmpty())) {

                    Intent intent =new Intent(BienvenueActivity.this, VousEtesActivity.class);

                    //on récupère le nom et le prénom de l'utilisateur
                    intent.putExtra("edittextnom", userNom);
                    intent.putExtra("edittextprenom", userPrenom);
                    startActivity(intent);


                }
            }
        });
    }
}



