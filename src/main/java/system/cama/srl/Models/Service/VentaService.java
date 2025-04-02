package system.cama.srl.Models.Service;

import java.util.List;

import system.cama.srl.Models.Entity.Venta;

public interface VentaService {
    
    public List<Venta> findAll();

	public void save(Venta venta);

	public Venta findOne(Long id);

	public void delete(Long id);
}
