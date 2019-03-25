package com.example.dopapillsgitFragment;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.SensorEventListener;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dopapillsgit.AjoutPhysiqueActivity;
import com.example.dopapillsgit.AjoutRdvActivity;
import com.example.dopapillsgit.AjoutRepasActivity;
import com.example.dopapillsgit.AjoutSymptomeActivity;
import com.example.dopapillsgit.CalendrierActivity;
import com.example.dopapillsgit.R;
import com.example.dopapillsgitModel.Medecin;
import com.example.dopapillsgitModel.Medicament;
import com.example.dopapillsgitModel.RDV;
import com.example.dopapillsgitModel.StepDetector;
import com.example.dopapillsgitModel.StepListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import android.support.v7.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import static android.content.Context.SENSOR_SERVICE;

public class AccueilFragment extends Fragment implements PopupMenu.OnMenuItemClickListener, SensorEventListener, StepListener {

    //Var

    private ListView mListView,listview_voirEvenement;
    private View view;
    private ImageButton cal;
    private ImageButton ajouterEvenement;
    private  ArrayList<String> array,array2;
    private ArrayAdapter<String> adapter,adapter2;

    //podometre
    private TextView steps;
    private static final String TEXT_NUM_STEPS = "Number of Steps: ";
    private int numSteps;
    private StepDetector StepDetector;
    private SensorManager sensorManager;;
    private Sensor accel;


    //Firebase
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRefMedicament,myRefRDV;
    private DatabaseReference rootRef;
    private  String userID,MedicamentId;

    //Query
    private  Query query,query2;
    private  Sensor sSensor;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_accueil, container, false);

        //this.getActivity().getSystemService(Activity.SENSOR_SERVICE);

        steps =  view.findViewById(R.id.steps);
        // sensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        //sSensor= sensorManager .getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);*/
        sensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);

        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Toast.makeText(getActivity(),"bool accel "+Boolean.toString(accel !=null), Toast.LENGTH_SHORT).show();

        StepDetector = new StepDetector();
        StepDetector.registerListener(this);


        //var
        cal = view.findViewById(R.id.bouttoncalendrier);
        ajouterEvenement =view.findViewById(R.id.ajouterevenement);
        listview_voirEvenement=view.findViewById(R.id.listview_voirEvenement);
        mListView = (ListView) view.findViewById(R.id.listview_voirMedicament);


        //Firebase

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRefMedicament=FirebaseDatabase.getInstance().getReference("Medicament");
        myRefRDV=FirebaseDatabase.getInstance().getReference("RDV");
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        rootRef = FirebaseDatabase.getInstance().getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

            }
        };

        //afficher les evenements déjà entrées par l'utilisateur
        array2 = new ArrayList<>();
        query2=myRefRDV.child(userID);
        //  toastMessage(userID);
        query2.addListenerForSingleValueEvent(valueEventListener2);
        adapter2 = new ArrayAdapter<String>(getActivity(), R.layout.list_item_accueil_medicament, array2);
        listview_voirEvenement.setAdapter(adapter2);


        //afficher les informations déjà entrées par l'utilisateur
        array = new ArrayList<>();
        query=myRefMedicament.child(userID);
        //  toastMessage(userID);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("Data onChildAdded", dataSnapshot.getValue().toString());

                Medicament medicament = (Medicament) dataSnapshot.getValue(Medicament.class);

                array.add( medicament.getHoraires()+" h"+"            "+medicament.getNom()+" "+ medicament.getDosage()+" mg");
                adapter.notifyDataSetChanged();


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

        adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_accueil_medicament, array);
        mListView.setAdapter(adapter);


        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCalendrier = new Intent(getActivity(), CalendrierActivity.class);
                startActivity(intentCalendrier);
            }
        });
        ajouterEvenement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(getActivity().findViewById(R.id.bottom_navigation));
            }
        });
        String date_n = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault()).format(new Date());

        TextView tv_date = view.findViewById(R.id.date);
        tv_date.setText(date_n);
        return view;
    }

    public void showPopup(View v) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), v);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu_main);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if(item.getItemId()==R.id.rdv)
        {
            Intent rdv = new Intent (getActivity(), AjoutRdvActivity.class);
            startActivity(rdv);
        }

        else if(item.getItemId()==R.id.repas){

            Intent repas = new Intent (getActivity(), AjoutRepasActivity.class);
            startActivity(repas);
        }
        else if(item.getItemId()==R.id.activitePhysique){

            Intent activitePhysique = new Intent (getActivity(), AjoutPhysiqueActivity.class);
            startActivity(activitePhysique);
        }
        else if(item.getItemId()==R.id.symptome){

            Intent symptome = new Intent (getActivity(), AjoutSymptomeActivity.class);
            startActivity(symptome);
        }
        else if(item.getItemId()==R.id.notePersonnelle){

            Intent notePersonnelle = new Intent (getActivity(), AjoutPhysiqueActivity.class);
            startActivity(notePersonnelle);
        }
        return false;
    }


    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {


            if (dataSnapshot.exists()) {
                //  toastMessage("exist");

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    //  toastMessage("child"+key);
                    Query query1= myRefMedicament.child(userID).child(key);


                    query1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Medicament medicament = (Medicament) dataSnapshot.getValue(Medicament.class);

                            array.add( medicament.getHoraires()+" h"+"            "+medicament.getNom()+" "+ medicament.getDosage()+" mg");
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






    ValueEventListener valueEventListener2 = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {


            if (dataSnapshot.exists()) {
                //  toastMessage("exist");

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    //  toastMessage("child"+key);
                    Query query1= myRefRDV.child(userID).child(key);
//voir si on peut montrer que les rdv du jour + changer les iterfaces


                    query1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            RDV rdv = (RDV) dataSnapshot.getValue(RDV.class);
                            if(rdv.gethDebut()!=null &&rdv.gethFin()!=null ){

                                array2.add( rdv.gethDebut()+"h - "+ rdv.gethFin()+"h "+rdv.getNom() + " à "+ rdv.getLieu());
                                adapter2.notifyDataSetChanged();
                            }else{
                                array2.add( rdv.getDate()+" -  " +rdv.getNom() + " à "+ rdv.getLieu());
                                adapter2.notifyDataSetChanged();
                            }

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
    @Override
    public void onResume (){
        super.onResume();
        numSteps = 0;
        steps.setText(TEXT_NUM_STEPS + String.valueOf(numSteps));
        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_UI);
       /*super.onResume();
        running = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        Sensor stepDetector = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        Toast.makeText(getActivity(),"bool"+Boolean.toString(countSensor !=null), Toast.LENGTH_SHORT).show();
        if(countSensor !=null){
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        }
        else {
            Toast.makeText(getActivity(),"Aucun capteur trouvé !", Toast.LENGTH_SHORT).show();
        }*/
    }


    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        // running = false;
        // si on désactive l'enregistrement, le matériel cessera de détecter l'étape.
        //sensorManager/unregisterListerner(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
       /* if (running) {
            steps.setText(String.valueOf(event.values[0]));
        }*/
        // steps.setText(String.valueOf(event.values[0]));
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            StepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    public void step(long timeNs) {
        numSteps++;
        steps.setText(TEXT_NUM_STEPS + String.valueOf(numSteps));



    }
}


