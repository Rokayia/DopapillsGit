package com.example.dopapillsgit;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dopapillsgitFragment.DialogPopUpAjoutMedecinFragement;
import com.example.dopapillsgitFragment.DialogPopUpAjoutMedicamentFragment;
import com.example.dopapillsgitFragment.DialogPopUpRemoveFragment;
import com.example.dopapillsgitModel.Medecin;
import com.example.dopapillsgitModel.Medicament;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MedecinActivity extends AppCompatActivity implements DialogPopUpAjoutMedecinFragement.DialogListener, DialogPopUpRemoveFragment.DialogListener {

    //Var
    String mNom, mPrenom, mVille,  mMail, mSpecialite;
    private ListView mListView;
    private LinearLayout btnAjoutMed, btnSupprimerMed;
    private ArrayList<String> array;
    private ArrayAdapter<String> adapter;
    private Map<String, Object> childUpdates;
    final Map<String, Object> childRemove = new HashMap<>();
    private ArrayList<String> keyList;
    private int positionItem;


    //Firebase
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRefMedecin;
    private DatabaseReference rootRef;
    private String userID, MedId;


    //Query
    private Query query;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medecin);


        //var
        btnAjoutMed = (LinearLayout) findViewById(R.id.ajouter_medecin);
        btnSupprimerMed = findViewById(R.id.supprimer_medecin);
        childUpdates = new HashMap<>();


        //Firebase
        mListView = (ListView) findViewById(R.id.listviewmedecin);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRefMedecin = FirebaseDatabase.getInstance().getReference("Medecin");
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        rootRef = FirebaseDatabase.getInstance().getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

            }
        };


        //afficher les informations déjà entrées par l'utilisateur
        array = new ArrayList<>();
        query = myRefMedecin.child(userID);
        //  toastMessage(userID);
        // query.addListenerForSingleValueEvent(valueEventListener);
        adapter = new ArrayAdapter<String>(this, R.layout.list_item_layout, array);
        mListView.setAdapter(adapter);
        keyList = new ArrayList<>();
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("Data onChildAdded", dataSnapshot.getValue().toString());
                Medecin medecin = dataSnapshot.getValue(Medecin.class);

                mNom = medecin.getNom();
                mPrenom = medecin.getPrenom();
                mVille = medecin.getVille();

                mMail = medecin.getMail();
                mSpecialite = medecin.getSpecialite();
                keyList.add(dataSnapshot.getKey());


                array.add(medecin.getNom() + " " + medecin.getPrenom()
                        + "         " + medecin.getSpecialite());
                adapter.notifyDataSetChanged();
                //Toast.makeText(getBaseContext(), "data=" + dataSnapshot.getValue(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d("Data onChildChanged", dataSnapshot.getValue().toString());

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("Data onChildRemoved", dataSnapshot.getValue().toString());
                //Toast.makeText(getBaseContext(), "data=" + dataSnapshot.getValue(), Toast.LENGTH_LONG).show();


                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d("Data onChildMoved", dataSnapshot.getValue().toString());
                //Toast.makeText(getBaseContext(), "data=" + dataSnapshot.getValue(), Toast.LENGTH_LONG).show();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });


        //Ajouter medecin
        btnAjoutMed.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                openDialog();


            }
        });

        //Rappel médicamenteux
        btnSupprimerMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toastMessage("Veuillez choisir le médecin que vous voulez supprimer");
                //supprimer medicament
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        positionItem = position;
                        //toastMessage( myRefMedecin.child(userID).child(keyList.get(positionItem)).getKey());
                        openDialogRemove();
                    }
                });
            }
        });


    }





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

    public void openDialog() {
        DialogPopUpAjoutMedecinFragement exampleDialog = new DialogPopUpAjoutMedecinFragement();

        exampleDialog.show(getSupportFragmentManager(), "example dialog");

    }

    public void openDialogRemove() {

        DialogPopUpRemoveFragment exampleDialog = new DialogPopUpRemoveFragment();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");


    }

    public void applyTexts(String nom, String prenom, String ville,  String mail, String specialite) {

        mNom = nom;
        mPrenom = prenom;
        mVille = ville;

        mMail = mail;
        mSpecialite = specialite;

        //ajouter dans firebase
        MedId = myRefMedecin.push().getKey();
        Medecin medica = new Medecin(userID, MedId, mNom, mPrenom, mVille,  mMail, mSpecialite);

        childUpdates.put("/Medecin/" + userID + "/" + MedId + "/", medica);
        rootRef.updateChildren(childUpdates);

        //ajouter un item dans la listView
        //  array.add(nom+" "+prenom+ "         "+ specialite);
        //adapter.notifyDataSetChanged();


    }

    public String getTxt() {
        return " Etes-vous sûr de vouloir supprimer ce médecin de votre liste?";
    }

    public void onPositiveButtonClicked() {
        //suprimer medicament

        myRefMedecin.child(userID).child(keyList.get(positionItem)).removeValue();
        keyList.remove(positionItem);
        array.remove(positionItem);
        adapter.notifyDataSetChanged();


    }


    private void toastMessage(String message) {
        Toast.makeText(MedecinActivity.this, message, Toast.LENGTH_SHORT).show();
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


