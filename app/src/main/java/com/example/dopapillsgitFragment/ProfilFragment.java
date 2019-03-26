package com.example.dopapillsgitFragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProfilFragment extends Fragment {


    View view;

    /********************************** Attributs de la classe*************************************/


    /**********************************Variables****************************************/
    private TextView editTextNomPrenomProfil;
    private LinearLayout btnId, btnDonnes, btnMedicaments, btnMed;
    private LinearLayout btnSignOut, btnRecapitulatif;
    private ImageView imageViewAvatar;
    private static final String TAG = "ProfilFragment";

    /********************************Firebase****************************************/
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef, myRefAvatar;
    private String userID;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profil, container, false);

        /********************************** Initialisation des attributs***************************/


        /**********************************Variables****************************************/
        editTextNomPrenomProfil = view.findViewById(R.id.nom_prenom_profil);
        btnSignOut = (LinearLayout) view.findViewById(R.id.sign_out_button);
        btnId = (LinearLayout) view.findViewById(R.id.button_identifiants);
        btnDonnes = (LinearLayout) view.findViewById(R.id.button_donnees_sante);
        btnMedicaments = (LinearLayout) view.findViewById(R.id.button_medicaments);
        btnMed = (LinearLayout) view.findViewById(R.id.button_medecin);
        btnRecapitulatif=view.findViewById(R.id.button_genererFicheNeurologue);
        imageViewAvatar = view.findViewById(R.id.imageView_avatarProfil);


/********************************Firebase****************************************/
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference("Patient");
        myRefAvatar = mFirebaseDatabase.getReference("Patient");
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
            }
        };
/********************************Affichage sur la page ****************************************/


        //afficher le nom,prenoom et mail de l'utiilisateur
        Query query = myRef
                .orderByChild("id")
                .equalTo(userID);

        //afficher l'avatar qui correspond au sexe et à l'âge de l'utilsateur
        query.addListenerForSingleValueEvent(valueEventListenerAvatar);

        //afficher le nom et le prenom de l'utilisateur
        query.addListenerForSingleValueEvent(valueEventListener);

/********************************Passer à une autre activité****************************************/
        // Déconnection

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
               mAuth.signOut();
               startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });

        // Génerer un récapitulatif pdf pour le neurologue du patient
        btnRecapitulatif.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {

                Intent intentConnexion = new Intent(getActivity(), PdfviewerActivity.class);
                startActivity(intentConnexion);

            }
        });


        // Voir  ses identifiants
        btnId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), IdentifiantsCompteActivity.class);
                i.putExtra("Profil", ProfilFragment.class);

                // Now start your activity
                startActivity(i);

                //startActivity(new Intent(getActivity(), IdentifiantsCompteActivity.class));
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

    /********************************Listener****************************************/

    // Afficher le nom et le prenom de la personne
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    String nom = user.getNom();
                    String prenom = user.getPrenom();
                    editTextNomPrenomProfil.setText(nom + " " + prenom);

                }

            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    //Afficher un avatar en fonction du sexe et de l'âge de la personne
    ValueEventListener valueEventListenerAvatar = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    String sexe = user.getSexe();
                    int age = Integer.parseInt(user.getAge());

                    if (sexe.equals("Une femme") && age < 55) {
                        imageViewAvatar.setBackgroundResource(R.drawable.avatarfemmme);
                    } else if (sexe.equals("Une femme") && age > 55) {
                        imageViewAvatar.setBackgroundResource(R.drawable.avatarfemmemature);
                    } else if (sexe.equals("Un homme") && (age > 55)) {
                        imageViewAvatar.setBackgroundResource(R.drawable.avatarhommemature);
                    } else if (sexe.equals("Un homme") && age < 55) {
                        imageViewAvatar.setBackgroundResource(R.drawable.avatarhomme);
                    }


                }

            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

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





}
