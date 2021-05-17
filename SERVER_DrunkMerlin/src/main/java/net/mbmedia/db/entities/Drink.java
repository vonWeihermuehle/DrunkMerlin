package net.mbmedia.db.entities;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="drink")
public class Drink {

    private int id;
    private String nutzer_id;
    private String name;
    private String menge;
    private String prozent;
    private Date zeitpunkt;
    private String base64LatLng;

    public Drink(){}

    public Drink(String nutzer_id, String name, String menge, String prozent, Date zeitpunkt, String base64LatLng) {
        this.nutzer_id = nutzer_id;
        this.name = name;
        this.menge = menge;
        this.prozent = prozent;
        this.zeitpunkt = zeitpunkt;
        this.base64LatLng = base64LatLng;
    }

    public Drink(int id, String nutzer_id, String name, String menge, String prozent, Date zeitpunkt, String base64LatLng) {
        this.id = id;
        this.nutzer_id = nutzer_id;
        this.name = name;
        this.menge = menge;
        this.prozent = prozent;
        this.zeitpunkt = zeitpunkt;
        this.base64LatLng = base64LatLng;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name="nutzer_id")
    public String getNutzer_id() {
        return nutzer_id;
    }

    public void setNutzer_id(String nutzer_id) {
        this.nutzer_id = nutzer_id;
    }

    @Column(name="name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name="menge")
    public String getMenge() {
        return menge;
    }

    public void setMenge(String menge) {
        this.menge = menge;
    }

    @Column(name="prozent")
    public String getProzent() {
        return prozent;
    }

    public void setProzent(String prozent) {
        this.prozent = prozent;
    }

    @Column(name="zeitpunkt")
    public Date getZeitpunkt() {
        return zeitpunkt;
    }

    public void setZeitpunkt(Date zeitpunkt) {
        this.zeitpunkt = zeitpunkt;
    }

    @Column(name="latlng")
    public String getBase64LatLng() {
        return base64LatLng;
    }

    public void setBase64LatLng(String base64LatLng) {
        this.base64LatLng = base64LatLng;
    }
}
