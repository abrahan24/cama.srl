package system.cama.srl.Models.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import system.cama.srl.Models.Dao.DetalleVentaDao;
import system.cama.srl.Models.Entity.DetalleVenta;
import system.cama.srl.Models.Service.DetalleVentaService;

@Service
public class DetalleVentaServiceImpl implements DetalleVentaService{

    @Autowired
    private DetalleVentaDao detalleVentaDao;

    @Override
    public List<DetalleVenta> findAll() {
        // TODO Auto-generated method stub
        return (List<DetalleVenta>) detalleVentaDao.findAll();
    }

    @Override
    public void save(DetalleVenta detalleVenta) {
        // TODO Auto-generated method stub
        detalleVentaDao.save(detalleVenta);
    }

    @Override
    public DetalleVenta findOne(Long id) {
        // TODO Auto-generated method stub
        return detalleVentaDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        detalleVentaDao.deleteById(id);
    }
    
}
