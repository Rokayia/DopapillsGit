package com.example.dopapillsgitFragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.SENSOR_SERVICE;

public class AccueilFragment extends Fragment
        implements PopupMenu.OnMenuItemClickListener, SensorEventListener, StepListener {


    /********************************** Attributs de la classe*************************************/


    /**********************************Variables****************************************/
    private ListView mListView, mListview_voirEvenement;
    private View view;
    private ImageButton mCalendrier;
    private ImageButton mAjouterEvenement;
    private ArrayList<String> array, array2;
    private ArrayAdapter<String> adapter, adapter2;
    private SharedPreferences settings;

    /**********************************Podometre****************************************/
    private TextView mPas;
    private int numSteps;
    private StepDetector StepDetector;
    private SensorManager sensorManager;
    ;
    private Sensor accel;

    /********************************Firebase****************************************/

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference rootRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRefMedicament, myRefRDV;
    private String userID, userIdMed;

    /********************************Query****************************************/
    private Query query, query2;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_accueil, container, false);

        /********************************** Initialisation des attributs *****************************/


        /**********************************Variables****************************************/
        mCalendrier = view.findViewById(R.id.bouttoncalendrier);
        mAjouterEvenement = view.findViewById(R.id.ajouterevenement);
        mListview_voirEvenement = view.findViewById(R.id.listview_voirEvenement);
        mListView = (ListView) view.findViewById(R.id.listview_voirMedicament);

        /**********************************Podometre****************************************/

        mPas = view.findViewById(R.id.steps);
        sensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        StepDetector = new StepDetector();
        StepDetector.registerListener(this);

        /********************************Firebase****************************************/
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRefMedicament = FirebaseDatabase.getInstance().getReference("Medicament");
        myRefRDV = FirebaseDatabase.getInstance().getReference("RDV");
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        rootRef = FirebaseDatabase.getInstance().getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

            }
        };


        /********************************** Affichage d'information*****************************/

        // Afficher les événements déjà entrées par l'utilisateur

        array2 = new ArrayList<>();
        query2 = myRefRDV.child(userID);
        query2.addListenerForSingleValueEvent(valueEventListener2);
        adapter2 = new ArrayAdapter<String>(getActivity(), R.layout.list_item_accueil_medicament, array2);
        mListview_voirEvenement.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();


        //afficher les informations déjà entrées par l'utilisateur

        array = new ArrayList<>();
        query = myRefMedicament.child(userID);
        query.addChildEventListener(childEventListener);
        adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_accueil_medicament, array);
        mListView.setAdapter(adapter);

        /*************************** Affichage d'une nouvelle activité*****************************/

        //CalendarActivity

        mCalendrier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCalendrier = new Intent(getActivity(), CalendrierActivity.class);
                startActivity(intentCalendrier);
            }
        });
        /************************** Ajout d'un événement depuis un menu pop up********************/

        mAjouterEvenement.setOnClickListener(new View.OnClickListener() {
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

    /********************************** Méthode ***************************************************/


    /**************************** Affichage du menu pop up**************************/
    public void showPopup(View v) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), v);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu_main);
        popupMenu.show();
    }

    /******************** Afficher une autre activité en fonction du choix du patient**************/
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.rdv) {
            Intent rdv = new Intent(getActivity(), AjoutRdvActivity.class);
            startActivity(rdv);
        } else if (item.getItemId() == R.id.repas) {

            Intent repas = new Intent(getActivity(), AjoutRepasActivity.class);
            startActivity(repas);
        } else if (item.getItemId() == R.id.activitePhysique) {

            Intent activitePhysique = new Intent(getActivity(), AjoutPhysiqueActivity.class);
            startActivity(activitePhysique);
        } else if (item.getItemId() == R.id.symptome) {

            Intent symptome = new Intent(getActivity(), AjoutSymptomeActivity.class);
            startActivity(symptome);
        }

        return false;
    }

    /********************************** Listener ***************************************************/

    // Remplir le listView avec seulement les rendez-vous du jour
    ValueEventListener valueEventListener2 = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            if (dataSnapshot.exists()) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();

                    Query query1 = myRefRDV.child(userID).child(key);

                    query1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                            RDV rdv = dataSnapshot.getValue(RDV.class);
                            String date_p = new SimpleDateFormat("dd/M/yyyy", Locale.getDefault()).format(new Date());

                            if (date_p.equals(rdv.getDate())) {
                                if (rdv.gethDebut() != null && rdv.gethFin() != null) {

                                    array2.add(rdv.gethDebut() + " h - " + rdv.gethFin() + " h " + rdv.getNom() + " à " + rdv.getLieu());
                                    adapter2.notifyDataSetChanged();
                                } else {
                                    array2.add("Toute la journée : " + rdv.getNom() + " à " + rdv.getLieu());
                                    adapter2.notifyDataSetChanged();
                                }
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
            Log.e("error", databaseError.getMessage());
        }

    };

    // se connecter à la base de données d'identification avec mail à l'ouverture de la page
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    // se déconnecter à la base de données d'identification avec mail à la fermeture de la page
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        //reprendre la valeur de pas après que le patient est quitté l'activité et continuer à compter
        settings = getActivity().getSharedPreferences("SauvegarderPas", Context.MODE_PRIVATE);
        int firstInt = settings.getInt("pas", 0);
        numSteps = firstInt;

        mPas.setText(String.valueOf(numSteps));
        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_UI);
    }


    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

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
        mPas.setText(String.valueOf(numSteps));


    }

    /*** ChildEventListener pour récupérer tous les médicaments en fonction du jour de la semaine****/

    ChildEventListener childEventListener = new ChildEventListener() {
        @Override

        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            userIdMed = dataSnapshot.getKey();

            final Query queryJour = myRefMedicament.child(userID).child(dataSnapshot.getKey());
            final Query query1 = myRefMedicament.child(userID).child(dataSnapshot.getKey()).child("jour");
            query1.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    Calendar calendar = Calendar.getInstance();
                    int day = calendar.get(Calendar.DAY_OF_WEEK);
                    switch (day) {
                        case Calendar.MONDAY:
                            if (dataSnapshot.getValue().equals("lundi")) {
                                queryJour.addValueEventListener(valueEventListenerLundi);
                            }
                            break;
                        case Calendar.TUESDAY:
                            if (dataSnapshot.getValue().equals("mardi")) {
                                queryJour.addValueEventListener(valueEventListenerMardi);
                            }
                            break;
                        case Calendar.WEDNESDAY:
                            if (dataSnapshot.getValue().equals("mercredi")) {
                                queryJour.addValueEventListener(valueEventListenerMercredi);
                            }
                            break;
                        case Calendar.THURSDAY:
                            if (dataSnapshot.getValue().equals("jeudi")) {
                                queryJour.addValueEventListener(valueEventListenerJeudi);
                            }
                            break;
                        case Calendar.FRIDAY:
                            if (dataSnapshot.getValue().equals("vendredi")) {
                                queryJour.addValueEventListener(valueEventListenerVendredi);
                            }
                            break;
                        case Calendar.SATURDAY:
                            if (dataSnapshot.getValue().equals("samedi")) {
                                queryJour.addValueEventListener(valueEventListenerSamedi);
                            }
                            break;
                        case Calendar.SUNDAY:
                            if (dataSnapshot.getValue().equals("dimanche")) {
                                queryJour.addValueEventListener(valueEventListenerDimanche);
                            }
                            break;
                        default:


                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            Medicament medicament = (Medicament) dataSnapshot.getValue(Medicament.class);


        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    /**********************ValueEventListener en fonction des jours de la semaine******************/

                            /**********************Lundi******************/
    ValueEventListener valueEventListenerLundi = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            Medicament medicament = (Medicament) dataSnapshot.getValue(Medicament.class);
            if (medicament.getHoraires() != null && medicament.getNom() != null)
                array.add(medicament.getHoraires() + " h" + "            " + medicament.getNom() + " " + medicament.getDosage() + " mg");
            adapter.notifyDataSetChanged();

          }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
                            /**********************Mardi******************/
    ValueEventListener valueEventListenerMardi = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Medicament medicament = (Medicament) dataSnapshot.getValue(Medicament.class);
            if (medicament.getHoraires() != null && medicament.getNom() != null)
                array.add(medicament.getHoraires() + " h" + "            " + medicament.getNom() + " " + medicament.getDosage() + " mg");
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
                            /**********************Mercredi******************/
    ValueEventListener valueEventListenerMercredi = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Medicament medicament = (Medicament) dataSnapshot.getValue(Medicament.class);
            if (medicament.getHoraires() != null && medicament.getNom() != null)
                array.add(medicament.getHoraires() + " h" + "            " + medicament.getNom() + " " + medicament.getDosage() + " mg");
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

                                /**********************Jeudi******************/
    ValueEventListener valueEventListenerJeudi = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Medicament medicament = (Medicament) dataSnapshot.getValue(Medicament.class);
            if (medicament.getHoraires() != null && medicament.getNom() != null)
                array.add(medicament.getHoraires() + " h" + "            " + medicament.getNom() + " " + medicament.getDosage() + " mg");
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
                                /**********************Vendredi******************/
    ValueEventListener valueEventListenerVendredi = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Medicament medicament = (Medicament) dataSnapshot.getValue(Medicament.class);
            if (medicament.getHoraires() != null && medicament.getNom() != null)
                array.add(medicament.getHoraires() + " h" + "            " + medicament.getNom() + " " + medicament.getDosage() + " mg");
            adapter.notifyDataSetChanged();
        }


        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

                                /**********************Samedi******************/
    ValueEventListener valueEventListenerSamedi = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Medicament medicament = (Medicament) dataSnapshot.getValue(Medicament.class);
            if (medicament.getHoraires() != null && medicament.getNom() != null)
                array.add(medicament.getHoraires() + " h" + "            " + medicament.getNom() + " " + medicament.getDosage() + " mg");
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
                                /**********************Dimanche******************/
    ValueEventListener valueEventListenerDimanche = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Medicament medicament = (Medicament) dataSnapshot.getValue(Medicament.class);
            if (medicament.getHoraires() != null && medicament.getNom() != null)
                array.add(medicament.getHoraires() + " h" + "            " + medicament.getNom() + " " + medicament.getDosage() + " mg");
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    // Lorsque le patient va sur une autre page, on stocke la valeur du compteur de pas
    public void onDestroy() {
        super.onDestroy();

        settings = getActivity().getSharedPreferences("SauvegarderPas", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("pas", numSteps);
        editor.commit();
    }

}


