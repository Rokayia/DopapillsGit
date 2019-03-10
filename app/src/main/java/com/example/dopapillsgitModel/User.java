package com.example.dopapillsgitModel;

public class User {
    private String nom;
    private String prenom;
    private String sexe;
    private String age;
    private String poids;
    private String taille;
    private String pseudo;
    private String email;

    public User(){

    }
    public User(String nom, String prenom) {
        this.setNom(nom);
        this.setPrenom(prenom);

    }
    public User(String nom, String prenom, String sexe, String age, String poids, String taille) {
        this.setNom(nom);
        this.setPrenom(prenom);
        this.setSexe(sexe);
        this.setAge(age);
        this.setPoids(poids);
        this.setTaille(taille);

    }


    public User(String nom, String prenom, String sexe, String age, String poids, String taille, String pseudo, String email) {
        this.setNom(nom);
        this.setPrenom(prenom);
        this.setSexe(sexe);
        this.setAge(age);
        this.setPoids(poids);
        this.setTaille(taille);
        this.setPseudo(pseudo);
        this.setEmail(email);

    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getSexe() {
        return sexe;
    }

    public String getAge() {
        return age;
    }

    public String getPoids() {
        return poids;
    }

    public String getTaille() {
        return taille;
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getEmail() {
        return email;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setPoids(String poids) {
        this.poids = poids;
    }

    public void setTaille(String taille) {
        this.taille = taille;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
