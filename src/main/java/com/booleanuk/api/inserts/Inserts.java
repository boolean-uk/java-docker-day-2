package com.booleanuk.api.inserts;


import com.booleanuk.api.model.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class Inserts {

    @PersistenceContext
    private EntityManager entityManager;
    @Transactional
    public void insertWithEntityManager(Role role) {
        this.entityManager.persist(role);
    }
}
