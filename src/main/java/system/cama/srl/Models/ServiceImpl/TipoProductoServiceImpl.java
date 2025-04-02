package system.cama.srl.Models.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import system.cama.srl.Models.Dao.TipoProductoDao;
import system.cama.srl.Models.Entity.TipoProducto;
import system.cama.srl.Models.Service.TipoProductoService;

@Service
public class TipoProductoServiceImpl implements TipoProductoService{

    @Autowired
    private TipoProductoDao tipoProductoDao;

    @Override
    public List<TipoProducto> findAll() {
        // TODO Auto-generated method stub
        return (List<TipoProducto>) tipoProductoDao.findAll();
    }

    @Override
    public void save(TipoProducto tipoProducto) {
        // TODO Auto-generated method stub
        tipoProductoDao.save(tipoProducto);
    }

    @Override
    public TipoProducto findOne(Long id) {
        // TODO Auto-generated method stub
        return tipoProductoDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        tipoProductoDao.deleteById(id);
    }
    
}
