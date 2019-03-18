package com.example.dopapillsgitFragment;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
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
import com.example.dopapillsgit.AjoutSymptomeActivity;
import com.example.dopapillsgit.CalendrierActivity;
import com.example.dopapillsgit.R;
import com.example.dopapillsgitModel.Medicament;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class AccueilFragment extends Fragment implements PopupMenu.OnMenuItemClickListener {
    //Var

    private ListView mListView;
    private View view;
    private ImageButton cal;
    private ImageButton ajouterEvenement;
    private  ArrayList<String> array;
    private ArrayAdapter<String> adapter;

    //Firebase
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRefMedicament;
    private DatabaseReference rootRef;
    private  String userID,MedicamentId;

    //Query
    private  Query query;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_accueil, container, false);
        cal = view.findViewById(R.id.bouttoncalendrier);
        ajouterEvenement =view.findViewById(R.id.ajouterevenement);


        //Firebase
        mListView = (ListView) view.findViewById(R.id.listview_voirMedicament);
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
                showPopup(getView().findFocus());
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
            Intent rdv = new Intent (getContext(), AjoutPhysiqueActivity.class);
            startActivity(rdv);
        }

        else if(item.getItemId()==R.id.repas){

            Intent repas = new Intent (getContext(), AjoutPhysiqueActivity.class);
            startActivity(repas);
        }
        else if(item.getItemId()==R.id.activitePhysique){

            Intent activitePhysique = new Intent (getContext(), AjoutPhysiqueActivity.class);
            startActivity(activitePhysique);
        }
        else if(item.getItemId()==R.id.symptome){

            Intent symptome = new Intent (getContext(), AjoutSymptomeActivity.class);
            startActivity(symptome);
        }
        else if(item.getItemId()==R.id.notePersonnelle){

            Intent notePersonnelle = new Intent (getContext(), AjoutPhysiqueActivity.class);
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

}


