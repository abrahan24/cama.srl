package system.cama.srl.Models.Service;

import java.util.List;

import system.cama.srl.Models.Entity.DetalleVenta;

public interface DetalleVentaService {
    
    public List<DetalleVenta> findAll();

	public void save(DetalleVenta detalleVenta);

	public DetalleVenta findOne(Long id);

	public void delete(Long id);
}
