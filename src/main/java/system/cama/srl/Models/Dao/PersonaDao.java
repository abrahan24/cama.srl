package system.cama.srl.Models.Dao;

import org.springframework.data.repository.CrudRepository;

import system.cama.srl.Models.Entity.Persona;

public interface PersonaDao extends CrudRepository<Persona , Long>{

    
}