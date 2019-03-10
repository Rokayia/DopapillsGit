package com.example.dopapillsgit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dopapillsgitModel.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class IdentifiantsCompteActivity extends AppCompatActivity {
    private static final String TAG = "ViewDatabase";
    public EditText editTextNom,editTextPrenom,editTextEmail;


    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private  String userID;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identifiant_compte);
        editTextNom = findViewById(R.id.editTextNomIdentifiantCompte);
        editTextPrenom= findViewById(R.id.editTextPrenomIdentifiant);
        editTextEmail= findViewById(R.id.editTextEmailIdentifiant);


        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

            }
        };
//afficher le nom,prenom et mail de l'utilisateur
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void showData(DataSnapshot dataSnapshot) {
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            User uInfo = new User();
            uInfo.setNom(ds.child(userID).getValue(User.class).getNom());
            uInfo.setEmail(ds.child(userID).getValue(User.class).getEmail());
            uInfo.setPrenom(ds.child(userID).getValue(User.class).getPrenom());

            //visualiser les données sur Logcat
            Log.d(TAG, "showData: nom: " + uInfo.getNom());
            Log.d(TAG, "showData: prenom: " + uInfo.getPrenom());
            Log.d(TAG, "showData: email: " + uInfo.getEmail());

            String nom = uInfo.getNom();
            String prenom = uInfo.getPrenom();
            String email = uInfo.getEmail();

            editTextNom.setText(nom);
            editTextPrenom.setText(prenom);
            editTextEmail.setText(email);

        }
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
    }


