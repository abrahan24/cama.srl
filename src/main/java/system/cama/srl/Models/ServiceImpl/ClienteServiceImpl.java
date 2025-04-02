package system.cama.srl.Models.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import system.cama.srl.Models.Dao.ClienteDao;
import system.cama.srl.Models.Entity.Cliente;
import system.cama.srl.Models.Service.ClienteService;

@Service
public class ClienteServiceImpl implements ClienteService{

    @Autowired
    private ClienteDao clienteDao;

    @Override
    public List<Cliente> findAll() {
        // TODO Auto-generated method stub
        return (List<Cliente>) clienteDao.findAll();
    }

    @Override
    public void save(Cliente cliente) {
        // TODO Auto-generated method stub
        clienteDao.save(cliente);
    }

    @Override
    public Cliente findOne(Long id) {
        // TODO Auto-generated method stub
        return clienteDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        clienteDao.deleteById(id);
    }
    
}
