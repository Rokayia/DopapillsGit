package com.example.dopapillsgit;

import android.app.AlarmManager;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dopapillsgitModel.ActivitePhysique;
import com.example.dopapillsgitModel.DonneesSante;
import com.example.dopapillsgitModel.Medicament;
import com.example.dopapillsgitModel.Symptome;
import com.example.dopapillsgitModel.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class PdfviewerActivity extends AppCompatActivity {

    //var
    private static final String TAG = "PdfviewerActivity";
    private RelativeLayout mRlContainer;
    private int mwidth, mheight;
    private   String mMedicament,mActivite,mSymptome;
    private TextView mTextViewNomPatient,mTextViewPreomPatient,mTextViewAnneeDiagnosticPatient
            ,mTextViewContreIndication,mTextViewRecapitulatifMedicament,mTextViewActivitePhysique,
    mTextViewEtatPatient;


    //Firebase
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRefPatient,myRefDonneesDeSante,myRefMedicament,myRefActivitePhysique,myRefEtatPatient;

    private DatabaseReference rootRef;
    private  String userID;




    //Query
    private Query queryPatient,queryDonneesDeSante,queryMedicament,queryActivitePhysique,queryEtatPatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfviewer);

        //var
        mRlContainer = findViewById(R.id.mPdfViewer);
            //Initialisation string
        mMedicament = "";
        mActivite="";
        mSymptome="";
            //textView pour afficher les données du patient
        mTextViewNomPatient=findViewById(R.id.textView_NomPatient);
        mTextViewPreomPatient=findViewById(R.id.textView_PrenomPatient);
        mTextViewAnneeDiagnosticPatient=findViewById((R.id.textView_AnneeDiagnosticPatient));
        mTextViewContreIndication=findViewById(R.id.textView_ContreIndicationPatient);
        mTextViewRecapitulatifMedicament=findViewById(R.id.textView_RécapitulatifDesMedicamentsPatient);
        mTextViewActivitePhysique=findViewById(R.id.textView_RécapitulatifActivitePhysiquePatient);
        mTextViewEtatPatient=findViewById(R.id.textView_RécapitulatifEtatPatient);



        //Firebase

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        //Référence sur un node précis

            //Patient
        myRefPatient=FirebaseDatabase.getInstance().getReference("Patient");

            //DonneesDeSante
        myRefDonneesDeSante=mFirebaseDatabase.getReference("DonneesDeSante");

            //Medicament
        myRefMedicament=mFirebaseDatabase.getReference("Medicament");

            //ActivitePhysique
        myRefActivitePhysique=mFirebaseDatabase.getReference("ActivitePhysique");

           //Etat Physique
        myRefEtatPatient=mFirebaseDatabase.getReference("Symptome");



        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

            }
        };




        //Récupération des données du patient
        queryPatient=myRefPatient;
        queryPatient.addValueEventListener(valueEventListenerPatient);

        //Récupération des données de santé du patient
        queryDonneesDeSante=myRefDonneesDeSante.orderByChild("idPatient")
                .equalTo(userID);
        queryDonneesDeSante.addValueEventListener(valueEventListenerDonneesDeSante);

        //Récupération des médicaments du Patient
        queryMedicament=myRefMedicament.child(userID);
        queryMedicament.addChildEventListener(childEventListenerMedicament);

        //Récupération des activite physique du Patient
        queryActivitePhysique=myRefActivitePhysique.child(userID);
        queryActivitePhysique.addChildEventListener(childEventListenerActivite);

        //Récupération de l'état du Patient
        queryEtatPatient=myRefEtatPatient.child(userID);
        queryEtatPatient.addChildEventListener(childEventListenerEtatPatient);



    }

    //Listener sur les données personnelles du patient
    ValueEventListener valueEventListenerPatient = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
//toastMessage(Boolean.toString(dataSnapshot.exists()));
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    String nom = user.getNom();
                    String prenom = user.getPrenom();
                    mTextViewNomPatient.setText(nom);
                    mTextViewPreomPatient.setText(prenom);

                }

            }
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


    //Listener sur les données de santé du Patient
    ValueEventListener valueEventListenerDonneesDeSante = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
//toastMessage("snapshot"+Boolean.toString(dataSnapshot.exists()));
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DonneesSante donneesSante = snapshot.getValue(DonneesSante.class);

                    String anneeDiagnostic = donneesSante.getAnneeDiagnostic();
                    String contreIndication = donneesSante.getContreIndication();

                    mTextViewAnneeDiagnosticPatient.setText(anneeDiagnostic);
                    mTextViewContreIndication.setText(contreIndication);

                }
            }
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


    //Listener sur tous les medicaments  du Patient
    ChildEventListener childEventListenerMedicament = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            Medicament medicament = (Medicament) dataSnapshot.getValue(Medicament.class);

            mMedicament+=medicament.getNom()+" "+ medicament.getDosage()+" mg"+ "    "+ medicament.getHoraires()+" h"+'\n';
            // toastMessage("annee"+anneeDiagnostic);

            mTextViewRecapitulatifMedicament.setText(  mMedicament);

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
    };


//Listener sur tous les activité physique  du Patient
    ChildEventListener childEventListenerActivite = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            ActivitePhysique activitePhysique =  dataSnapshot.getValue(ActivitePhysique.class);

            mActivite +=activitePhysique.getDateDebut()+" - "+ activitePhysique.getDateFin()+" "+ ": de "+ activitePhysique.gethDebut()
                    +" h "+": de "+ activitePhysique.gethFin()+" h "+" avec une intensité "+ activitePhysique.getIntensité()+'\n'+'\n';


            mTextViewActivitePhysique.setText(  mActivite);

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
    };

    //Listener sur l'etat de santé global du Patient
    ChildEventListener childEventListenerEtatPatient = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            Symptome symptome =  dataSnapshot.getValue(Symptome.class);

            mSymptome+= symptome.getDateAjout()+ " - Mobilité : " + symptome.getMobilite()
                    + " - Mouvement Anormaux : " + symptome.getMvmAnormaux()
                    + " - Tremblement : " + symptome.getTremblement()+'\n'+'\n';


            mTextViewEtatPatient.setText(mSymptome);

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
    };




    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
       // super.onWindowFocusChanged(hasFocus);

        mwidth = mRlContainer.getWidth();
        mheight = mRlContainer.getHeight();

        Bitmap b = Bitmap.createBitmap(mwidth, mheight, Bitmap.Config.ARGB_8888);
        Canvas c1 = new Canvas(b);
        mRlContainer.draw(c1);

        PdfDocument pd = new PdfDocument();

        PdfDocument.PageInfo pi = new PdfDocument.PageInfo.Builder(mwidth, mheight, 1).create();
        PdfDocument.Page p = pd.startPage(pi);
        Canvas c = p.getCanvas();
        c.drawBitmap(b, 0, 0, new Paint());
        pd.finishPage(p);

        try {
            //demander l'autorisation de l'utilisateur pour accéder à son stockage si ca n'est pas déja fait
            isStoragePermissionGranted();


            File fileTest = new File("/sdcard/Récapitulatif pour neurologue.pdf"); //Virtually path of your pdf file after download

            File f = new File(Environment.getExternalStorageDirectory() + File.separator + "Récapitulatif pour neurologue.pdf");
            pd.writeTo(new FileOutputStream(f));

            toastMessage("Le fichier a bien été ajouté à vos fichiers");

        } catch (FileNotFoundException fnfe) {

            fnfe.printStackTrace();
        } catch (IOException ioe) {

            ioe.printStackTrace();
        }

        pd.close();
    }


    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "La permission est autorisé");
                return true;
            } else {

                Log.v(TAG, "La permission est revoquée");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
            Log.v(TAG, "La permission est autorisé");
            return true;
        }


    }

    private void toastMessage(String message){
        Toast.makeText(PdfviewerActivity.this,message,Toast.LENGTH_SHORT).show();
    }
}