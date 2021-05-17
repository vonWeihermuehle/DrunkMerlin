package net.mbmedia.db.jpa;

import net.mbmedia.db.GrundFunktionen;
import net.mbmedia.db.entities.Freunde;
import net.mbmedia.db.entities.FreundeMitKarte;
import net.mbmedia.db.entities.FreundeMitPromille;
import net.mbmedia.helper.CustomComparator;

import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class FreundJPA extends GrundFunktionen {


    public void insertFreunde(String nutzer_id, String freund_id){
        setup();
        EntityTransaction et = em.getTransaction();
        et.begin();

        Freunde f1 = new Freunde(nutzer_id, freund_id);
        Freunde f2 = new Freunde(freund_id, nutzer_id);

        try{
            em.persist(f1);
            em.persist(f2);
            et.commit();
        }catch (Exception e){
            et.rollback();
            e.printStackTrace();
        }finally {
            closeEntitiyManager();
        }
    }

    public void deleteFreunde(String nutzer_id, String freund_id){
        setup();
        EntityTransaction et = em.getTransaction();
        et.begin();

        Freunde f1 = new Freunde(nutzer_id, freund_id);
        Freunde f2 = new Freunde(freund_id, nutzer_id);

        try{
            em.remove(f1);
            em.remove(f2);
            et.commit();
        }catch (Exception e){
            et.rollback();
            e.printStackTrace();
        }finally {
            closeEntitiyManager();
        }
    }

    public ArrayList<FreundeMitPromille> holeFreunde(String nutzer_id){
        setup();

        ArrayList<FreundeMitPromille> freunde = new ArrayList<>();

        Query query = em.createNativeQuery("select f.id, f.username, fp.promille from nutzer n " +
                "inner join freunde fs on (fs.nutzer_id = n.id) " +
                "inner join nutzer f on (fs.freund_id = f.id) " +
                "left outer join promille fp on (fp.nutzer_id = f.id) " +
                "where n.id = :nutzer_id");
        query.setParameter("nutzer_id", nutzer_id);

        Query query2 = em.createNativeQuery("select n.id, 'Ich' as username, p.promille from nutzer n " +
                "left outer join promille p on (n.id = p.nutzer_id) " +
                "where n.id = :nutzer_id");

        query2.setParameter("nutzer_id", nutzer_id);


        List<Object[]> rows = query.getResultList();
        for(Object[] row : rows){
            FreundeMitPromille f = new FreundeMitPromille();
            f.setFreund_id(row[0].toString());
            f.setUsername(row[1].toString());
            if(row[2] == null){
                f.setPromille("0.0");
            }else {
                f.setPromille(row[2].toString());
            }
            freunde.add(f);
        }

        rows = query2.getResultList();
        for(Object[] row : rows){
            FreundeMitPromille f = new FreundeMitPromille();
            f.setFreund_id(row[0].toString());
            f.setUsername(row[1].toString());
            if(row[2] == null){
                f.setPromille("0.0");
            }else {
                f.setPromille(row[2].toString());
            }
            freunde.add(f);
        }

        Collections.sort(freunde, new CustomComparator());

        closeEntitiyManager();

        return freunde;
    }

    public ArrayList<FreundeMitKarte> zeigeFreundeMitKarte(String nutzer_id){
        setup();
        String sql = "select n.username as username, d.zeitpunkt as zeitpunkt, d.LatLng as latlng from drink d inner join freunde f on d.nutzer_id = f.freund_id inner join nutzer n on f.freund_id = n.id where f.nutzer_id = :nutzer_id and LatLng is not null and LatLng != '' and d.zeitpunkt > (CURRENT_DATE - INTERVAL 1 DAY) order by username asc, zeitpunkt desc;";
        Query q = em.createNativeQuery(sql).setParameter("nutzer_id", nutzer_id);
        List<Object[]> objects = q.getResultList();

        ArrayList<FreundeMitKarte> freunde = new ArrayList<>();

        for(Object[] o : objects){
            FreundeMitKarte k = new FreundeMitKarte();
            k.setUsername(o[0].toString());
            k.setZeitpunkt(o[1].toString());
            k.setLatlng(o[2].toString());
            freunde.add(k);

            System.out.println("username: " + k.getUsername());
        }

        return freunde;

    }


}
