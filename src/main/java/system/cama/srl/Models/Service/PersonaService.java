package system.cama.srl.Models.Service;

import java.util.List;

import system.cama.srl.Models.Entity.Persona;

public interface PersonaService {
    
    public List<Persona> findAll();

	public void save(Persona persona);

	public Persona findOne(Long id);

	public void delete(Long id);
}
