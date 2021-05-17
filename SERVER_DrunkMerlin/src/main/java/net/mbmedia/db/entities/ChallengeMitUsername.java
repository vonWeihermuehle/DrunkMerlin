package net.mbmedia.db.entities;

public class ChallengeMitUsername extends Challenge{

    private String username;

    public ChallengeMitUsername(){}
    public ChallengeMitUsername(String username) {
        this.username = username;
    }

    public ChallengeMitUsername(int id, String nutzer_id, String freund_id, String text, String username) {
        super(id, nutzer_id, freund_id, text);
        this.username = username;
    }

    public ChallengeMitUsername(String nutzer_id, String freund_id, String text, String username) {
        super(nutzer_id, freund_id, text);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
