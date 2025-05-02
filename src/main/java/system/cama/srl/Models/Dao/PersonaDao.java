package system.cama.srl.Models.Dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import system.cama.srl.Models.Entity.Persona;

public interface PersonaDao extends CrudRepository<Persona , Long>{

    @Query("SELECT p FROM Persona p WHERE p.ci = :ci")
    Optional<Persona> findByCi(@Param("ci") String ci);
    
    @Query("SELECT p FROM Persona p WHERE p.num_celular = :numCelular")
    Optional<Persona> findByNumCelular(@Param("numCelular") String numCelular);
}