package net.mbmedia.drunkmerlin.TO;

public class Drink {
    private String id;
    private String name;
    private String prozent;
    private String menge;
    private String zeit;
    private String promille;

    public Drink(){

    }

    public Drink(String name, String prozent, String menge, String zeitpunkt){
        this.name = name;
        this.prozent = prozent;
        this.menge = menge;
        this.zeit = zeitpunkt;
    }

    public void setPromille(String z){
        this.promille = z;
    }
    public void setZeit(String z){
        this.zeit = z;
    }
    public void setId(String id){
        this.id = id;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setProzent(String prozent){
        this.prozent = prozent;
    }
    public void setMenge(String menge){this.menge = menge;}

    public String getPromille(){
        return promille;
    }
    public String getZeit(){
        return zeit;
    }
    public String getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public String getProzent(){
        return prozent;
    }
    public String getMenge(){return menge;}
}