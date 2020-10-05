package com.backvuebato.repository;

import com.backvuebato.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly=true)
public interface UserRepository extends JpaRepository<Users, Integer> {
    Users findByUsername(String username);
    Users findById(int id);
}
