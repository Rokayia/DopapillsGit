package com.example.dopapillsgitModel;

import android.os.Parcel;
import android.os.Parcelable;

public class Medecin implements Parcelable {
    private String idMed;
    private String idPatient;
    private String nom;
    private String prenom;
    private String ville;
    private  String mail;
    private String RPPS;
    private String specialite;


    public Medecin(String idPatient, String idMed, String nom, String prenom, String ville,String mail, String RPPS, String specialite) {
     this.setIdMed(idMed);
     this.setIdPatient(idPatient);
     this.setNom(nom);
     this.setPrenom(prenom);
     this.setVille(ville);
     this.setMail(mail);
     this.setRPPS(RPPS);
     this.setSpecialite(specialite);



    }

    public Medecin() {

    }

    protected Medecin(Parcel in) {
        setIdMed(in.readString());
        setIdPatient(in.readString());
        setNom(in.readString());
        setPrenom(in.readString());
        setVille(in.readString());
        setMail(in.readString());
        setRPPS(in.readString());
        setSpecialite(in.readString());
    }

    public static final Parcelable.Creator<Medecin> CREATOR = new Parcelable.Creator<Medecin>() {
        @Override
        public Medecin createFromParcel(Parcel in) {
            return new Medecin(in);
        }

        @Override
        public Medecin[] newArray(int size) {
            return new Medecin[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(getIdMed());
        parcel.writeString(getIdPatient());
        parcel.writeString(getNom());
        parcel.writeString(getPrenom());
        parcel.writeString(getVille());
        parcel.writeString(getMail());
        parcel.writeString(getRPPS());
        parcel.writeString(getSpecialite());

    }

    public String getIdMed() {
        return idMed;
    }

    public void setIdMed(String idMed) {
        this.idMed = idMed;
    }

    public String getIdPatient() {
        return idPatient;
    }

    public void setIdPatient(String idPatient) {
        this.idPatient = idPatient;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getRPPS() {
        return RPPS;
    }

    public void setRPPS(String RPPS) {
        this.RPPS = RPPS;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }
}
