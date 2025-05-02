package system.cama.srl.Models.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import system.cama.srl.Models.Entity.Producto;

public interface ProductoDao extends CrudRepository<Producto , Long>{
    
    @Query(value = """
    select p.* from producto p where p.estado_producto = 'A' and p.nom_producto like concat( :nom_producto,'%')
    """,nativeQuery = true)
    public List<Producto> obtenerProductosPorNombres(@Param("nom_producto") String nom_producto);

    @Query(value = """
    select p.* from producto p where p.estado_producto = 'A' and p.cod_producto  like concat(:cod_producto,'%')
    """,nativeQuery = true)
    public List<Producto> obtenerProductosPorCodigoProducto(@Param("cod_producto") String cod_producto);

    @Query(value = """
    select p.* from producto p where p.cod_producto = :cod_producto
    """,nativeQuery = true)
    Producto obtenerProductoPorCodigo(@Param("cod_producto") String cod_producto);
}
