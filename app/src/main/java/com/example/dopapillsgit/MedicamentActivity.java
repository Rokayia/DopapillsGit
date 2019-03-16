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
import com.example.dopapillsgitModel.ContactUrgence;
import com.example.dopapillsgitModel.DonneesSante;
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

public class MedicamentActivity  extends AppCompatActivity implements DialogPopUpAjoutMedicamentFragment.DialogListener {
    private static final String TAG = "MedicamentActivity";
  //  private static final TimeZone Locale = ;


    //Var
    String mNom,mDosage,mFréquence,mNombreFoisJour,mHoraire;
    private ListView mListView;
    private LinearLayout btnAjoutMed;
    private  ArrayList<String> array;
    private ArrayAdapter<String> adapter;
    private   Map<String, Object> childUpdates;
    private  ArrayList<Calendar> calendars=new  ArrayList<Calendar>() ;
    ArrayList<PendingIntent> intentArray;


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



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicament);



        //var
        btnAjoutMed=(LinearLayout) findViewById(R.id.ajouter_medicament);
        childUpdates = new HashMap<>();
        /*Calendar c=Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 9);
        c.set(Calendar.MINUTE, 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(fridgeDetails.this,
                0, myIntent, 0);
        setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY,pendingIntent);
       /* AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 5);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);*/

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

        if(!calendars.isEmpty()) {
            toastMessage("note empty =" +Boolean.toString(!calendars.isEmpty()));
            for (Calendar cal : calendars) {

            }
        }
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

    public void applyTexts(String nom, String dosage,String fréquence,String nombreFoisJour,String horaire, int heure,int minute) {
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

        //Ajouter un nouvel rappel médicamenteux




        Calendar cal = Calendar.getInstance();//
        // cal.set(Calendar.AM_PM,1);
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.HOUR, heure);
        cal.set(Calendar.MINUTE,minute);
        cal.set(Calendar.SECOND, 00);
toastMessage("heur =" + Integer.toString(cal.getTime().getHours()));

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        intentArray = new ArrayList<PendingIntent>();
        for(int i = 0; i < 10; ++i)
        {
            Intent notificationIntent = new Intent(this, AlarmReceiver.class);
            // Loop counter `i` is used as a `requestCode`
            PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            // Single alarms in 1, 2, ..., 10 minutes (in `i` minutes)
          //  alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, broadcast);
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, cal.getTimeInMillis(),  broadcast);
            intentArray.add(broadcast);
        }




        calendars.add(cal);

        /*AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 02);
        calendar.set(Calendar.MINUTE, 00);*/

// setRepeating() lets you specify a precise custom interval--in this case,
// 1 day
        //alarmMgr.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis()/1000,
               // AlarmManager.INTERVAL_DAY, alarmIntent);
       // am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
      //  ajouterAlarme( 1, 17, 35);



    }
    private void toastMessage(String message){
        Toast.makeText(MedicamentActivity.this,message,Toast.LENGTH_SHORT).show();
    }

    //Rappel médicamenteux
    /*public void scheduleNotification(Context context, long delay, int notificationId) {//delay is after how much time(in millis) from current time you want to schedule the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle(context.getString(R.string.bienvenue))
                .setContentText(context.getString(R.string.votre_pr_nom))
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.avatar)
                .setLargeIcon(((BitmapDrawable) context.getResources().getDrawable(R.drawable.avatar)).getBitmap())
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        Intent intent = new Intent(context, MedicamentActivity.class);
        PendingIntent activity = PendingIntent.getActivity(context, notificationId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(activity);

        Notification notification = builder.build();

        Intent notificationIntent = new Intent(context, MyNotificationPublisher.class);
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION_ID, notificationId);
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

   /* public void ajouterAlarme(int id, int hour, int minute)
    {
        Calendar cal = Calendar.getInstance();
        cal.set( hour, minute);

        Intent intent = new Intent(this, TimeAlarm.class);
        PendingIntent operation = PendingIntent.getBroadcast(this, id, intent, PendingIntent.FLAG_ONE_SHOT);
        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), operation);

    }*/
/*
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);

       // updateTimeText(c);
        toastMessage("Alarm set for :"+ DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime()));
        startAlarm(c);
    }

   /* private void updateTimeText(Calendar c) {
        String timeText = "Alarm set for: ";
        timeText += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());

        mTextView.setText(timeText);
    }


    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        alarmManager.cancel(pendingIntent);
        toastMessage("Alarm canceled");
    }*/
}


