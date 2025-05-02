package system.cama.srl.Models.Service;

import java.util.List;

import org.springframework.data.repository.query.Param;

import system.cama.srl.Models.Entity.Producto;

public interface ProductoService {
    
    public List<Producto> findAll();

	public void save(Producto producto);

	public Producto findOne(Long id);

	public void delete(Long id);

	public List<Producto> obtenerProductosPorNombres(@Param("nom_producto") String nom_producto);

	public List<Producto> obtenerProductosPorCodigoProducto(@Param("cod_producto") String cod_producto);

	Producto obtenerProductoPorCodigo(@Param("cod_producto") String cod_producto);

}
