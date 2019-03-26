package com.example.dopapillsgitFragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.dopapillsgit.R;

public class DialogPopUpAjoutMedecinFragement extends AppCompatDialogFragment {
    /********************************** Attributs de la classe*************************************/


        /**********************************Variables****************************************/
    private EditText editTextNom, editTextPrenom, editTextVille, editTextMail;
    private Spinner spinner_specialite;



        /**********************************Listener****************************************/
    private DialogPopUpAjoutMedecinFragement.DialogListener listener;

    public Dialog onCreateDialog(Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_new_medecin, null);

        builder.setView(view)
                .setTitle("Ajout d'un medecin")
                .setIcon(R.drawable.doctoraddpopup)

                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String nom = editTextNom.getText().toString();
                        String prenom = editTextPrenom.getText().toString();
                        String ville = editTextVille.getText().toString();

                        String mail = editTextMail.getText().toString();
                        String specialite = spinner_specialite.getSelectedItem().toString();
                        listener.applyTexts(nom, prenom, ville, mail, specialite);
                    }
                });


        /********************************** Initialisation des attributs *****************************/


        /**********************************Variables****************************************/
        editTextNom = view.findViewById(R.id.edit_nom_medecin);
        editTextPrenom = view.findViewById(R.id.edit_prenom_medecin);

        editTextMail = view.findViewById(R.id.edit_mail_medecin);

        editTextVille = view.findViewById(R.id.edit_ville_medecin);

        spinner_specialite = (Spinner) view.findViewById(R.id.spinner_specialiteMedecin);

        //Remplir les cases du Spinner de spécialité
        ArrayAdapter<CharSequence> adapterSpecialite = ArrayAdapter.createFromResource(getActivity(), R.array.spécialitéMedecin, android.R.layout.simple_spinner_item);
        adapterSpecialite.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_specialite.setAdapter(adapterSpecialite);


        return builder.create();
    }



    /**********************************Méthodes****************************************/
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (DialogPopUpAjoutMedecinFragement.DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "Veuillez implementez DialogListener");
        }
    }



    /**********************************Interface****************************************/
    public interface DialogListener {
        void applyTexts(String nom, String prenom, String ville, String mail, String specialite);
    }
}
