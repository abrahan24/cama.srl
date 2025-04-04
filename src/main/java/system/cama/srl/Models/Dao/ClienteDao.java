package system.cama.srl.Models.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import system.cama.srl.Models.Entity.Cliente;

public interface ClienteDao extends CrudRepository<Cliente , Long>{
    
    @Query(value = "SELECT c.* FROM cliente c " +
    "LEFT JOIN persona p ON p.id_persona = c.id_persona " +
    "WHERE p.nombres LIKE CONCAT('%', :nombres, '%') " +
    "AND c.estado_cliente = 'A' " +
    "AND p.estado_persona = 'A' "+
    "lIMIT 5", nativeQuery = true)
    public List<Cliente> findByNombresPersonaContaining(@Param("nombres") String nombres);

    @Query(value = "SELECT c.* FROM cliente c " +
        "LEFT JOIN persona p ON p.id_persona = c.id_persona " +
        "WHERE c.nit LIKE CONCAT( '%', :nit ,'%' )" +
        "AND c.estado_cliente = 'A' " +
        "AND p.estado_persona = 'A' "+
        "lIMIT 5", nativeQuery = true)
    public List<Cliente> findByNitContaining(@Param("nit") String nit);

    @Query(value = "SELECT c.* FROM cliente c " +
        "LEFT JOIN persona p ON p.id_persona = c.id_persona " +
        "WHERE p.ci LIKE CONCAT( '%' , :ci ,'%' ) " +
        "AND c.estado_cliente = 'A' " +
        "AND p.estado_persona = 'A' "+
        "lIMIT 5", nativeQuery = true)
    public List<Cliente> findByCiPersonaContaining(@Param("ci") String ci);
}
