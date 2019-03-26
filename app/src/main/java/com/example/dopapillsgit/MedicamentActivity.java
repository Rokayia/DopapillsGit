package com.example.dopapillsgit;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.dopapillsgitFragment.DialogPopUpAjoutMedicamentFragment;
import com.example.dopapillsgitFragment.DialogPopUpFragment;
import com.example.dopapillsgitFragment.DialogPopUpRemoveFragment;
import com.example.dopapillsgitModel.ContactUrgence;
import com.example.dopapillsgitModel.DonneesSante;
import com.example.dopapillsgitModel.Medecin;
import com.example.dopapillsgitModel.Medicament;
import com.example.dopapillsgitModel.TimeAlarm;
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
import android.content.Context;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class MedicamentActivity  extends AppCompatActivity implements DialogPopUpAjoutMedicamentFragment.DialogListener, DialogPopUpRemoveFragment.DialogListener {
    private static final String TAG = "MedicamentActivity";
  //  private static final TimeZone Locale = ;


    //Var
    String mNom,mDosage,mFréquence,mHoraire;
    List<String> mJour;
    private  LinearLayout btnSupprimerMed;
    private ListView mListView;
    private LinearLayout btnAjoutMed;
    private  ArrayList<String> array;
    private ArrayAdapter<String> adapter;
    private   Map<String, Object> childUpdates;
    private  ArrayList<Calendar> calendars=new  ArrayList<Calendar>() ;
    ArrayList<PendingIntent> intentArray;
    private ArrayList<String> keyList;
    private int positionItem;


    //Firebase
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRefMedicament;
    private DatabaseReference rootRef;
    private  String userID,MedicamentId;
    private AlarmManager am;



    //Query
    private  Query query;

    private Context context;



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicament);
        context=this;



        //var
        btnAjoutMed=(LinearLayout) findViewById(R.id.ajouter_medicament);
        btnSupprimerMed = (LinearLayout) findViewById(R.id.linear_supprimer_medicament);
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

        //query.addListenerForSingleValueEvent(valueEventListener);

        adapter = new ArrayAdapter<String>(this, R.layout.list_item_layout,R.id.textView_listView, array);
        mListView.setAdapter(adapter);
        keyList = new ArrayList<>();
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Medicament medicament = (Medicament) dataSnapshot.getValue(Medicament.class);
                mNom = medicament.getNom();
                mDosage = medicament.getDosage();
                mFréquence = medicament.getFrequence();
                mHoraire = medicament.getHoraires();
                keyList.add(dataSnapshot.getKey());
                //   toastMessage("nom"+medicament.getNom());

                array.add(medicament.getNom()+" "+ medicament.getDosage()+" mg"+ "    "+ medicament.getHoraires()+" h");
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {


            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {



                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });



       // Context context=null;
      //  scheduleNotification(context.getApplicationContext(),30,1);

        //Ajout medicament
        btnAjoutMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                openDialog();
            }
        });

        //Rappel médicamenteux

       /* if(!calendars.isEmpty()) {
            toastMessage("note empty =" +Boolean.toString(!calendars.isEmpty()));
            for (Calendar cal : calendars) {

            }
        }*/


        //Supprimer un médicament
        btnSupprimerMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toastMessage("Veuillez choisir le médecin que vous voulez supprimer");

                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        positionItem = position;
                        //toastMessage( myRefMedecin.child(userID).child(keyList.get(positionItem)).getKey());
                        openDialogRemove();

                    }
                });
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
                  //  toastMessage("child"+key);
                    Query query1= myRefMedicament.child(userID).child(key);


                    query1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Medicament medicament = (Medicament) dataSnapshot.getValue(Medicament.class);
                            mNom = medicament.getNom();
                            mDosage = medicament.getDosage();
                            mFréquence = medicament.getFrequence();
                            mHoraire = medicament.getHoraires();
                            keyList.add(dataSnapshot.getKey());
                         //   toastMessage("nom"+medicament.getNom());

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

    public void openDialogRemove() {

        DialogPopUpRemoveFragment exampleDialog = new DialogPopUpRemoveFragment();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");

   }
    public String getTxt() {
      return " Etes-vous sûr de vouloir supprimer ce médicament de votre liste?";
}

    public void onPositiveButtonClicked() {
        //suprimer medicament

        myRefMedicament.child(userID).child(keyList.get(positionItem)).removeValue();
        keyList.remove(positionItem);
        array.remove(positionItem);
        adapter.notifyDataSetChanged();


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void applyTexts(String nom, String dosage, String fréquence, List<String> jour, String horaire, int heure, int minute) {
        mNom=nom;
        mDosage=dosage;
        mFréquence=fréquence;
        mJour=jour;
        mHoraire=horaire;
        toastMessage(horaire);
        toastMessage("heur 3 =" + heure+ " "+ minute);
       //ajouter dans firebase
        MedicamentId=myRefMedicament.push().getKey();
        Medicament medica =new Medicament(userID,MedicamentId,mNom, mDosage,mFréquence,mJour,mHoraire);

        childUpdates.put("/Medicament/" + userID+ "/"+ MedicamentId+ "/",medica);
        rootRef.updateChildren(childUpdates);

        //ajouter un item dans la listView
       // array.add(nom+" "+dosage+" mg" + "    "+ horaire+" h");
        //adapter.notifyDataSetChanged();

        //Ajouter un nouvel rappel médicamenteux




        Calendar cal = Calendar.getInstance();//
        // cal.set(Calendar.AM_PM,1);
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.HOUR, heure);
        cal.set(Calendar.MINUTE,minute);
        cal.set(Calendar.SECOND, 00);
toastMessage("heur =" + cal.getTime().toString());
        toastMessage("heur 2 =" + heure+ " "+ minute);
        final int _id = (int) System.currentTimeMillis();

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        intentArray = new ArrayList<PendingIntent>();
        for(int i = 0; i < 10; ++i)
        {
            Intent notificationIntent = new Intent(this, AlarmReceiver.class);

            PendingIntent broadcast = PendingIntent.getBroadcast(this, _id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),  broadcast);
            //alarmManager.setExact(AlarmManager.ELAPSED_REALTIME,
                    //cal.getTimeInMillis(), PendingIntent.getBroadcast(this, 1,
                           // notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT));
            intentArray.add(broadcast);
        }




        calendars.add(cal);





    }
    private void toastMessage(String message){
        Toast.makeText(MedicamentActivity.this,message,Toast.LENGTH_SHORT).show();
    }


}


