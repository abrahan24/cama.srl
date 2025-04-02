package system.cama.srl.Models.Service;

import java.util.List;

import system.cama.srl.Models.Entity.DetalleServicio;

public interface DetalleServicioService {
    
    public List<DetalleServicio> findAll();

	public void save(DetalleServicio detalleServicio);

	public DetalleServicio findOne(Long id);

	public void delete(Long id);
}
