package com.example.dopapillsgitModel;

public class Repas {
    private String idRepas;
    private String idPatient;
    private String moment;
    private String date;
    private String info;

    public Repas(String idRepas, String idPatient, String moment, String date, String info) {
        this.setIdRepas(idRepas);
        this.setIdPatient(idPatient);
        this.setMoment(moment);
        this.setDate(date);
        this.setInfo(info);
    }

    public String getIdRepas() {
        return idRepas;
    }

    public void setIdRepas(String idRepas) {
        this.idRepas = idRepas;
    }

    public String getIdPatient() {
        return idPatient;
    }

    public void setIdPatient(String idPatient) {
        this.idPatient = idPatient;
    }

    public String getMoment() {
        return moment;
    }

    public void setMoment(String moment) {
        this.moment = moment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
