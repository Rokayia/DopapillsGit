package com.example.dopapillsgit;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dopapillsgitModel.ContactUrgence;
import com.example.dopapillsgitModel.DonneesSante;
import com.example.dopapillsgitModel.Medicament;
import com.example.dopapillsgitModel.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InscriptionActivity extends AppCompatActivity {
    public EditText emailId, passwd,passwd2,editTextPseudo;
    Button btnSignUp;
    TextView signIn;
    FirebaseAuth firebaseAuth;
    public static String NOM  = "nom";
    public static String  PRENOM = "prenom";
    public static String TAILLE  = "taille";
    public static String  POIDS = "poids";
    public static String SEXE  = "sexe";
    public static String  AGE = "age";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        firebaseAuth = FirebaseAuth.getInstance();
        editTextPseudo=findViewById(R.id.pseudo);
        emailId = findViewById(R.id.mail);
        passwd = findViewById(R.id.mdppatient);
        passwd2 = findViewById(R.id.mdppatient2);
        btnSignUp = findViewById(R.id.boutonsuivantvalider);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               final String emailID = emailId.getText().toString();
                final String pseudo = editTextPseudo.getText().toString().trim();
                String paswd = passwd.getText().toString();
                String paswd2 = passwd2.getText().toString();
                if (emailID.isEmpty()) {
                    emailId.setError("Veuillez nous informer de votre email!");
                    emailId.requestFocus();
                } else if (paswd.isEmpty()) {
                    passwd.setError("Veuillez entrer un mot de passe!");
                    passwd.requestFocus();
                } else if (emailID.isEmpty() && paswd.isEmpty()) {
                    Toast.makeText(InscriptionActivity.this, "Les champs sont vides !", Toast.LENGTH_SHORT).show();
                }else if(!paswd.equals(paswd2)){
                    passwd2.setError("Les mots de passe ne correspondent pas. Veuillez réessayer!");
                    passwd2.requestFocus();
                } else if ((!(emailID.isEmpty() && paswd.isEmpty())&&(paswd.equals(paswd2)))) {
                    firebaseAuth.createUserWithEmailAndPassword(emailID, paswd).addOnCompleteListener(InscriptionActivity.this, new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {

                            if (task.isSuccessful()) {
                                FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
                                DatabaseReference myRefu = mDatabase.getReference("Patient");
                                DatabaseReference myRefd = mDatabase.getReference("DonneesDeSante");
                                DatabaseReference myRefc = mDatabase.getReference("ContactUrgence");
                                DatabaseReference myRefmedicament = mDatabase.getReference("Medicament");
                                String userId = myRefu.push().getKey();
                                String ContactId=myRefc.push().getKey();

                                Intent i = getIntent();
                                String strnAge = "";
                                String strSexe = "";
                                String strPoids = "";
                                String strTaille = "";
                                String strNom = "";
                                String strPrenom = "";
                                String id=FirebaseAuth.getInstance().getCurrentUser().getUid();
                                if(i !=null){
                                    if (i.hasExtra(POIDS)&&i.hasExtra(TAILLE)&&i.hasExtra(AGE)&&i.hasExtra(SEXE)){
                                        strnAge = i.getStringExtra(AGE);
                                        strSexe=i.getStringExtra(SEXE);
                                        strPoids=i.getStringExtra(POIDS);
                                        strTaille=i.getStringExtra(TAILLE);
                                        strNom = i.getStringExtra(NOM);
                                        strPrenom = i.getStringExtra(PRENOM);

                                    }
                                }
                                //Création de la base de données Patient

                                User user = new User(strNom, strPrenom,strSexe,strnAge,strPoids,strTaille,pseudo,emailID,id);


                                DatabaseReference refu = myRefu.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                refu.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            Toast.makeText(InscriptionActivity.this, getString( R.string.registration_success), Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(InscriptionActivity.this, getString(R.string.registration_failed), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                                //Création de la base de données DonnéesDeSanté
                                String groupeSanguin="";
                                String contactUrgence="";
                                String allergies="";
                                String dateDeNaissance="";
                                String anneeDiagnostic="";
                                String contreIndication="";
                                DonneesSante donneesDeSante= new DonneesSante(id,groupeSanguin, contactUrgence,allergies, strTaille, strPoids, dateDeNaissance,  anneeDiagnostic,contreIndication);
                                DatabaseReference refd = myRefd.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                refd.setValue(donneesDeSante).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            Toast.makeText(InscriptionActivity.this, getString( R.string.registration_success), Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(InscriptionActivity.this, getString(R.string.registration_failed), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                                //Création de la base de données ContactUrgence
                                String nomC="";
                                String prenomC="";
                                String numeroTelC="";
                                String idC=ContactId;

                                ContactUrgence contactU= new ContactUrgence(nomC,prenomC, numeroTelC,idC);
                                DatabaseReference refc = myRefc.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                refc.setValue(contactU).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            Toast.makeText(InscriptionActivity.this, getString( R.string.registration_success), Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(InscriptionActivity.this, getString(R.string.registration_failed), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                            } else {
                                Toast.makeText(InscriptionActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }

                            if (!task.isSuccessful()) {
                                Toast.makeText(InscriptionActivity.this.getApplicationContext(),
                                        "L'inscription a échoué : " + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                startActivity(new Intent(InscriptionActivity.this, UserActivity.class));
                            }
                        }
                    });
                } else {
                    Toast.makeText(InscriptionActivity.this, "Erreur", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}