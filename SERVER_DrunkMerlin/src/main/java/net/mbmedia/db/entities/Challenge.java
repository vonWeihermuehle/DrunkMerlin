package net.mbmedia.db.entities;


import javax.persistence.*;

@Table(name="challenge")
@Entity
@NamedQuery(name="Challenge.DeleteByID", query = "delete from Challenge c where c.id = :id")
public class Challenge {

    private int id;
    private String nutzer_id;
    private String freund_id;
    private String text;

    public Challenge(){}

    public Challenge(int id, String nutzer_id, String freund_id, String text) {
        this.id = id;
        this.nutzer_id = nutzer_id;
        this.freund_id = freund_id;
        this.text = text;
    }

    public Challenge(String nutzer_id, String freund_id, String text) {
        this.nutzer_id = nutzer_id;
        this.freund_id = freund_id;
        this.text = text;
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

    @Column(name="freund_id")
    public String getFreund_id() {
        return freund_id;
    }

    public void setFreund_id(String freund_id) {
        this.freund_id = freund_id;
    }

    @Column(name="text")
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
