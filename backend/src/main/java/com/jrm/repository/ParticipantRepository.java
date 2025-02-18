package com.jrm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jrm.model.Participant;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    

}
