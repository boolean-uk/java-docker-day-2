package com.booleanuk.api.repository;

import com.booleanuk.api.model.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class RoleInsertRepository {

    @PersistenceContext
    private EntityManager entityManager;


    @Transactional
    public void insertWithQuery(Role role) {
        entityManager.createNativeQuery("INSERT INTO Role (name) VALUES (?,?)")
                .setParameter(1, role.getId())
                .setParameter(2, role.getName())
                .executeUpdate();
}}
