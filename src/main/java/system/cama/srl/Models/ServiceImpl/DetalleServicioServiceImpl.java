package system.cama.srl.Models.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import system.cama.srl.Models.Dao.DetalleServicioDao;
import system.cama.srl.Models.Entity.DetalleServicio;
import system.cama.srl.Models.Service.DetalleServicioService;

@Service
public class DetalleServicioServiceImpl implements DetalleServicioService{

    @Autowired
    private DetalleServicioDao detalleServicioDao;

    @Override
    public List<DetalleServicio> findAll() {
        // TODO Auto-generated method stub
        return (List<DetalleServicio>) detalleServicioDao.findAll();
    }

    @Override
    public void save(DetalleServicio detalleServicio) {
        // TODO Auto-generated method stub
        detalleServicioDao.save(detalleServicio);
    }

    @Override
    public DetalleServicio findOne(Long id) {
        // TODO Auto-generated method stub
        return detalleServicioDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        detalleServicioDao.deleteById(id);
    }
    
}
