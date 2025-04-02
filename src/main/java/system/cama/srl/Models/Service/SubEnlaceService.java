package system.cama.srl.Models.Service;

import java.util.List;

import system.cama.srl.Models.Entity.SubEnlace;

public interface SubEnlaceService {
    
    public List<SubEnlace> findAll();

	public void save(SubEnlace subEnlace);

	public SubEnlace findOne(Long id);

	public void delete(Long id);

	public List<SubEnlace> obtenerSubEnlacesPorIdUsuario(Long id);

}
