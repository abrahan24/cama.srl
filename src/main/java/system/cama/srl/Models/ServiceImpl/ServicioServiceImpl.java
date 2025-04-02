package system.cama.srl.Models.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import system.cama.srl.Models.Dao.ServicioDao;
import system.cama.srl.Models.Entity.Servicio;
import system.cama.srl.Models.Service.ServicioService;

@Service
public class ServicioServiceImpl implements ServicioService{

    @Autowired
    private ServicioDao servicioDao;

    @Override
    public List<Servicio> findAll() {
        // TODO Auto-generated method stub
        return (List<Servicio>) servicioDao.findAll();
    }

    @Override
    public void save(Servicio servicio) {
        // TODO Auto-generated method stub
        servicioDao.save(servicio);
    }

    @Override
    public Servicio findOne(Long id) {
        // TODO Auto-generated method stub
        return servicioDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        servicioDao.deleteById(id);
    }
    
}
