package net.mbmedia.drunkmerlin.TO;

public class Freund {

    private String id;
    private String name;



    private String promille;

    public Freund(String id, String name, String promille){
        this.id = id;
        this.name = name;
        this.promille = promille;
    }

    public Freund(){

    }

    public void setId(String id){
        this.id = id;
    }

    public String getID(){
        return this.id;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public String getPromille() {
        return promille;
    }

    public void setPromille(String promille) {
        this.promille = promille;
    }
}
