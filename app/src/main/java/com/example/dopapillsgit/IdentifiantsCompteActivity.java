package com.example.dopapillsgit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;


import com.example.dopapillsgitModel.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class IdentifiantsCompteActivity extends AppCompatActivity {
    private static final String TAG = "ViewDatabase";
    /********************************** Attributs de la classe*************************************/


    /**********************************Variables****************************************/
    public EditText editTextNom,editTextPrenom,editTextEmail;


    /**********************************Firebase****************************************/
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private  String userID;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identifiant_compte);

        /********************************** Initialisation des attributs*************************************/


        /**********************************Variables****************************************/
        editTextNom = findViewById(R.id.editTextNomIdentifiantCompte);
        editTextPrenom= findViewById(R.id.editTextPrenomIdentifiant);
        editTextEmail= findViewById(R.id.editTextEmailIdentifiant);


        /**********************************Firebase****************************************/
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference("Patient");
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

            }
        };

//afficher le nom,prenom et mail de l'utilisateur

        Query query=myRef
                .orderByChild("id")
                .equalTo(userID);

        query.addListenerForSingleValueEvent(valueEventListener);


    }

    /**********************************Listener****************************************/
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    String nom = user.getNom();
                    String prenom = user.getPrenom();
                    String email = user.getEmail();

                    editTextNom.setText(nom);
                    editTextPrenom.setText(prenom);
                    editTextEmail.setText(email);

                }

            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    @Override
    public void onRestart() {
        super.onRestart();
        getFragmentManager().popBackStackImmediate();

    }
//se connecter de firebase auth
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
//se déconnecter de firebase auth
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

//revenir au ProfilFragment
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


