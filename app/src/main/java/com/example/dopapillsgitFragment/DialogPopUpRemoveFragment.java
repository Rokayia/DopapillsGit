package com.example.dopapillsgitFragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.dopapillsgit.R;

public class DialogPopUpRemoveFragment extends AppCompatDialogFragment {
    /********************************** Attributs de la classe*************************/


    /**********************************Variables****************************************/
    private TextView textView;
    private DialogPopUpRemoveFragment.DialogListener listener;
    private  AlertDialog.Builder builder;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_dialog_remove, null);

        builder.setView(view)
                .setTitle("Suppression")
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Confirmer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        listener.onPositiveButtonClicked();
                    }
                });


        /********************************** Initialisation des  attributs *************************/


        /**********************************Variables****************************************/

        textView = view.findViewById(R.id.textView_remove);
        String s= listener.getTxt();
        textView.setText(s);


        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (DialogPopUpRemoveFragment.DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "Veuillez implementez DialogListener");
        }
    }
    public void ChangeText(String txt){
        textView.setText(txt);
    }

    public interface DialogListener {
        String getTxt();
        void   onPositiveButtonClicked();
    }
}
