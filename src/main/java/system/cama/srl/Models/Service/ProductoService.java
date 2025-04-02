package system.cama.srl.Models.Service;

import java.util.List;

import system.cama.srl.Models.Entity.Producto;

public interface ProductoService {
    
    public List<Producto> findAll();

	public void save(Producto producto);

	public Producto findOne(Long id);

	public void delete(Long id);
}
