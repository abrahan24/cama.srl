package system.cama.srl.Models.Service;

import java.util.List;

import system.cama.srl.Models.Entity.Rol;

public interface RolService {
    
    public List<Rol> findAll();

	public void save(Rol rol);

	public Rol findOne(Long id);

	public void delete(Long id);
}
