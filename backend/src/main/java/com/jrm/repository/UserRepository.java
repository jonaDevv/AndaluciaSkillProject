package com.jrm.repository;

import java.lang.classfile.ClassFile.Option;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jrm.model.User;
import com.jrm.model.UserRole;

@Repository 
public interface UserRepository extends JpaRepository<User, Long> {

    Optional <User> findByUsername(String username);

    Optional<User> findByDni(String dni);

    @Query("SELECT u FROM User u " +
           "WHERE u.specialty.id = :specialtyId " +
           "AND :role MEMBER OF u.roles " +
           "ORDER BY (SELECT COUNT(e) FROM Evaluacion e WHERE e.user = u) ASC, u.id ASC")
    List<User> findExpertsBySpecialtyOrderByEvaluationCount(
        @Param("specialtyId") Long specialtyId,
        @Param("role") UserRole role
    );

    @Query("SELECT u FROM User u " +
       "WHERE u.specialty.id = :specialtyId " +
       "AND :role MEMBER OF u.roles " +
       "AND u.id != :excludeUserId " +
       "ORDER BY (SELECT COUNT(e) FROM Evaluacion e WHERE e.user = u) ASC, u.id ASC")
    List<User> findExpertsBySpecialtyExcludingUser(
        @Param("specialtyId") Long specialtyId,
        @Param("role") UserRole role,
        @Param("excludeUserId") Long excludeUserId
    );

}
