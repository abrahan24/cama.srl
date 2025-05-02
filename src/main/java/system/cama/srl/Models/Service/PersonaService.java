package system.cama.srl.Models.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.query.Param;

import system.cama.srl.Models.Entity.Persona;

public interface PersonaService {
    
    public List<Persona> findAll();

	public void save(Persona persona);

	public Persona findOne(Long id);

	public void delete(Long id);

    Optional<Persona> findByCi(@Param("ci") String ci);
	
    Optional<Persona> findByNumCelular(@Param("numCelular") String numCelular);
}
