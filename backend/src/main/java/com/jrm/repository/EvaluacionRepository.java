package com.jrm.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jrm.model.Evaluacion;
import com.jrm.repository.projection.ReasignacionStats;

@Repository
public interface EvaluacionRepository extends JpaRepository<Evaluacion, Long> {

    List<Evaluacion> findByUserId(Long expertoId);


    @Query("SELECT e.user.id AS expertoId, COUNT(e) AS totalReasignaciones " +
           "FROM Evaluacion e " +
           "WHERE SIZE(e.historialReasignaciones) > 0 " +
           "GROUP BY e.user.id")
    List<ReasignacionStats> getEstadisticasReasignacion();

    @Query("SELECT DISTINCT e FROM Evaluacion e " +
       "LEFT JOIN FETCH e.items i " + // Alias para items
       "LEFT JOIN FETCH e.prueba p " +
       "LEFT JOIN FETCH e.participant pt " +
       "WHERE e.id = :id")
    Optional<Evaluacion> findByIdWithItems(@Param("id") Long id);
    
  

    // Busca evaluaciones pendientes por username del experto
    @Query("SELECT e FROM Evaluacion e " +
       "JOIN FETCH e.user u " +
       "JOIN FETCH e.participant p " +
       "JOIN FETCH e.prueba pr " +
       "WHERE u.username = :username " +
       "AND e.estado = :estado")
    List<Evaluacion> findByUserUsernameAndEstado(
        @Param("username") String username,
        @Param("estado") String estado
    );



    @Query("SELECT e FROM Evaluacion e " +
       "WHERE e.estado = 'FINALIZADA' " +
       "AND e.pFinalObtenida = (" +
         "SELECT MAX(e2.pFinalObtenida) FROM Evaluacion e2 " +
         "WHERE e2.prueba = e.prueba AND e2.estado = 'FINALIZADA'" +
       ")")
    List<Evaluacion> findGanadores();
    



}
