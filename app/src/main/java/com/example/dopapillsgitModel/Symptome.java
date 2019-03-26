package com.example.dopapillsgitModel;

import android.os.Parcel;
import android.os.Parcelable;

public class Symptome implements Parcelable  {
/*
 ** Implémenter Parceble permet de passer un object de type Symptome d'une activité à une autre
 */

    /********************************** Attributs de la classe*************************************/
    private String idSymptome;
    private String idPatient;
    private String dateAjout;
    private String hAjout;
    private String mobilite;
    private String mvmAnormaux;
    private String tremblement;

    /********************************** Constructeurs**********************************************/
    public Symptome(String idSymptome, String idPatient, String dateAjout, String hAjout, String mobilite, String mvmAnormaux, String tremblement) {
        this.setIdSymptome(idSymptome);
        this.setIdPatient(idPatient);
        this.setDateAjout(dateAjout);
        this.sethAjout(hAjout);
        this.setMobilite(mobilite);
        this.setMvmAnormaux(mvmAnormaux);
        this.setTremblement(tremblement);
    }

    public Symptome() {
    }

    /******************************************* Accesseurs ***************************************/
    public String getIdSymptome() {
        return idSymptome;
    }

    public void setIdSymptome(String idSymptome) {
        this.idSymptome = idSymptome;
    }

    public String getIdPatient() {
        return idPatient;
    }

    public void setIdPatient(String idPatient) {
        this.idPatient = idPatient;
    }

    public String getDateAjout() {
        return dateAjout;
    }

    public void setDateAjout(String dateAjout) {
        this.dateAjout = dateAjout;
    }

    public String gethAjout() {
        return hAjout;
    }

    public void sethAjout(String hAjout) {
        this.hAjout = hAjout;
    }

    public String getMobilite() {
        return mobilite;
    }

    public void setMobilite(String mobilite) {
        this.mobilite = mobilite;
    }

    public String getMvmAnormaux() {
        return mvmAnormaux;
    }

    public void setMvmAnormaux(String mvmAnormaux) {
        this.mvmAnormaux = mvmAnormaux;
    }

    public String getTremblement() {
        return tremblement;
    }

    public void setTremblement(String tremblement) {
        this.tremblement = tremblement;
    }

    /********************************** Méthodes **************************************************/
    protected Symptome(Parcel in) {
        setIdPatient(in.readString());
        setIdSymptome(in.readString());
        setDateAjout(in.readString());
        sethAjout(in.readString());
        setMobilite(in.readString());
        setMvmAnormaux(in.readString());
        setTremblement(in.readString());

    }
    public static final Creator<Symptome> CREATOR = new Creator<Symptome>() {
        @Override
        public Symptome createFromParcel(Parcel in) {
            return new Symptome(in);
        }

        @Override
        public Symptome[] newArray(int size) {
            return new Symptome[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(getIdPatient());
        parcel.writeString(getIdSymptome());
        parcel.writeString(getDateAjout());
        parcel.writeString(gethAjout());
        parcel.writeString(getMobilite());
        parcel.writeString(getTremblement());
        parcel.writeString(getMvmAnormaux());

    }

}
