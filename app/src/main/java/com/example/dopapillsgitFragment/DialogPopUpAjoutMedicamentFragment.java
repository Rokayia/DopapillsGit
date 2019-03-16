package com.example.dopapillsgitFragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.dopapillsgit.MedicamentActivity;
import com.example.dopapillsgit.R;

import java.util.Calendar;

public class DialogPopUpAjoutMedicamentFragment extends AppCompatDialogFragment {

    private EditText mDosage,mNombreFoisParJour;
    private TextView horaireTextView;
    private Spinner spinner_medicament,spinner_fréquence;
    private ImageButton mHoraire;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;
    private String horaire;
    int heure,min;

    private DialogPopUpAjoutMedicamentFragment.DialogListener listener;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());



        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_new_medicament, null);
//spinner
        spinner_medicament = (Spinner) view.findViewById(R.id.NomMedicament);
        spinner_fréquence = (Spinner) view.findViewById(R.id.FreqMedicament);
        mNombreFoisParJour= (EditText) view.findViewById(R.id.NombreFoisMedicament);

        //Remplir les cases du Spinner des noms de médicament
        ArrayAdapter<CharSequence> adapterMedicamentNom = ArrayAdapter.createFromResource(getActivity(), R.array.medicaments, android.R.layout.simple_spinner_item);
        adapterMedicamentNom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_medicament.setAdapter(adapterMedicamentNom);

        //Remplir les cases du Spinner des fréquence
        ArrayAdapter<CharSequence> adapterFréquence = ArrayAdapter.createFromResource(getActivity(), R.array.fréquence, android.R.layout.simple_spinner_item);
        adapterFréquence.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_fréquence.setAdapter(adapterFréquence);


        spinner_medicament = view.findViewById(R.id.NomMedicament);
        mDosage = view.findViewById(R.id.DosageMedicament);
        horaireTextView= view.findViewById(R.id.HoraireMedicamentTextView);
        mHoraire= view.findViewById(R.id.HoraireMedicamentButton);
        mHoraire.setVisibility(View.VISIBLE);

        

        builder.setView(view)
                .setTitle("Ajout d'un médicament")
                .setIcon(R.drawable.medicamentadd)
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String nom = spinner_medicament.getSelectedItem().toString();
                        String dosage = mDosage.getText().toString();
                        String frequence = spinner_fréquence.getSelectedItem().toString();
                        String nombreFoisJour = mNombreFoisParJour.getText().toString();

                        spinner_medicament.setSelection(((ArrayAdapter)spinner_medicament.getAdapter()).getPosition(nom));
                        toastMessage(nom);
                        spinner_fréquence.setSelection(((ArrayAdapter)spinner_fréquence.getAdapter()).getPosition(frequence));







                        listener.applyTexts(nom, dosage,frequence,nombreFoisJour,horaire,heure,min);
                    }
                });

        mHoraire.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view2) {
                Calendar cal2 = Calendar.getInstance();
                int hour = cal2.get(Calendar.HOUR);
                int minute = cal2.get(Calendar.MINUTE);
                heure=hour;
                min=minute;


                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, mTimeSetListener, hour, minute, true);
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.show();
            }
        });

        mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hour, int minute) {
               // Log.d("", "onTimeSet : hh:mm " + hour + ":" + minute);
                mHoraire.setVisibility(View.GONE);
                String time = hour + ":" + minute;
                horaire=time;
                horaireTextView.setText(time);


            }


        };
        return builder.create();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (DialogPopUpAjoutMedicamentFragment.DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "Veuillez implementez DialogListener");
        }
    }

    public interface DialogListener {
        void applyTexts(String nom, String dosage,String frequence,String nombreFoisJour,String horaire,int heure,int minute);
    }
    private void toastMessage(String message){
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }
}
