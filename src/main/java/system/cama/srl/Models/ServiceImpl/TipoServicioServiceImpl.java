package system.cama.srl.Models.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import system.cama.srl.Models.Dao.TipoServicioDao;
import system.cama.srl.Models.Entity.TipoServicio;
import system.cama.srl.Models.Service.TipoServicioService;

@Service
public class TipoServicioServiceImpl implements TipoServicioService{

    @Autowired
    private TipoServicioDao tipoServicioDao;

    @Override
    public List<TipoServicio> findAll() {
        // TODO Auto-generated method stub
        return (List<TipoServicio>) tipoServicioDao.findAll();
    }

    @Override
    public void save(TipoServicio tipoServicio) {
        // TODO Auto-generated method stub
        tipoServicioDao.save(tipoServicio);
    }

    @Override
    public TipoServicio findOne(Long id) {
        // TODO Auto-generated method stub
        return tipoServicioDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        tipoServicioDao.deleteById(id);
    }
    
}
