package system.cama.srl.Models.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import system.cama.srl.Models.Dao.SubEnlaceDao;
import system.cama.srl.Models.Entity.SubEnlace;
import system.cama.srl.Models.Service.SubEnlaceService;

@Service
public class SubEnlaceServiceImpl implements SubEnlaceService{

    @Autowired
    private SubEnlaceDao subEnlaceDao;

    @Override
    public List<SubEnlace> findAll() {
        // TODO Auto-generated method stub
        return (List<SubEnlace>) subEnlaceDao.findAll();
    }

    @Override
    public void save(SubEnlace subEnlace) {
        // TODO Auto-generated method stub
        subEnlaceDao.save(subEnlace);
    }

    @Override
    public SubEnlace findOne(Long id) {
        // TODO Auto-generated method stub
        return subEnlaceDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        subEnlaceDao.deleteById(id);
    }

    @Override
    public List<SubEnlace> obtenerSubEnlacesPorIdUsuario(Long id) {
        // TODO Auto-generated method stub
        return subEnlaceDao.obtenerSubEnlacesPorIdUsuario(id);
    }
    
}
