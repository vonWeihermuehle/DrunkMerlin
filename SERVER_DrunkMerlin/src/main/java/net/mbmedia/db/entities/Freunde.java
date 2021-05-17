package net.mbmedia.db.entities;

import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Table(name="freunde")

@NamedQuery(name="Freund.Finde", query = "select f.id, f.username, fp.promille from nutzer n  inner join freunde fs on (fs.nutzer_id = n.id) inner join nutzer f on (fs.freund_id = f.id) left outer join promille fp on (fp.nutzer_id = f.id) where n.id = :nutzer_id order by promille desc;")
public class Freunde {

    public String nutzer_id;
    public String freund_id;

    public Freunde(){}

    public Freunde(String nutzer_id, String freund_id) {
        this.nutzer_id = nutzer_id;
        this.freund_id = freund_id;
    }

    public String getNutzer_id() {
        return nutzer_id;
    }

    public void setNutzer_id(String nutzer_id) {
        this.nutzer_id = nutzer_id;
    }

    public String getFreund_id() {
        return freund_id;
    }

    public void setFreund_id(String freund_id) {
        this.freund_id = freund_id;
    }
}
