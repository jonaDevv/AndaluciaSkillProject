package com.jrm.repository;

import org.springframework.stereotype.Repository;
import com.jrm.model.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface SpecialtyRepository extends JpaRepository<Specialty, Long> {

    



}
