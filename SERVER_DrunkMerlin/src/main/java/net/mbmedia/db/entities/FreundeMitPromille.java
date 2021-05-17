package net.mbmedia.db.entities;

public class FreundeMitPromille extends Freunde {

    private String promille;
    private String username;

    public FreundeMitPromille(String freund_id, String username, String promille){
        this.promille = promille;
        this.username = username;
        this.freund_id = freund_id;
    }

    public FreundeMitPromille(){}

    public String getPromille() {
        return promille;
    }

    public void setPromille(String promille) {
        this.promille = promille;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
