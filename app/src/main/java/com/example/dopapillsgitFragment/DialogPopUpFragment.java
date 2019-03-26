package com.example.dopapillsgitFragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.dopapillsgit.R;

public class DialogPopUpFragment extends AppCompatDialogFragment  {

    /********************************** Attributs de la classe*************************************/


    /**********************************Variables****************************************/
    private EditText editTextNom;
    private EditText editTextPrenom;
    private EditText editTextNumTel;
    private DialogListener listener;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_dialog, null);

        builder.setView(view)
                .setTitle("Ajout d'un contact d'urgence")
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
                        String numTel = editTextNumTel.getText().toString();

                        listener.applyTexts(nom, prenom,numTel);
                    }
                });
        /********************************** Initialisation des  attributs *************************/


        /**********************************Variables****************************************/
        editTextNom = view.findViewById(R.id.edit_nom_contact);
        editTextPrenom = view.findViewById(R.id.edit_prenom_contact);
        editTextNumTel= view.findViewById(R.id.edit_numTel_contact);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "Veuillez implementez DialogListener");
        }
    }



    /**********************************Interface****************************************/
    public interface DialogListener {
        void applyTexts(String nom, String prenom,String numTel);
    }
}
