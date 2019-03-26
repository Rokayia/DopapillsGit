package com.example.dopapillsgit;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.dopapillsgitFragment.DialogPopUpFragment;
import com.example.dopapillsgitModel.ContactUrgence;
import com.example.dopapillsgitModel.DonneesSante;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class DonnesSanteActivity extends AppCompatActivity implements DialogPopUpFragment.DialogListener{
    private static final String TAG = "DonnesSanteActivity";

    private Spinner spinner_allergies,spinner_contreIndications,spinner_groupeSanguin;
    private ContactUrgence contactUrgence;

    private Button btnModifierDonnees, btnAjoutDispositif, btnMoficationTerminee;
    private EditText editTextDateDeNaissance,editTextTaille,editTextPoids,editTextAnneeDiagnostic,editTextGroupeSanguin,editTextContactUrgence;

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private String dateDeNaissance,taille,poids,anneeDiagnostic,groupeSanguin,allergie,contreIndication;
    private String nomC,prenomC,numeroTelC, userC;


    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef,myRefC;
    private  String userID;

    private  Query query,queryC;


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donnees_de_sante);


        spinner_allergies = (Spinner) findViewById(R.id.allergie_spinner);
        spinner_contreIndications = (Spinner) findViewById(R.id.contreIndication_spinner);
        spinner_groupeSanguin= (Spinner) findViewById(R.id.groupe_sanguin_donnesSante);

        btnModifierDonnees = (Button) findViewById(R.id.modifierdonnes);
        btnAjoutDispositif = (Button) findViewById(R.id.ajouter_dispositif);
        btnMoficationTerminee = (Button) findViewById(R.id.modification_terminee);

        editTextDateDeNaissance=(EditText) findViewById(R.id.editTextdate_de_naissance_donnesSante);
        editTextTaille=(EditText) findViewById(R.id.editTexttaille_donnesSante);
        editTextPoids=(EditText) findViewById(R.id.editTextpoids_donnesSante);
        editTextAnneeDiagnostic=(EditText) findViewById(R.id.editTextdiagnostic_donnesSante);
        editTextContactUrgence=(EditText) findViewById(R.id.editTextcontact_urgence_donnesSante);

        //Remplir les cases du Spinner d'allergies
        ArrayAdapter<CharSequence> adapterAllergie = ArrayAdapter.createFromResource(this, R.array.allergies, android.R.layout.simple_spinner_item);
        adapterAllergie.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_allergies.setAdapter(adapterAllergie);
        spinner_allergies.setEnabled(false);


        //Remplir les cases du Spinner de contre indications
        ArrayAdapter<CharSequence> adapterContreIndication = ArrayAdapter.createFromResource(this, R.array.contreIndication, android.R.layout.simple_spinner_item);
        adapterContreIndication.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_contreIndications.setAdapter(adapterContreIndication);
        spinner_contreIndications.setEnabled(false);

        //Remplir les cases du Spinner de groupe sanguin
        ArrayAdapter<CharSequence> adapterGroupeSanguin = ArrayAdapter.createFromResource(this, R.array.groupeSanguin, android.R.layout.simple_spinner_item);
        adapterGroupeSanguin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_groupeSanguin.setAdapter(adapterGroupeSanguin);
        spinner_groupeSanguin.setEnabled(false);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference("DonneesDeSante");
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        //toastMessage(userID+" utilisateur");
        myRefC= FirebaseDatabase.getInstance().getReference("ContactUrgence");


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

            }
        };

        //afficher les informations déjà entrées par l'utilisateur
        query=myRef.orderByChild("idPatient")
                .equalTo(userID);
        //toastMessage(userID);
        query.addListenerForSingleValueEvent(valueEventListener);

        queryC=myRefC;
        queryC.addListenerForSingleValueEvent(valueEventListenerContact);

        //modifier les informations
        btnModifierDonnees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnAjoutDispositif.setVisibility(View.GONE);
                btnMoficationTerminee.setVisibility(View.VISIBLE);
//modification date de naissance
                editTextDateDeNaissance.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar cal = Calendar.getInstance();
                        int year = cal.get(Calendar.YEAR);
                        int month = cal.get(Calendar.MONTH);
                        int day = cal.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog dialog = new DatePickerDialog(
                                DonnesSanteActivity.this,
                                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                mDateSetListener,
                                year,month,day);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                    }
                });
                mDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                        dateDeNaissance = month + "/" + day + "/" + year;
                        editTextDateDeNaissance.setText(dateDeNaissance);
                        editTextDateDeNaissance.setTextColor(getResources().getColor(R.color.noir));
                    }
                };

//modification taille
                editTextTaille.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editTextTaille.setClickable(true);
                        editTextTaille.setCursorVisible(true);
                        editTextTaille.setFocusable(true);
                        editTextTaille.setFocusableInTouchMode(true);
                        editTextTaille.setTextColor(getResources().getColor(R.color.noir));

                        taille= editTextTaille.getText().toString();



                    }
                });
//modification poids
                editTextPoids.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editTextPoids.setClickable(true);
                        editTextPoids.setCursorVisible(true);
                        editTextPoids.setFocusable(true);
                        editTextPoids.setFocusableInTouchMode(true);
                        editTextPoids.setTextColor(getResources().getColor(R.color.noir));

                        poids = editTextPoids.getText().toString();

                    }
                });
//modification année diagnostic
                editTextAnneeDiagnostic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editTextAnneeDiagnostic.setClickable(true);
                        editTextAnneeDiagnostic.setCursorVisible(true);
                        editTextAnneeDiagnostic.setFocusable(true);
                        editTextAnneeDiagnostic.setFocusableInTouchMode(true);
                        editTextAnneeDiagnostic.setTextColor(getResources().getColor(R.color.noir));
                        anneeDiagnostic= editTextAnneeDiagnostic.getText().toString();

                    }
                });
//modification groupe sanguin

                spinner_groupeSanguin.setClickable(true);
                spinner_groupeSanguin.setFocusable(true);
                spinner_groupeSanguin.setFocusableInTouchMode(true);
                spinner_groupeSanguin.setEnabled(true);

                groupeSanguin=spinner_groupeSanguin.getSelectedItem().toString();
//modification contact d'urgence
                editTextContactUrgence.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        editTextContactUrgence.setClickable(true);
                        editTextContactUrgence.setCursorVisible(true);
                        editTextContactUrgence.setFocusable(true);
                        editTextContactUrgence.setFocusableInTouchMode(true);
                        editTextContactUrgence.setTextColor(getResources().getColor(R.color.noir));

                        openDialog();


                      //  contactUrgence= editTextContactUrgence.getText().toString();
                    }
                });
//modification allergies

                        spinner_allergies.setClickable(true);
                        spinner_allergies.setFocusable(true);
                        spinner_allergies.setFocusableInTouchMode(true);
                        spinner_allergies.setEnabled(true);

                        allergie=spinner_allergies.getSelectedItem().toString();

//modification contre-indication

                        spinner_contreIndications.setClickable(true);
                        spinner_contreIndications.setFocusable(true);
                        spinner_contreIndications.setFocusableInTouchMode(true);
                        spinner_contreIndications.setEnabled(true);
                        contreIndication=spinner_contreIndications.getSelectedItem().toString();






            }
        });




        //modification terminée

        btnMoficationTerminee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//modifier
     changeData();


                btnAjoutDispositif.setVisibility(View.VISIBLE);
                btnMoficationTerminee.setVisibility(View.GONE);
//modification date de naissance bloquée
                editTextDateDeNaissance.setClickable(false);
                editTextDateDeNaissance.setCursorVisible(false);
                editTextDateDeNaissance.setFocusable(false);
                editTextDateDeNaissance.setFocusableInTouchMode(false);
                editTextDateDeNaissance.setTextColor(getResources().getColor(R.color.grey));
                editTextDateDeNaissance.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
//modification taille bloquée
                editTextTaille.setClickable(false);
                editTextTaille.setCursorVisible(false);
                editTextTaille.setFocusable(false);
                editTextTaille.setFocusableInTouchMode(false);
                editTextTaille.setTextColor(getResources().getColor(R.color.grey));
                editTextTaille.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
//modification poids bloquée
                editTextPoids.setClickable(false);
                editTextPoids.setCursorVisible(false);
                editTextPoids.setFocusable(false);
                editTextPoids.setFocusableInTouchMode(false);
                editTextPoids.setTextColor(getResources().getColor(R.color.grey));
                editTextPoids.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
//modification année diagnostic bloquée
                editTextAnneeDiagnostic.setClickable(false);
                editTextAnneeDiagnostic.setCursorVisible(false);
                editTextAnneeDiagnostic.setFocusable(false);
                editTextAnneeDiagnostic.setFocusableInTouchMode(false);
                editTextAnneeDiagnostic.setTextColor(getResources().getColor(R.color.grey));
                editTextAnneeDiagnostic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
//modification groupe sanguin bloquée
                spinner_groupeSanguin.setClickable(false);
                spinner_groupeSanguin.setFocusable(false);
                spinner_groupeSanguin.setFocusableInTouchMode(false);
                spinner_groupeSanguin.setEnabled(false);
//modification contact d'urgence bloquée
                editTextContactUrgence.setClickable(false);
                editTextContactUrgence.setCursorVisible(false);
                editTextContactUrgence.setFocusable(false);
                editTextContactUrgence.setFocusableInTouchMode(false);
                editTextContactUrgence.setTextColor(getResources().getColor(R.color.grey));
                editTextContactUrgence.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
//modification allergies bloquée
                spinner_allergies.setClickable(false);
                spinner_allergies.setFocusable(false);
                spinner_allergies.setFocusableInTouchMode(false);
                spinner_allergies.setEnabled(false);

//modification contre-indication bloquée
                spinner_contreIndications.setClickable(false);
                spinner_contreIndications.setFocusable(false);
                spinner_contreIndications.setFocusableInTouchMode(false);
                spinner_contreIndications.setEnabled(false);

            }
        });



    }
    //modification des données dans la base de données
    private void changeData() {

            //changer les données dans la base de données
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("DonneesDeSante");
            DatabaseReference mDatabaseContact = FirebaseDatabase.getInstance().getReference("ContactUrgence");
            mDatabase.child(userID).child("dateDeNaissance").setValue(editTextDateDeNaissance.getText().toString());
            mDatabase.child(userID).child("taille").setValue(editTextTaille.getText().toString());
            mDatabase.child(userID).child("poids").setValue(editTextPoids.getText().toString());
            mDatabase.child(userID).child("anneeDiagnostic").setValue(editTextAnneeDiagnostic.getText().toString());
            mDatabase.child(userID).child("groupeSanguin").setValue(spinner_groupeSanguin.getSelectedItem().toString());
            mDatabaseContact.child(userID).child("nom").setValue(nomC);
            mDatabaseContact.child(userID).child("idContact").setValue(userC);
          //  toastMessage("bool"+" "+ Boolean.toString(mDatabaseContact.child(userID).e));
         /*   if(!mDatabaseContact.child(userID).child("idContact").equals(userC)){
                mDatabaseContact.child(userID).child("idContact").setValue(userC);
            }*/
        //mDatabaseContact.push id pour avoir un id pour contact if (contact.id=="") donc push
            mDatabaseContact.child(userID).child("prenom").setValue(prenomC);
            mDatabaseContact.child(userID).child("numeroTel").setValue(numeroTelC);
            mDatabase.child(userID).child("contactUrgence").setValue(userC);

            mDatabase.child(userID).child("allergies").setValue(spinner_allergies.getSelectedItem().toString());
            mDatabase.child(userID).child("contreIndication").setValue(spinner_contreIndications.getSelectedItem().toString());

            query.addListenerForSingleValueEvent(valueEventListenermodif);
            mDatabaseContact.addListenerForSingleValueEvent(valueEventListenerContact);

    }
    ValueEventListener valueEventListenermodif = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                   // toastMessage(snapshot.getValue(DonneesSante.class).toString());
                   DonneesSante donneesSante = (DonneesSante)snapshot.getValue(DonneesSante.class);
                    donneesSante.setDateDeNaissance(editTextDateDeNaissance.getText().toString());
                    donneesSante.setTaille(editTextTaille.getText().toString());
                    donneesSante.setPoids(editTextPoids.getText().toString());
                    donneesSante.setAnneeDiagnostic(editTextAnneeDiagnostic.getText().toString());
                    donneesSante.setGroupeSanguin(spinner_groupeSanguin.getSelectedItem().toString());
                    donneesSante.setContactUrgence(editTextContactUrgence.getText().toString());
                    donneesSante.setContreIndication(spinner_contreIndications.getSelectedItem().toString());
                    donneesSante.setAllergies(spinner_allergies.getSelectedItem().toString());


                }




            }
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            //toastMessage(Boolean.toString(dataSnapshot.exists()));
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                   DonneesSante donneesSante = snapshot.getValue(DonneesSante.class);

                    String dateDeNaissance= donneesSante.getDateDeNaissance();
                    String taille = donneesSante.getTaille();
                    String poids = donneesSante.getPoids();
                    String anneeDeDiagnostic= donneesSante.getAnneeDiagnostic();
                    String contactU=donneesSante.getContactUrgence();
                    //String groupeSanguin= snapshot.child("groupeSanguin").getValue(DonneesSante.class).getGroupeSanguin();
                   /*nom=snapshot.child("contactUrgence").child("nom").getValue(String.class);
                    prenom=snapshot.child("contactUrgence").child("prenom").getValue(String.class);
                    numeroTel=snapshot.child("contactUrgence").child("numTel").getValue(String.class);
                    contactUrgence= new ContactUrgence(nom,prenom,numeroTel);
                    String contactUrg= nom+ " "
                            +prenom
                            + '\n'+ numeroTel;*/

                    editTextDateDeNaissance.setText(dateDeNaissance);
                    editTextPoids.setText(taille);
                    editTextTaille.setText(poids);
                    editTextAnneeDiagnostic.setText(anneeDeDiagnostic);
                    editTextContactUrgence.setText(contactU);

                    spinner_allergies.setSelection(((ArrayAdapter)spinner_allergies.getAdapter()).getPosition(snapshot.child("allergies").getValue(String.class)));
                    spinner_groupeSanguin.setSelection(((ArrayAdapter)spinner_groupeSanguin.getAdapter()).getPosition(snapshot.child("groupeSanguin").getValue(String.class)));
                    spinner_contreIndications.setSelection(((ArrayAdapter)spinner_contreIndications.getAdapter()).getPosition(snapshot.child("contreIndication").getValue(String.class)));


                }

            }
        }
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    ValueEventListener valueEventListenerContact = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               // toastMessage(Boolean.toString(dataSnapshot.exists()));
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if(snapshot.getKey()==userID) {
                            ContactUrgence contact = (ContactUrgence) snapshot.getValue(ContactUrgence.class);
                            userC = contact.getIdContact();
                            nomC = contact.getNom();
                            prenomC = contact.getPrenom();
                            numeroTelC = contact.getNumeroTel();
                            toastMessage("num" + numeroTelC);
                            contactUrgence = new ContactUrgence(nomC, prenomC, numeroTelC, userC);


                            editTextContactUrgence.setText(nomC + " " + prenomC + " " + '\n' + numeroTelC);
                        }


                    }

                }
            }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };



    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    private void toastMessage(String message){
        Toast.makeText(DonnesSanteActivity.this,message,Toast.LENGTH_SHORT).show();
    }
    public void openDialog() {
        DialogPopUpFragment exampleDialog = new DialogPopUpFragment();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }

    public void applyTexts(String nom, String prenom,String numeroTel) {
       // contactUrgence= new ContactUrgence(nom,prenom,numeroTel);
        nomC=nom;
        prenomC=prenom;
        numeroTelC=numeroTel;
        editTextContactUrgence.setText(nom + " "+prenom+" "+ '\n'+ numeroTel);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle button click here
        if (item.getItemId() == android.R.id.home) {
            Intent intentforBackButton = NavUtils.getParentActivityIntent(this);
            intentforBackButton.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            NavUtils.navigateUpTo(this, intentforBackButton);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
