package system.cama.srl.Models.Dao;

import org.springframework.data.repository.CrudRepository;

import system.cama.srl.Models.Entity.Producto;

public interface ProductoDao extends CrudRepository<Producto , Long>{
    
}
