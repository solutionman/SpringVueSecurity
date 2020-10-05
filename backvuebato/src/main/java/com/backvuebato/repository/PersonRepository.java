package com.backvuebato.repository;

import com.backvuebato.entity.Persons;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional(readOnly=true)
public interface PersonRepository extends JpaRepository<Persons, Integer> {
    Persons findByUserid(int userid);
    Page<Persons> findByFamilynameContainingIgnoreCaseOrFirstnameContainingIgnoreCaseOrMiddlenameContainingIgnoreCase(
            String familyName, String firstName, String middleName, Pageable pageable);
}
