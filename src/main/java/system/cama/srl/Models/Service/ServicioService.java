package system.cama.srl.Models.Service;

import java.util.List;

import system.cama.srl.Models.Entity.Servicio;

public interface ServicioService {
    
    public List<Servicio> findAll();

	public void save(Servicio servicio);

	public Servicio findOne(Long id);

	public void delete(Long id);
}
