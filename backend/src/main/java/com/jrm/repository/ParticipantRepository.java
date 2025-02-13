package com.jrm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jrm.model.Participant;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    

}
