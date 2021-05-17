package net.mbmedia.db.jpa;

import net.mbmedia.db.GrundFunktionen;

import net.mbmedia.db.entities.Nutzer;
import net.mbmedia.db.entities.Promille;
import net.mbmedia.helper.DatumZeitHelper;


import javax.persistence.EntityTransaction;
import java.util.ArrayList;

public class nutzerJPA extends GrundFunktionen {

    public String registriere(String username, String hash){
        setup();

        ArrayList<Nutzer> n = (ArrayList<Nutzer>) em.createNamedQuery("Nutzer.findByName")
                .setParameter("name", username)
                .getResultList();

        if(n.size() > 0){
            return "Benutzername oder Email bereits vergeben";
        }

        DatumZeitHelper zeitHelper = new DatumZeitHelper();
        Nutzer neu = new Nutzer(createUUID(), username, hash, zeitHelper.getAktuelleZeitDatum());

        EntityTransaction et = em.getTransaction();
        et.begin();
        try{
            em.persist(neu);
            et.commit();
        }catch (Exception e){
            et.rollback();
            return getAllgFehlerMeldung();
        }finally {
            closeEntitiyManager();
        }

        return neu.getId();

    }

    public String login(String username, String hash){
        setup();
        ArrayList<Nutzer> n = (ArrayList<Nutzer>) em.createNamedQuery("Nutzer.Login")
                .setParameter("name", username)
                .setParameter("pw", hash)
                .getResultList();


        if(n.size() < 1){
            return "fehler";
        }

        closeEntitiyManager();
        return n.get(0).getId();
    }


    public void updateOrInsertPromille(String nutzer_id, String promille){
        setup();
        ArrayList<Promille> p = (ArrayList<Promille>) em.createNamedQuery("Promille.FindByUser")
                .setParameter("nutzer_id", nutzer_id)
                .getResultList();

        if(p.size() < 1){
            //insert
            Promille neu = new Promille(nutzer_id, promille);
            EntityTransaction t = em.getTransaction();
            t.begin();

            try{
                em.persist(neu);
                t.commit();
            }catch (Exception e){
                t.rollback();
                e.printStackTrace();
                return;
            }finally {
                closeEntitiyManager();
            }

        }else {
            Promille alt = p.get(0);
            alt.setPromille(promille);

            em.createNamedQuery("Promille.Update")
                    .setParameter("promille", promille)
                    .setParameter("nutzer_id", nutzer_id)
                    .executeUpdate();

        }


    }

}
