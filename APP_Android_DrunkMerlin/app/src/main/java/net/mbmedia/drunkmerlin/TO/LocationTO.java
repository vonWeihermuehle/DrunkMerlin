package net.mbmedia.drunkmerlin.TO;



public class LocationTO {

    private String zeitpunkt;
    private String latlng;
    private String username;

    public LocationTO(){

    }

    public LocationTO(String zeitpunkt, String latlng, String username){
        this.zeitpunkt = zeitpunkt;
        this.latlng = latlng;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
