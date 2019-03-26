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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.dopapillsgitModel.RDV;
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

public class AjoutRdvActivity extends AppCompatActivity {

    /********************************** Attributs de la classe*************************************/


    /**********************************Variables****************************************/
    private EditText editTextTypeDActivite,editTextLieu;
    private Button btnAjoutRDV;
    private Switch switch_journeeEntiere;
    private Map<String, Object> childUpdates;
    private ImageView imageViewDate;

    String nom,lieu,hDebut,hFin,dateRDV;

    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;


    private TextView mDisplayTime;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;

    private TextView mDisplayTime2;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener2;



    /**********************************Firebase****************************************/
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRefActivite;
    private DatabaseReference rootRef;
    private String userID, RDVId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_rdv);

        /********************************** Initiation des attributs*************************************/


        /**********************************Variables****************************************/
        editTextTypeDActivite= findViewById(R.id.editTextTypeDActivite);
        editTextLieu=findViewById(R.id.editTextLieu);
        btnAjoutRDV=findViewById(R.id.ajoutrdv);
        switch_journeeEntiere=findViewById(R.id.switch_journeeEntiere);
        imageViewDate= findViewById(R.id.imageViewDate);

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
                        AjoutRdvActivity.this,
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
                dateRDV=date;
                mDisplayDate.setText(date);
            }
        };

        mDisplayTime = (TextView) findViewById(R.id.heure);
        mDisplayTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view2) {
                Calendar cal2 = Calendar.getInstance();
                int hour = cal2.get(Calendar.HOUR);
                int minute = cal2.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(AjoutRdvActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mTimeSetListener, hour, minute, true);
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



        mDisplayTime2 = (TextView) findViewById(R.id.heure2);
        mDisplayTime2 = (TextView) findViewById(R.id.heure);
        mDisplayTime2 = (TextView) findViewById(R.id.heure2);
        mDisplayTime2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view2) {
                Calendar cal2 = Calendar.getInstance();
                int hour = cal2.get(Calendar.HOUR);
                int minute = cal2.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(AjoutRdvActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mTimeSetListener2, hour, minute, true);
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
        myRefActivite = FirebaseDatabase.getInstance().getReference("RDV");
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

        //gestion du switch
        switch_journeeEntiere.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    mDisplayTime2.setVisibility(View.GONE);
                    mDisplayTime.setVisibility(View.GONE);
                    imageViewDate.setVisibility(View.GONE);
                    mDisplayDate.setGravity(View.TEXT_ALIGNMENT_GRAVITY);
                }else{
                    mDisplayTime2.setVisibility(View.VISIBLE);
                    mDisplayTime.setVisibility(View.VISIBLE);
                    imageViewDate.setVisibility(View.VISIBLE);
                }


            }
        });


        //ajouter un rdv
        btnAjoutRDV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ajouterRDV();
                startActivity(new Intent(AjoutRdvActivity.this, UserActivity.class));
            }
        });

    }
    /********************************** Méthode*************************************/


    /**********************************Ajout d'un RDV****************************************/

    public void ajouterRDV() {

        nom =editTextTypeDActivite.getText().toString();
        lieu= editTextLieu.getText().toString();



        //ajouter dans firebase
        RDVId = myRefActivite.push().getKey();

        RDV rdv = new RDV(RDVId, userID, nom, lieu, hDebut, hFin, dateRDV);

        childUpdates.put("/RDV/" + userID + "/" +RDVId + "/", rdv);
        rootRef.updateChildren(childUpdates);

    }
    private void toastMessage(String message){
        Toast.makeText(AjoutRdvActivity.this,message,Toast.LENGTH_SHORT).show();
    }
}

