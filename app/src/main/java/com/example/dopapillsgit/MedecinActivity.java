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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dopapillsgitFragment.DialogPopUpAjoutMedecinFragement;
import com.example.dopapillsgitFragment.DialogPopUpAjoutMedicamentFragment;
import com.example.dopapillsgitModel.Medecin;
import com.example.dopapillsgitModel.Medicament;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class MedecinActivity extends AppCompatActivity implements DialogPopUpAjoutMedecinFragement.DialogListener{

    //Var
    String mNom,mPrenom,mVille,mRPPS,mMail,mSpecialite;
    private ListView mListView;
    private LinearLayout btnAjoutMed;
    private ArrayList<String> array;
    private ArrayAdapter<String> adapter;
    private Map<String, Object> childUpdates;



    //Firebase
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRefMedecin;
    private DatabaseReference rootRef;
    private  String userID,MedId;




    //Query
    private Query query;



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medecin);



        //var
        btnAjoutMed=(LinearLayout) findViewById(R.id.ajouter_medecin);
        childUpdates = new HashMap<>();

        //Firebase
        mListView = (ListView) findViewById(R.id.listviewmedecin);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRefMedecin=FirebaseDatabase.getInstance().getReference("Medecin");
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
        query=myRefMedecin.child(userID);
        //  toastMessage(userID);
        query.addListenerForSingleValueEvent(valueEventListener);
        adapter = new ArrayAdapter<String>(this, R.layout.list_item_layout, array);
        mListView.setAdapter(adapter);



        //Ajout medecin
        btnAjoutMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                openDialog();
            }
        });

        //Rappel médicamenteux


    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {


            if (dataSnapshot.exists()) {
                //  toastMessage("exist");

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    //  toastMessage("child"+key);
                    Query query1= myRefMedecin.child(userID).child(key);


                    query1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Medecin medecin =  dataSnapshot.getValue(Medecin.class);
                            mNom = medecin.getNom();
                            mPrenom = medecin.getPrenom();
                            mVille=medecin.getVille();
                            mRPPS=medecin.getRPPS();
                            mMail=medecin.getMail();
                            mSpecialite=medecin.getSpecialite();


                            array.add(medecin.getNom()+" "+ medecin.getPrenom()
                                   + "         "+ medecin.getSpecialite());
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
        DialogPopUpAjoutMedecinFragement exampleDialog = new DialogPopUpAjoutMedecinFragement();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }

    public void applyTexts(String nom, String prenom,String ville,String RPPS, String mail, String specialite) {

        mNom=nom;
        mPrenom=prenom;
        mVille=ville;
        mRPPS=RPPS;
        mMail=mail;
        mSpecialite=specialite;

        //ajouter dans firebase
        MedId=myRefMedecin.push().getKey();
        Medecin medica =new Medecin(userID,MedId,mNom, mPrenom,mVille,mRPPS,mMail,mSpecialite);

        childUpdates.put("/Medecin/" + userID+ "/"+ MedId+ "/",medica);
        rootRef.updateChildren(childUpdates);

        //ajouter un item dans la listView
        array.add(nom+" "+prenom+ "         "+ specialite);
        adapter.notifyDataSetChanged();






    }
    private void toastMessage(String message){
        Toast.makeText(MedecinActivity.this,message,Toast.LENGTH_SHORT).show();
    }


}



