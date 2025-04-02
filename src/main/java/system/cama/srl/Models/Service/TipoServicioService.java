package system.cama.srl.Models.Service;

import java.util.List;

import system.cama.srl.Models.Entity.TipoServicio;

public interface TipoServicioService {
    
    public List<TipoServicio> findAll();

	public void save(TipoServicio tipoServicio);

	public TipoServicio findOne(Long id);

	public void delete(Long id);
}
