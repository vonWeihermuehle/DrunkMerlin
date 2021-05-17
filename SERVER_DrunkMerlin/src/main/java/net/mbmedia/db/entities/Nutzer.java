package net.mbmedia.db.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "nutzer")

@NamedQueries({
        @NamedQuery(name="Nutzer.findByName", query="Select n from Nutzer n where n.username = :name"),
        @NamedQuery(name="Nutzer.Login", query="Select n from Nutzer n where n.username = :name and n.hash = :pw"),
})

public class Nutzer {

    private String id;
    private String username;
    private String hash;
    private Date registriert;

    public Nutzer(){}

    public Nutzer(String id, String username, String hash, Date registriert) {
        this.id = id;
        this.username = username;
        this.hash = hash;
        this.registriert = registriert;
    }

    @Id
    @Column(name="id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name="username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name="hash")
    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @Column(name="registriert")
    public Date getRegistriert() {
        return registriert;
    }

    public void setRegistriert(Date registriert) {
        this.registriert = registriert;
    }
}
