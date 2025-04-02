package system.cama.srl.Models.Service;

import java.util.List;

import system.cama.srl.Models.Entity.Cliente;

public interface ClienteService {
    
    public List<Cliente> findAll();

	public void save(Cliente cliente);

	public Cliente findOne(Long id);

	public void delete(Long id);
}
