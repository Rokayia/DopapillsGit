package com.example.dopapillsgit;

import com.example.dopapillsgitModel.Medicament;

public interface IMedicamentActivity {

    void createNewMedicament(String medicament, String dose,String fréquence,String nombreDefois,String horaires);

    void updateMedicament(Medicament medicament);

    void onMedicamentSelected(Medicament medicament);

    void deleteMedicament(Medicament medicament);
}
