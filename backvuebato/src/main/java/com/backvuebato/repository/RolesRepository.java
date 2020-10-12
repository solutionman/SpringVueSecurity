package com.backvuebato.repository;

import com.backvuebato.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Integer> {
    List<Roles> findAll();
    Roles findByName(String name);
}
