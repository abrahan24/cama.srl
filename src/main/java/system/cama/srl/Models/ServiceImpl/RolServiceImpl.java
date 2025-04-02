package system.cama.srl.Models.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import system.cama.srl.Models.Dao.RolDao;
import system.cama.srl.Models.Entity.Rol;
import system.cama.srl.Models.Service.RolService;

@Service
public class RolServiceImpl implements RolService{

    @Autowired
    private RolDao rolDao;

    @Override
    public List<Rol> findAll() {
        // TODO Auto-generated method stub
        return (List<Rol>) rolDao.findAll();
    }

    @Override
    public void save(Rol rol) {
        // TODO Auto-generated method stub
        rolDao.save(rol);
    }

    @Override
    public Rol findOne(Long id) {
        // TODO Auto-generated method stub
        return rolDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        rolDao.deleteById(id);
    }
    
}
