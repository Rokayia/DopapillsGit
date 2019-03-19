package com.example.dopapillsgit;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.dopapillsgitModel.RDV;
import com.example.dopapillsgitModel.Repas;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AjoutRepasActivity extends AppCompatActivity {

    //var
    private EditText editTextTAjoutInfoRepas;
    private Button btnAjoutRepas;
    private Spinner spinner_RepasMomentJournee;
    private Map<String, Object> childUpdates;


    String moment,dateRepas,info;

    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;




    //Firebase
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRefActivite;
    private DatabaseReference rootRef;
    private String userID, RepasId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_repas);

        //var

        editTextTAjoutInfoRepas= findViewById(R.id.ajoutInfoRepas);

        btnAjoutRepas=findViewById(R.id.button_ajout_repas);
        spinner_RepasMomentJournee=findViewById(R.id.spinnerRepasMomentJournee);

        //Remplir les cases du Spinner du moment du repas
        ArrayAdapter<CharSequence> adapterMoment = ArrayAdapter.createFromResource(this, R.array.momentRepas, android.R.layout.simple_spinner_item);
        adapterMoment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_RepasMomentJournee.setAdapter(adapterMoment);

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
                        AjoutRepasActivity.this,
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
                dateRepas=date;
                mDisplayDate.setText(date);
            }
        };








        //Firebase

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRefActivite = FirebaseDatabase.getInstance().getReference("Repas");
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



        //ajouter un rdv

        btnAjoutRepas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ajouterRDV();
                startActivity(new Intent(AjoutRepasActivity.this, UserActivity.class));
            }
        });

    }

    public void ajouterRDV() {

        moment =spinner_RepasMomentJournee.getSelectedItem().toString();
        info= editTextTAjoutInfoRepas.getText().toString();



        //ajouter dans firebase
        RepasId = myRefActivite.push().getKey();

        Repas repas  = new Repas(RepasId, userID, moment, dateRepas,info);

        childUpdates.put("/Repas/" + userID + "/" +RepasId + "/", repas);
        rootRef.updateChildren(childUpdates);

    }
    private void toastMessage(String message){
        Toast.makeText(AjoutRepasActivity.this,message,Toast.LENGTH_SHORT).show();
    }
}

