package br.com.don.erp.factory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


@ApplicationScoped
public class EntityManagerProducer {

    private EntityManagerFactory factory;


    public EntityManagerProducer() {
        this.factory = Persistence.createEntityManagerFactory("don");
    }


    @Produces
    @ApplicationScoped
    public EntityManager createEntityManager() {
        return this.factory.createEntityManager();
    }


    public void closeEntityManager(@Disposes EntityManager manager) {
        if (manager.isOpen()) {
            manager.close();
        }
    }
}
