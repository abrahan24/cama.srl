package system.cama.srl.Models.Service;

import java.util.List;

import org.springframework.data.repository.query.Param;

import system.cama.srl.Models.Entity.Cliente;

public interface ClienteService {
    
    public List<Cliente> findAll();

	public void save(Cliente cliente);

	public Cliente findOne(Long id);

	public void delete(Long id);

	public List<Cliente> findByNombresPersonaContaining(@Param("nombres") String nombres);

	public List<Cliente> findByNitContaining(@Param("nit") String nit);

	public List<Cliente> findByCiPersonaContaining(@Param("ci") String ci);

}
