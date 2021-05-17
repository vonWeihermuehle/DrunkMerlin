package net.mbmedia.db.jpa;

import net.mbmedia.db.GrundFunktionen;
import net.mbmedia.db.entities.Challenge;
import net.mbmedia.db.entities.ChallengeMitUsername;

import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class ChallengeJPA extends GrundFunktionen {

    public void fuegeHinzu(String nutzer_id, String freund_id, String text){
        setup();

        Challenge c = new Challenge(nutzer_id, freund_id, text);

        EntityTransaction et = em.getTransaction();
        et.begin();

        try{
            em.persist(c);
            et.commit();
        }catch (Exception e){
            e.printStackTrace();
            et.rollback();
        }finally {
            closeEntitiyManager();
        }
    }

    public void loesche(int id){
        setup();
        EntityTransaction et = em.getTransaction();
        et.begin();
        em.createNamedQuery("Challenge.DeleteByID")
                .setParameter("id", id)
                .executeUpdate();

        et.commit();

        closeEntitiyManager();
    }

    public ArrayList<ChallengeMitUsername> zeige(String nutzer_id){
        setup();

        ArrayList<ChallengeMitUsername> challenges = new ArrayList<>();

        Query q = em.createNativeQuery("select no.*, n.username as author from challenge no left outer join nutzer n on n.id = no.freund_id where no.nutzer_id = :nutzer_id or no.nutzer_id is null limit 1;");
        q.setParameter("nutzer_id", nutzer_id);

        List<Object[]> objects = q.getResultList();
        for(Object[] o : objects){
            ChallengeMitUsername c = new ChallengeMitUsername();
            c.setId(Integer.parseInt(o[0].toString()));
            c.setNutzer_id(o[1].toString());
            c.setFreund_id(o[2].toString());
            c.setText(o[3].toString());
            c.setUsername(o[4].toString());
            challenges.add(c);
        }

        closeEntitiyManager();

        return challenges;

    }
}
