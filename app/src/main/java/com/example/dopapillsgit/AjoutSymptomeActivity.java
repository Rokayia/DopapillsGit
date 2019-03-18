package com.example.dopapillsgit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.dopapillsgitModel.ActivitePhysique;
import com.example.dopapillsgitModel.Symptome;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AjoutSymptomeActivity extends AppCompatActivity {

    private RadioGroup radioGroupMvmAnormaux,radioGroupMobilite,radioGroupTremblements;
    private Button btnAjoutSymptome;
    private Map<String, Object> childUpdates;

    String mobilite,mvmAnormaux,tremblement,dateAjout,hAjout;


    //Firebase
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRefActivite;
    private DatabaseReference rootRef;
    private String userID, SymptomeId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_symptome);
        radioGroupMobilite=findViewById(R.id.radiogroupMobilite);
        radioGroupMvmAnormaux=findViewById(R.id.radiogrupMvmAnormaux);
        radioGroupTremblements=findViewById(R.id.radiogroupTremblements);
        btnAjoutSymptome=findViewById(R.id.ajoutsymptome);


        String date_n = new SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault()).format(new Date());

        TextView tv_date = findViewById(R.id.date);
        tv_date.setText(date_n);
        dateAjout=date_n;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        String heure = format.format(calendar.getTime());
        hAjout=heure;
        TextView textView = findViewById(R.id.heure);
        textView.setText(heure);


        //Firebase

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRefActivite = FirebaseDatabase.getInstance().getReference("Symptome");
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        rootRef = FirebaseDatabase.getInstance().getReference();
        childUpdates = new HashMap<>();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

            }
        };


        //ajouter une activite physique
        //medecin
        btnAjoutSymptome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ajouterSymptome();
                startActivity(new Intent(AjoutSymptomeActivity.this, UserActivity.class));
            }
        });

    }

    public void ajouterSymptome() {



        if(radioGroupTremblements.getCheckedRadioButtonId()!=-1) {
            int idt = radioGroupTremblements.getCheckedRadioButtonId();
            View radioButtont = radioGroupTremblements.findViewById(idt);
            int radioButtonIdt = radioGroupTremblements.indexOfChild(radioButtont);
            RadioButton radioButtonChekedt = (RadioButton) radioGroupTremblements.getChildAt(radioButtonIdt);
            tremblement = radioButtonChekedt.getText().toString();
        }
        if(radioGroupMvmAnormaux.getCheckedRadioButtonId()!=-1) {
            int idmvm = radioGroupMvmAnormaux.getCheckedRadioButtonId();
            View radioButtonmvm = radioGroupMvmAnormaux.findViewById(idmvm);
            int radioButtonIdmvm = radioGroupMvmAnormaux.indexOfChild(radioButtonmvm);
            RadioButton radioButtonChekedmvm = (RadioButton) radioGroupMvmAnormaux.getChildAt(radioButtonIdmvm);
            mvmAnormaux = radioButtonChekedmvm.getText().toString();
        }
        if(radioGroupMobilite.getCheckedRadioButtonId()!=-1) {
            int idm = radioGroupMobilite.getCheckedRadioButtonId();
            View radioButtonm = radioGroupMobilite.findViewById(idm);
            int radioButtonIdm = radioGroupMobilite.indexOfChild(radioButtonm);
            RadioButton radioButtonChekedm = (RadioButton) radioGroupMobilite.getChildAt(radioButtonIdm);
            mobilite = radioButtonChekedm.getText().toString();
        }

        //ajouter dans firebase
        SymptomeId = myRefActivite.push().getKey();

        Symptome symptome = new Symptome(SymptomeId, userID, dateAjout, hAjout, mobilite, mvmAnormaux, tremblement);

        childUpdates.put("/Symptome/" + userID + "/" +SymptomeId + "/", symptome);
        rootRef.updateChildren(childUpdates);

    }
}
