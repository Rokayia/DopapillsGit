package com.example.dopapillsgitModel;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Medicament implements Parcelable{
    private String idMedicament;
    private String idPatient;
    private String nom;
    private String dosage;
    private String frequence;
    private String nombreDeFoisParJour;
    private String horaires;
    public Medicament(String idPatient,String idMedicament,String nom,String dosage,String frequence,String nombreDeFoisParJour,String horaires){
        this.setIdMedicament(idMedicament);
        this.setNom(nom);
        this.setDosage(dosage);
        this.setFrequence(frequence);
        this.setNombreDeFoisParJour(nombreDeFoisParJour);
        this.setHoraires(horaires);
        this.setIdPatient(idPatient);

    }
    public Medicament() {

    }
    protected Medicament(Parcel in) {
        idMedicament = in.readString();
        idPatient = in.readString();
        nom = in.readString();
        dosage = in.readString();
        frequence = in.readString();
        nombreDeFoisParJour = in.readString();
        horaires = in.readString();
    }
    public static final Creator<Medicament> CREATOR = new Creator<Medicament>() {
        @Override
        public Medicament createFromParcel(Parcel in) {
            return new Medicament(in);
        }

        @Override
        public Medicament[] newArray(int size) {
            return new Medicament[size];
        }
    };
    public String getIdMedicament() {
        return idMedicament;
    }

    public void setIdMedicament(String idMedicament) {
        this.idMedicament = idMedicament;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getFrequence() {
        return frequence;
    }

    public void setFrequence(String frequence) {
        this.frequence = frequence;
    }

    public String getNombreDeFoisParJour() {
        return nombreDeFoisParJour;
    }

    public void setNombreDeFoisParJour(String nombreDeFoisParJour) {
        this.nombreDeFoisParJour = nombreDeFoisParJour;
    }

    public String getHoraires() {
        return horaires;
    }

    public void setHoraires(String horaires) {
        this.horaires = horaires;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(idMedicament);
        parcel.writeString(idPatient);
        parcel.writeString(nom);
        parcel.writeString(dosage);
        parcel.writeString(frequence);
        parcel.writeString(nombreDeFoisParJour);
        parcel.writeString(horaires);


    }

    public String getIdPatient() {
        return idPatient;
    }

    public void setIdPatient(String idPatient) {
        this.idPatient = idPatient;
    }
}
