package com.example.dopapillsgitModel;

public class ContactUrgence {


    /********************************** Attributs de la classe*************************************/

    private String nom;
    private String prenom;
    private String numeroTel;
    private String idContact;

    /********************************** Constructeurs**********************************************/
    public ContactUrgence(){

    }
    public ContactUrgence(String nom, String prenom,String numeroTel) {
     this.setNom(nom);
     this.setPrenom(prenom);
     this.setNumeroTel(numeroTel);
    }
    public ContactUrgence(String nom, String prenom,String numeroTel,String idContact) {
        this.setNom(nom);
        this.setPrenom(prenom);
        this.setNumeroTel(numeroTel);
        this.setIdContact(idContact);
    }

    /******************************************* Accesseurs ***************************************/
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

    public String getNumeroTel() {
        return numeroTel;
    }

    public void setNumeroTel(String numeroTel) {
        this.numeroTel = numeroTel;
    }

    public String getIdContact() {
        return idContact;
    }

    public void setIdContact(String idContact) {
        this.idContact = idContact;
    }
}
