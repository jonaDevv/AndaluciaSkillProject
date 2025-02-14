package com.jrm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jrm.model.Test;

public interface TestRepository  extends JpaRepository<Test, Long> {

}
