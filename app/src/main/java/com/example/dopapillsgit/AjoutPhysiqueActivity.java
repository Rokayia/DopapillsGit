package com.example.dopapillsgit;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.dopapillsgitModel.ActivitePhysique;
import com.example.dopapillsgitModel.Medicament;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AjoutPhysiqueActivity extends AppCompatActivity {

    /********************************** Attributs de la classe*************************************/


    /**********************************Variables****************************************/
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TextView mDisplayDate2;
    private DatePickerDialog.OnDateSetListener mDateSetListener2;
    private TextView mDisplayTime;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;
    private TextView mDisplayTime2;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener2;

    private Spinner spinner_type;
    private EditText editTextLieu, editTextRemarque;
    private RadioGroup radioButtonIntensite;
    private Button btnAjoutActivte;
    private Map<String, Object> childUpdates;

    String intensite,hDebut,hFin,dateDebut,dateFin;


    /**********************************Firebase****************************************/
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRefActivite;
    private DatabaseReference rootRef;
    private String userID, ActiviteId;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_activite_physique);

        /********************************** Initiation des attributs*************************************/


        /**********************************Variables****************************************/
        spinner_type = findViewById(R.id.spinner_typeActivite);
        editTextLieu = findViewById(R.id.editTextLieu);
        editTextRemarque = findViewById(R.id.ajoutremarque);
        radioButtonIntensite=findViewById(R.id.radiogroup);
        btnAjoutActivte=findViewById(R.id.ajoutevent);

        //Remplir les cases du Spinner type activité
        ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(this, R.array.typeActivite, android.R.layout.simple_spinner_item);
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_type.setAdapter(adapterType);


        mDisplayDate = (TextView) findViewById(R.id.tvDate);
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int number = cal.get(Calendar.DAY_OF_MONTH);
                int day = cal.get(Calendar.DAY_OF_WEEK);
                int hour = cal.get(Calendar.HOUR);
                int minute = cal.get(Calendar.MINUTE);

                DatePickerDialog dialog = new DatePickerDialog(
                        AjoutPhysiqueActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;

                String date = dayOfMonth + "/" + month + "/" + year;
                dateDebut=date;
                mDisplayDate.setText(date);
            }
        };

        mDisplayTime = (TextView) findViewById(R.id.heure);
        mDisplayTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view2) {
                Calendar cal2 = Calendar.getInstance();
                int hour = cal2.get(Calendar.HOUR);
                int minute = cal2.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(AjoutPhysiqueActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mTimeSetListener, hour, minute, true);
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.show();
            }
        });

        mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hour, int minute) {

                String time = hour + ":" + minute;
                hDebut=time;
                mDisplayTime.setText(time);
            }

        };

        mDisplayDate2 = (TextView) findViewById(R.id.tvDate2);
        mDisplayDate2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int number = cal.get(Calendar.DAY_OF_MONTH);
                int day = cal.get(Calendar.DAY_OF_WEEK);
                int hour = cal.get(Calendar.HOUR);
                int minute = cal.get(Calendar.MINUTE);

                DatePickerDialog dialog = new DatePickerDialog(
                        AjoutPhysiqueActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener2,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;

                String date = dayOfMonth + "/" + month + "/" + year;
                dateFin=date;
                mDisplayDate2.setText(date);
            }
        };

        mDisplayTime2 = (TextView) findViewById(R.id.heure2);
        mDisplayTime2 = (TextView) findViewById(R.id.heure);
        mDisplayTime2 = (TextView) findViewById(R.id.heure2);
        mDisplayTime2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view2) {
                Calendar cal2 = Calendar.getInstance();
                int hour = cal2.get(Calendar.HOUR);
                int minute = cal2.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(AjoutPhysiqueActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mTimeSetListener2, hour, minute, true);
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.show();
            }
        });

        mTimeSetListener2 = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hour, int minute) {

                String time = hour + ":" + minute;
                hFin=time;
                mDisplayTime2.setText(time);
            }

        };


              /**********************************Firebase****************************************/

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRefActivite = FirebaseDatabase.getInstance().getReference("ActivitePhysique");
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
        btnAjoutActivte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ajouterActivite();
                startActivity(new Intent(AjoutPhysiqueActivity.this, UserActivity.class));
            }
        });

    }
    /**********************************Méthode *************************************/


    /**********************************Ajout d'une activité****************************************/
    public void ajouterActivite() {

        String type = spinner_type.getSelectedItem().toString();
        String lieu = editTextLieu.getText().toString();
        String remarque = editTextRemarque.getText().toString();

        if(radioButtonIntensite.getCheckedRadioButtonId()!=-1) {
            int id = radioButtonIntensite.getCheckedRadioButtonId();
            View radioButton = radioButtonIntensite.findViewById(id);
            int radioButtonId = radioButtonIntensite.indexOfChild(radioButton);
            RadioButton radioButtonCheked = (RadioButton) radioButtonIntensite.getChildAt(radioButtonId);
            intensite = radioButtonCheked.getText().toString();
        }


       //ajouter dans firebase
        ActiviteId = myRefActivite.push().getKey();
        ActivitePhysique activitePhysique = new ActivitePhysique(userID, ActiviteId, type, lieu, dateDebut, dateFin, hDebut, hFin, intensite, remarque);

        childUpdates.put("/ActivitePhysique/" + userID + "/" +ActiviteId + "/", activitePhysique);
        rootRef.updateChildren(childUpdates);

    }
}
