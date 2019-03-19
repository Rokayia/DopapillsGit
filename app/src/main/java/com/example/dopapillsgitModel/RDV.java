package com.example.dopapillsgitModel;

import android.os.Parcel;
import android.os.Parcelable;

public class RDV implements Parcelable {
    private String idRdv;
    private String idPatient;
    private String nom;
    private String lieu;
    private String hDebut;
    private String hFin;
    private String date;
    public  RDV(){

    }
    public RDV(String idRdv, String idPatient, String nom, String lieu, String hDebut, String hFin, String date) {
        this.setIdRdv(idRdv);
        this.setIdPatient(idPatient);
        this.setNom(nom);
        this.setLieu(lieu);
        this.sethDebut(hDebut);
        this.sethFin(hFin);
        this.setDate(date);
    }


    public String getIdRdv() {
        return idRdv;
    }

    public void setIdRdv(String idRdv) {
        this.idRdv = idRdv;
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

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String gethDebut() {
        return hDebut;
    }

    public void sethDebut(String hDebut) {
        this.hDebut = hDebut;
    }

    public String gethFin() {
        return hFin;
    }

    public void sethFin(String hFin) {
        this.hFin = hFin;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    protected RDV(Parcel in) {
        setIdPatient(in.readString());
        setIdRdv(in.readString());
        setLieu(in.readString());
        setNom(in.readString());
        sethDebut(in.readString());
        sethFin(in.readString());
        setDate(in.readString());

    }
    public static final Creator<RDV> CREATOR = new Creator<RDV>() {
        @Override
        public RDV createFromParcel(Parcel in) {
            return new RDV(in);
        }

        @Override
        public RDV[] newArray(int size) {
            return new RDV[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(getIdPatient());
        parcel.writeString(getIdRdv());
        parcel.writeString(getLieu());
        parcel.writeString(getNom());
        parcel.writeString(gethDebut());
        parcel.writeString(gethFin());
        parcel.writeString(getDate());

    }
}
