package com.jrm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jrm.model.Prueba;

@Repository
public interface PruebaRepository  extends JpaRepository<Prueba, Long> {

}
