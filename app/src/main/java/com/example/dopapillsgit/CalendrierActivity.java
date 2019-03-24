package com.example.dopapillsgit;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dopapillsgitModel.Medicament;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CalendrierActivity extends AppCompatActivity {

    //var
    private CalendarView mCalendar;
    private TextView mTextView;
    private String mJourSemaine;
    private String mTextLundi,mTextMardi,mTextMercredi,mTextJeudi,mTextVendredi,mTextSamedi,mTextDimanche ;


    //Firebase

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef, myRefC;
    private String userID,userIdMed;

    //query
    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendrier_activity);
//var
        mCalendar = (CalendarView) findViewById(R.id.calendrier);
        mTextView = (TextView) findViewById(R.id.textView_evenement_journee);
        mTextLundi="";
        mTextMercredi="";
        mTextMardi="";
        mTextJeudi="";
        mTextVendredi="";
        mTextSamedi="";
        mTextDimanche="";



        //Firebase
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference("Medicament");
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        //query

        query = myRef.child(userID);
        query.addChildEventListener(childEventListener);
       // Toast.makeText(getApplicationContext(), Integer.toString(mNombreDeFoisSemaine), Toast.LENGTH_LONG).show();



        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

            }
        };
        mCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                if (dayOfWeek == 1) {
                    mTextView.setText(mTextDimanche);
                } else if (dayOfWeek == 2) {
                    mTextView.setText(mTextLundi);


                } else if (dayOfWeek == 3) {
                    mTextView.setText(mTextMardi);

                } else if (dayOfWeek == 4) {
                    mTextView.setText(mTextMercredi);
                } else if (dayOfWeek == 5) {
                    mTextView.setText(mTextJeudi);
                } else if (dayOfWeek == 6) {
                    mTextView.setText(mTextVendredi);
                } else if (dayOfWeek == 7) {
                    mTextView.setText(mTextSamedi);
                }





              //  Toast.makeText(getApplicationContext(), Integer.toString(dayOfWeek), Toast.LENGTH_LONG).show();
            }
        });
    }

    ChildEventListener childEventListener= new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            //Toast.makeText(getApplicationContext(), dataSnapshot.getKey(), Toast.LENGTH_LONG).show();
            userIdMed=dataSnapshot.getKey();
final Query queryJour=myRef.child(userID).child(dataSnapshot.getKey());
            final Query query1 = myRef.child(userID).child(dataSnapshot.getKey()).child("jour");
            query1.addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                                if(dataSnapshot.getValue().equals("lundi")){
                                                    queryJour.addValueEventListener(valueEventListenerLundi);
                                                }
                                                else  if(dataSnapshot.getValue().equals("mardi")){
                                                    queryJour.addValueEventListener(valueEventListenerMardi);
                                                }
                                                else  if(dataSnapshot.getValue().equals("mercredi")){
                                                    queryJour.addValueEventListener(valueEventListenerMercredi);
                                                }
                                                else  if(dataSnapshot.getValue().equals("jeudi")){
                                                    queryJour.addValueEventListener(valueEventListenerJeudi);
                                                }
                                                else  if(dataSnapshot.getValue().equals("vendredi")){
                                                    queryJour.addValueEventListener(valueEventListenerVendredi);
                                                }
                                                else  if(dataSnapshot.getValue().equals("samedi")){
                                                    queryJour.addValueEventListener(valueEventListenerSamedi);
                                                }
                                                else  if(dataSnapshot.getValue().equals("dimanche")){
                                                    queryJour.addValueEventListener(valueEventListenerDimanche);
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

           // Toast.makeText(getApplicationContext(), medicament.getNom(), Toast.LENGTH_LONG).show();

        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            Medicament medicament = (Medicament) dataSnapshot.getValue(Medicament.class);
          //  Toast.makeText(getApplicationContext(), medicament.getNom(), Toast.LENGTH_LONG).show();


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

ValueEventListener valueEventListenerLundi = new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        Medicament medicament = (Medicament) dataSnapshot.getValue(Medicament.class);
        if(medicament.getHoraires()!=null&&medicament.getNom()!=null)
        mTextLundi+= medicament.getHoraires()+ " "+ medicament.getNom()+'\n';
       }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
};

    ValueEventListener valueEventListenerMardi = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Medicament medicament = (Medicament) dataSnapshot.getValue(Medicament.class);
            if(medicament.getHoraires()!=null&&medicament.getNom()!=null)
                mTextMardi+= medicament.getHoraires()+ " "+ medicament.getNom()+'\n';
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    ValueEventListener valueEventListenerMercredi = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Medicament medicament = (Medicament) dataSnapshot.getValue(Medicament.class);
            if(medicament.getHoraires()!=null&&medicament.getNom()!=null)
                mTextMercredi+= medicament.getHoraires()+ " "+ medicament.getNom()+'\n';
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    ValueEventListener valueEventListenerJeudi = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Medicament medicament = (Medicament) dataSnapshot.getValue(Medicament.class);
            if(medicament.getHoraires()!=null&&medicament.getNom()!=null)
                mTextJeudi+= medicament.getHoraires()+ " "+ medicament.getNom()+'\n';
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    ValueEventListener valueEventListenerVendredi= new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Medicament medicament = (Medicament) dataSnapshot.getValue(Medicament.class);
            if(medicament.getHoraires()!=null&&medicament.getNom()!=null)
                mTextVendredi+= medicament.getHoraires()+ " "+ medicament.getNom()+'\n';
        }


        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    ValueEventListener valueEventListenerSamedi = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Medicament medicament = (Medicament) dataSnapshot.getValue(Medicament.class);
            if(medicament.getHoraires()!=null&&medicament.getNom()!=null)
                mTextSamedi+= medicament.getHoraires()+ " "+ medicament.getNom()+'\n';
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    ValueEventListener valueEventListenerDimanche = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Medicament medicament = (Medicament) dataSnapshot.getValue(Medicament.class);
            if(medicament.getHoraires()!=null&&medicament.getNom()!=null)
                mTextDimanche+= medicament.getHoraires()+ " "+ medicament.getNom()+'\n';
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
}