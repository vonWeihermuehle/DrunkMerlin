package net.mbmedia.db;

import org.hibernate.persister.walking.spi.EntityIdentifierDefinition;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Manager {

    protected EntityManager em;
    protected static EntityManagerFactory entityManagerFactory;


    public void setup(){

        if(entityManagerFactory == null){

            entityManagerFactory = Persistence.createEntityManagerFactory("persistence");
        }

        if(em == null || (!em.isOpen()))
        {
            em = entityManagerFactory.createEntityManager();
        }


    }

    public void closeEntitiyManager(){
        while(em.isOpen())
        {
            em.close();
        }
        em = null;
    }

    public EntityTransaction startTransaction(){
        EntityTransaction et = em.getTransaction();
        et.begin();

        return et;
    }




}