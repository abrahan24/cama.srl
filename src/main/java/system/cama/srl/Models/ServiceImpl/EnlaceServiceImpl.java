package system.cama.srl.Models.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import system.cama.srl.Models.Dao.EnlaceDao;
import system.cama.srl.Models.Entity.Enlace;
import system.cama.srl.Models.Service.EnlaceService;

@Service
public class EnlaceServiceImpl implements EnlaceService{

    @Autowired
    private EnlaceDao enlaceDao;

    @Override
    public List<Enlace> findAll() {
        // TODO Auto-generated method stub
        return (List<Enlace>) enlaceDao.findAll();
    }

    @Override
    public void save(Enlace enlace) {
        // TODO Auto-generated method stub
        enlaceDao.save(enlace);
    }

    @Override
    public Enlace findOne(Long id) {
        // TODO Auto-generated method stub
        return enlaceDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        enlaceDao.deleteById(id);
    }

    @Override
    public List<Enlace> obtenerEnlacesPorIdUsuario(Long id) {
        // TODO Auto-generated method stub
        return enlaceDao.obtenerEnlacesPorIdUsuario(id);
    }
    
}
