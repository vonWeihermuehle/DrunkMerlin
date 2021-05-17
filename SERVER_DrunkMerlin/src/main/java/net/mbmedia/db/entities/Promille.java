package net.mbmedia.db.entities;

import javax.persistence.*;

@Entity
@Table(name = "promille")

@NamedQueries({
        @NamedQuery(name = "Promille.FindByUser", query = "Select p from Promille p where p.nutzer_id = :nutzer_id"),
        @NamedQuery(name = "Promille.Update", query = "Update Promille p set p.promille = :promille where p.nutzer_id = :nutzer_id")
})
public class Promille {

    private String nutzer_id;
    private String promille;

    public Promille(){}

    public Promille(String nutzer_id, String promille) {
        this.nutzer_id = nutzer_id;
        this.promille = promille;
    }

    @Id
    @Column(name="nutzer_id")
    public String getNutzer_id() {
        return nutzer_id;
    }

    public void setNutzer_id(String nutzer_id) {
        this.nutzer_id = nutzer_id;
    }

    @Column(name="promille")
    public String getPromille() {
        return promille;
    }

    public void setPromille(String promille) {
        this.promille = promille;
    }
}
