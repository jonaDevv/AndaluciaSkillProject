package com.jrm.repository;

import org.springframework.stereotype.Repository;
import com.jrm.model.Specialty;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface SpecialtyRepository extends JpaRepository<Specialty, Long> {

    Optional<Specialty> findByCod(String cod);

    Optional<Specialty> findByName(String name);



}
