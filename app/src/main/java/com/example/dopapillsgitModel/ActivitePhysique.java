package com.example.dopapillsgitModel;

import android.os.Parcel;
import android.os.Parcelable;

public class ActivitePhysique implements Parcelable {
/*
 ** Implémenter Parceble permet de passer un object de type ActivitePhysique d'une activité à une autre
 */

    /********************************** Attributs de la classe*************************************/

    private String idActivite;
    private String idPatient;
    private String type;
    private String lieu;
    private String dateDebut;
    private String dateFin;
    private String hDebut;
    private String hFin;
    private String intensité;
    private String remarque;

    /********************************** Constructeurs**********************************************/
    public ActivitePhysique(String idPatient,String idActivite,String type,String lieu,String dateDebut,String dateFin,String hDebut,String hFin,String intensite,String remarque){
    this.setIdPatient(idPatient);
    this.setIdActivite(idActivite);
    this.setType(type);
    this.setLieu(lieu);
    this.setDateDebut(dateDebut);
    this.setDateFin(dateFin);
    this.sethDebut(hDebut);
    this.sethFin(hFin);
    this.setIntensité(intensite);
    this.setRemarque(remarque);

    }
    public ActivitePhysique() {

    }


    protected ActivitePhysique(Parcel in) {
        setIdPatient(in.readString());
        setIdActivite(in.readString());
        setType(in.readString());
        setLieu(in.readString());
        setDateDebut(in.readString());
        setDateFin(in.readString());
        sethDebut(in.readString());
        sethFin(in.readString());
        setIntensité(in.readString());
        setRemarque(in.readString());

    }
    /********************************** Méthodes **************************************************/
    public static final Creator<ActivitePhysique> CREATOR = new Creator<ActivitePhysique>() {
        @Override
        public ActivitePhysique createFromParcel(Parcel in) {
            return new ActivitePhysique(in);
        }

        @Override
        public ActivitePhysique[] newArray(int size) {
            return new ActivitePhysique[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(getIdPatient());
        parcel.writeString(getIdActivite());
        parcel.writeString(getType());
        parcel.writeString(getLieu());
        parcel.writeString(getDateDebut());
        parcel.writeString(getDateFin());
        parcel.writeString(gethDebut());
        parcel.writeString(gethFin());
        parcel.writeString(getIntensité());
        parcel.writeString(getRemarque());



    }

    /******************************************* Accesseurs ***************************************/
    public String getIdActivite() {
        return idActivite;
    }

    public void setIdActivite(String idActivite) {
        this.idActivite = idActivite;
    }

    public String getIdPatient() {
        return idPatient;
    }

    public void setIdPatient(String idPatient) {
        this.idPatient = idPatient;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
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

    public String getIntensité() {
        return intensité;
    }

    public void setIntensité(String intensité) {
        this.intensité = intensité;
    }

    public String getRemarque() {
        return remarque;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }
}

