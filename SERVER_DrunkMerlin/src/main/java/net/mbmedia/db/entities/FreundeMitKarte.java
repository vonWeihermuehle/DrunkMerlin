package net.mbmedia.db.entities;

public class FreundeMitKarte extends Freunde {
    private String username;
    private String zeitpunkt;
    private String latlng;

    public FreundeMitKarte(){

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getZeitpunkt() {
        return zeitpunkt;
    }

    public void setZeitpunkt(String zeitpunkt) {
        this.zeitpunkt = zeitpunkt;
    }

    public String getLatlng() {
        return latlng;
    }

    public void setLatlng(String latlng) {
        this.latlng = latlng;
    }
}
