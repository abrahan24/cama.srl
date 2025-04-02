package system.cama.srl.Models.Service;

import java.util.List;

import system.cama.srl.Models.Entity.Enlace;

public interface EnlaceService {
    
    public List<Enlace> findAll();

	public void save(Enlace enlace);

	public Enlace findOne(Long id);

	public void delete(Long id);

	public List<Enlace> obtenerEnlacesPorIdUsuario(Long id);

}
