package com.example.dopapillsgitFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dopapillsgit.*;
import com.example.dopapillsgit.R;
import com.example.dopapillsgitModel.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ProfilFragment extends Fragment {
    View view;
    //Interface utilisateur
    public TextView editTextNomPrenomProfil;
    LinearLayout  btnId, btnDonnes, btnMedicaments, btnMed;
    LinearLayout btnSignOut;
    private static final String TAG = "ProfilFragment";

    //Firebase
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private  String userID;





    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profil, container, false);
//Interface Utilisateur
        editTextNomPrenomProfil = view.findViewById(R.id.nom_prenom_profil);
        btnSignOut = (LinearLayout) view.findViewById(R.id.sign_out_button);
        btnId = (LinearLayout) view.findViewById(R.id.button_identifiants);
        btnDonnes = (LinearLayout) view.findViewById(R.id.button_donnees_sante);
        btnMedicaments= (LinearLayout) view.findViewById(R.id.button_medicaments);
        btnMed=(LinearLayout) view.findViewById(R.id.button_medecin);

       // btnMedicaments = (LinearLayout) view.findViewById(R.id.button_medicaments);
    //    btnMedecin = (LinearLayout) view.findViewById(R.id.button_medecin);
//Firebase
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference("Patient");
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
            }
        };
//afficher le nom,prenoom et mail de l'utiilisateur
        Query query=myRef
                .orderByChild("id")
                .equalTo(userID);
        toastMessage(userID);
        query.addListenerForSingleValueEvent(valueEventListener);

// Déconnection

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });
// Voir  ses identifiants
        btnId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), IdentifiantsCompteActivity.class));
            }
        });
        //Données de santé
        btnDonnes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), DonnesSanteActivity.class));
            }
        });
        //medicament

        btnMedicaments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), MedicamentActivity.class));
            }
        });
        //medecin
        btnMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), MedecinActivity.class));
            }
        });
        return view;
    }
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
toastMessage(Boolean.toString(dataSnapshot.exists()));
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    String nom =user.getNom();
                    String prenom =user.getPrenom();
                    editTextNomPrenomProfil.setText(nom +" " +prenom);

                }

            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

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
    private void toastMessage(String message){
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }
}
