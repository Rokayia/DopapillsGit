package com.example.dopapillsgit;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.dopapillsgitFragment.DialogPopUpAjoutMedicamentFragment;
import com.example.dopapillsgitFragment.DialogPopUpFragment;
import com.example.dopapillsgitModel.ContactUrgence;
import com.example.dopapillsgitModel.DonneesSante;
import com.example.dopapillsgitModel.Medicament;
import com.example.dopapillsgitModel.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MedicamentActivity  extends AppCompatActivity implements DialogPopUpAjoutMedicamentFragment.DialogListener{
    private static final String TAG = "MedicamentActivity";


    //Var
    String mNom,mDosage,mFréquence,mNombreFoisJour,mHoraire;
    private ListView mListView;
    private LinearLayout btnAjoutMed;
    private  ArrayList<String> array;
    private ArrayAdapter<String> adapter;
    private   Map<String, Object> childUpdates;


    //Firebase
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRefMedicament;
    private DatabaseReference rootRef;
    private  String userID,MedicamentId;


    //Query
    private  Query query;


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicament);

        //var
        btnAjoutMed=(LinearLayout) findViewById(R.id.ajouter_medicament);
        childUpdates = new HashMap<>();


        //Firebase
        mListView = (ListView) findViewById(R.id.listview);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRefMedicament=FirebaseDatabase.getInstance().getReference("Medicament");
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
        query=myRefMedicament.child(userID);
        //  toastMessage(userID);
        query.addListenerForSingleValueEvent(valueEventListener);
        adapter = new ArrayAdapter<String>(this, R.layout.list_item_layout, array);
        mListView.setAdapter(adapter);


        //Ajout medicament
        btnAjoutMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                openDialog();
            }
        });

    }

        ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {


            if (dataSnapshot.exists()) {
                //  toastMessage("exist");

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    toastMessage("child"+key);
                    Query query1= myRefMedicament.child(userID).child(key);


                    query1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Medicament medicament = (Medicament) dataSnapshot.getValue(Medicament.class);
                            mNom = medicament.getNom();
                            mDosage = medicament.getDosage();
                            mFréquence = medicament.getFrequence();
                            mHoraire = medicament.getHoraires();
                            toastMessage("nom"+medicament.getNom());

                            array.add(medicament.getNom()+" "+ medicament.getDosage()+" mg"+ "    "+ medicament.getHoraires()+" h");
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    }

                }
            }


        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.e("error",databaseError.getMessage());
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
    public void openDialog() {
        DialogPopUpAjoutMedicamentFragment exampleDialog = new DialogPopUpAjoutMedicamentFragment();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }

    public void applyTexts(String nom, String dosage,String fréquence,String nombreFoisJour,String horaire) {
        mNom=nom;
        mDosage=dosage;
        mFréquence=fréquence;
        mNombreFoisJour=nombreFoisJour;
        mHoraire=horaire;

       //ajouter dans firebase
        MedicamentId=myRefMedicament.push().getKey();
        Medicament medica =new Medicament(userID,MedicamentId,mNom, mDosage,mFréquence,mNombreFoisJour,mHoraire);

        childUpdates.put("/Medicament/" + userID+ "/"+ MedicamentId+ "/",medica);
        rootRef.updateChildren(childUpdates);

        //ajouter un item dans la listView
        array.add(nom+" "+dosage+" mg" + "    "+ horaire+" h");
        adapter.notifyDataSetChanged();





    }
    private void toastMessage(String message){
        Toast.makeText(MedicamentActivity.this,message,Toast.LENGTH_SHORT).show();
    }

    //Rappel médicamenteux

}


