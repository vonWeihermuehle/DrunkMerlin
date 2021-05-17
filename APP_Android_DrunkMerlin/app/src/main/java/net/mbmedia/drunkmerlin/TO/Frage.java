package net.mbmedia.drunkmerlin.TO;

public class Frage {

    private int id = -1;
    private String frage;

    public Frage(int id, String frage){
        this.id = id;
        this.frage =frage;
    }

    public Frage(String frage){
        this.frage = frage;
    }

    public Frage(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String get() {
        return frage;
    }

    public void set(String frage) {
        this.frage = frage;
    }
}
