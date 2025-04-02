package system.cama.srl.Models.Service;

import java.util.List;

import system.cama.srl.Models.Entity.TipoProducto;

public interface TipoProductoService {
    
    public List<TipoProducto> findAll();

	public void save(TipoProducto tipoProducto);

	public TipoProducto findOne(Long id);

	public void delete(Long id);
}
