package com.example.dopapillsgitModel;

public class DonneesSante {
    private String idPatient;
    private String groupeSanguin;
    private String contactUrgence;
    private String allergies;
    private String taille;
    private String poids;
    private String dateDeNaissance;
    private String anneeDiagnostic;
    private String contreIndication;
    public DonneesSante(){

    }

    public DonneesSante(String idPatient, String groupeSanguin, String contactUrgence, String allergies, String taille, String poids, String dateDeNaissance, String anneeDiagnostic,String contreIndication){
        this.setIdPatient(idPatient);
        this.setGroupeSanguin(groupeSanguin);
        this.setContactUrgence(contactUrgence);
        this.setAllergies(allergies);
        this.setTaille(taille);
        this.setPoids(poids);
        this.setAnneeDiagnostic(anneeDiagnostic);
        this.setDateDeNaissance(dateDeNaissance);
        this.setContreIndication(contreIndication);
    }


    public String getIdPatient() {
        return idPatient;
    }

    public void setIdPatient(String idPatient) {
        this.idPatient = idPatient;
    }

    public String getGroupeSanguin() {
        return groupeSanguin;
    }

    public void setGroupeSanguin(String groupeSanguin) {
        this.groupeSanguin = groupeSanguin;
    }

    public String getContactUrgence() {
        return contactUrgence;
    }

    public void setContactUrgence(String contactUrgence) {
        this.contactUrgence = contactUrgence;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getTaille() {
        return taille;
    }

    public void setTaille(String taille) {
        this.taille = taille;
    }

    public String getPoids() {
        return poids;
    }

    public void setPoids(String poids) {
        this.poids = poids;
    }

    public String getAnneeDiagnostic() {
        return anneeDiagnostic;
    }

    public void setAnneeDiagnostic(String anneeDiagnostic) {
        this.anneeDiagnostic = anneeDiagnostic;
    }

    public String getDateDeNaissance() {
        return dateDeNaissance;
    }

    public void setDateDeNaissance(String dateDeNaissance) {
        this.dateDeNaissance = dateDeNaissance;
    }

    public String getContreIndication() {
        return contreIndication;
    }

    public void setContreIndication(String contreIndication) {
        this.contreIndication = contreIndication;
    }
}
