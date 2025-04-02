package system.cama.srl.Models.Dao;

import org.springframework.data.repository.CrudRepository;

import system.cama.srl.Models.Entity.Cliente;

public interface ClienteDao extends CrudRepository<Cliente , Long>{
    
}
