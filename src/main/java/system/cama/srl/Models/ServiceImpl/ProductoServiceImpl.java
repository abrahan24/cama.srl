package system.cama.srl.Models.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import system.cama.srl.Models.Dao.ProductoDao;
import system.cama.srl.Models.Entity.Producto;
import system.cama.srl.Models.Service.ProductoService;

@Service
public class ProductoServiceImpl implements ProductoService{

    @Autowired
    private ProductoDao productoDao;

    @Override
    public List<Producto> findAll() {
        // TODO Auto-generated method stub
        return (List<Producto>) productoDao.findAll();
    }

    @Override
    public void save(Producto producto) {
        // TODO Auto-generated method stub
        productoDao.save(producto);
    }

    @Override
    public Producto findOne(Long id) {
        // TODO Auto-generated method stub
        return productoDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        productoDao.deleteById(id);
    }
    
}
