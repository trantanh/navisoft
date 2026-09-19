package com.trantanh.navipos.manager;

import com.trantanh.navipos.config.SpringContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.criteria.CriteriaQuery;

import java.util.List;

/**
 * Compatibility adapter for the legacy DAO implementations.
 *
 * Entity manager creation and connection pooling are owned by Spring Boot.
 * New persistence code should use Spring Data repositories directly; this
 * adapter keeps the current application behaviour while the DAOs are migrated.
 */
public class DatabaseManager<T> {

    private EntityManagerFactory entityManagerFactory;

    public void setup() {
        entityManagerFactory = SpringContext.getBean(EntityManagerFactory.class);
    }

    /**
     * The factory is shared and closed by Spring when the desktop app exits.
     */
    public void exit() {
        // Lifecycle is managed by the Spring application context.
    }

    public void saveOrUpdate(T entity) {
        executeInTransaction(entityManager -> entityManager.merge(entity));
    }

    public <R> R read(Class<R> type, int id) {
        EntityManager entityManager = createEntityManager();
        try {
            return entityManager.find(type, id);
        } finally {
            entityManager.close();
        }
    }

    public void delete(T entity) {
        executeInTransaction(entityManager -> {
            T managed = entityManager.contains(entity) ? entity : entityManager.merge(entity);
            entityManager.remove(managed);
        });
    }

    public List<T> findAll(Class<T> type) {
        EntityManager entityManager = createEntityManager();
        try {
            CriteriaQuery<T> query = entityManager.getCriteriaBuilder().createQuery(type);
            query.select(query.from(type));
            return entityManager.createQuery(query).getResultList();
        } finally {
            entityManager.close();
        }
    }

    private EntityManager createEntityManager() {
        if (entityManagerFactory == null) {
            setup();
        }
        return entityManagerFactory.createEntityManager();
    }

    private void executeInTransaction(EntityManagerAction action) {
        EntityManager entityManager = createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            action.execute(entityManager);
            transaction.commit();
        } catch (RuntimeException exception) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw exception;
        } finally {
            entityManager.close();
        }
    }

    @FunctionalInterface
    private interface EntityManagerAction {
        void execute(EntityManager entityManager);
    }
}
