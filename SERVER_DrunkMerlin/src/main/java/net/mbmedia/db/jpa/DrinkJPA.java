package net.mbmedia.db.jpa;

import net.mbmedia.db.GrundFunktionen;
import net.mbmedia.db.entities.Drink;
import javax.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.Date;


public class DrinkJPA extends GrundFunktionen {

    public void fuegeHinzu(String nutzer_id, String name, String prozent, String menge, Date zeitpunkt, String latlng){
        System.out.println("Menge: " + menge);

        Drink d = new Drink(nutzer_id, name, menge, prozent, zeitpunkt, latlng);

        System.out.println("Menge: " + menge);

        setup();
        EntityTransaction et = em.getTransaction();
        et.begin();

        try{
            em.persist(d);
            et.commit();
        }catch (Exception e){
            e.printStackTrace();
            et.rollback();
        }finally {
            closeEntitiyManager();
        }
    }

    public void loesche(String nutzer_id, int id){
        setup();
        EntityTransaction et = startTransaction();

        String hql = "delete from Drink d where d.id = :id and d.nutzer_id = :nutzer_id";

        try{
            em.createQuery(hql).setParameter("id", id).setParameter("nutzer_id", nutzer_id).executeUpdate();
            et.commit();
        }catch (Exception e){
            e.printStackTrace();
            et.rollback();
        }finally {
            closeEntitiyManager();
        }
    }

    public void loescheAlle(String nutzer_id){
        setup();

        String hql = "delete from Drink d where d.nutzer_id = :nutzer_id";

        EntityTransaction et = startTransaction();

        try{
            em.createQuery(hql).setParameter("nutzer_id", nutzer_id).executeUpdate();
            et.commit();
        }catch (Exception e){
            e.printStackTrace();
            et.rollback();
        }finally {
            closeEntitiyManager();
        }
    }

    public ArrayList<Drink> getAlleDrinks(String nutzer_id){
        setup();
        ArrayList<Drink> drinks = new ArrayList<>();

        try{
            drinks = (ArrayList<Drink>) em.createQuery("select d from Drink d where d.nutzer_id = :nutzer_id order by d.zeitpunkt desc")
                    .setParameter("nutzer_id", nutzer_id)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            closeEntitiyManager();
            return drinks;
        }

    }

}
