package tn.iac.mobiledevelopment.covoiturageapp.models;

import java.util.Date;

/**
 * Created by Chaker on 03/02/2016.
 */
public class Post {

    private int id_Post;
    private String identifiant;
    private String message;
    private int type;
    private Date date;
    private int nb_Places;
    private String source;
    private String destination;
    private int prix;
    private int depart;
    private String telephone;
    private int id_Groups;
    private int id_User;

    public int getId_Post() {
        return id_Post;
    }

    public void setId_Post(int id_Post) {
        this.id_Post = id_Post;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getNb_Places() {
        return nb_Places;
    }

    public void setNb_Places(int nb_Places) {
        this.nb_Places = nb_Places;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public int getDepart() {
        return depart;
    }

    public void setDepart(int depart) {
        this.depart = depart;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public int getId_Groups() {
        return id_Groups;
    }

    public void setId_Groups(int id_Groups) {
        this.id_Groups = id_Groups;
    }

    public int getId_User() {
        return id_User;
    }

    public void setId_User(int id_User) {
        this.id_User = id_User;
    }
}
