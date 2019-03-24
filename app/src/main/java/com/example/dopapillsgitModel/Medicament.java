package com.example.dopapillsgitModel;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
public class Medicament implements Parcelable{
    private String idMedicament;
    private String idPatient;
    private String nom;
    private String dosage;
    private String frequence;
    private List<String> jour;
   // private String mois;
   // private String annee;
    private String horaires;
    public Medicament(String idPatient,String idMedicament,String nom,String dosage,String frequence,List<String> jour,String horaires){
        this.setIdMedicament(idMedicament);
        this.setNom(nom);
        this.setDosage(dosage);
        this.setFrequence(frequence);
        this.setJour(jour);
        this.setHoraires(horaires);
        this.setIdPatient(idPatient);
       // this.setAnnee(annee);
     //   this.setMois(mois);

    }
    public Medicament() {

    }
    protected Medicament(Parcel in) {
        idMedicament = in.readString();
        idPatient = in.readString();
        nom = in.readString();
        dosage = in.readString();
        frequence = in.readString();

        for (int i =0;i<jour.size();i++){
            jour.get(i).equals(in.readString()) ;
        }

        setHoraires(in.readString());
       // setMois(in.readString());
       // setAnnee(in.readString());
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

    public List<String> getJour() {
        return jour;
    }

    public void setJour(List<String> jour) {
        this.jour = jour;
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
        for (int j =0;j<jour.size();j++){
            parcel.writeString(jour.get(i));
        }
        parcel.writeString(getHoraires());
     //   parcel.writeString(getMois());
     //   parcel.writeString(getAnnee());

    }


    public String getIdPatient() {
        return idPatient;
    }

    public void setIdPatient(String idPatient) {
        this.idPatient = idPatient;
    }

    /*public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public String getMois() {
        return mois;
    }

    public void setMois(String mois) {
        this.mois = mois;
    }*/
}
