package system.cama.srl.Models.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import system.cama.srl.Models.Dao.VentaDao;
import system.cama.srl.Models.Entity.Venta;
import system.cama.srl.Models.Service.VentaService;

@Service
public class VentaServiceImpl implements VentaService{

    @Autowired
    private VentaDao ventaDao;

    @Override
    public List<Venta> findAll() {
        // TODO Auto-generated method stub
        return (List<Venta>) ventaDao.findAll();
    }

    @Override
    public void save(Venta venta) {
        // TODO Auto-generated method stub
        ventaDao.save(venta);
    }

    @Override
    public Venta findOne(Long id) {
        // TODO Auto-generated method stub
        return ventaDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        ventaDao.deleteById(id);
    }
    
}
