package system.cama.srl.Models.ServiceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import system.cama.srl.Models.Dao.PersonaDao;
import system.cama.srl.Models.Entity.Persona;
import system.cama.srl.Models.Service.PersonaService;

@Service
public class PersonaServiceImpl implements PersonaService{
    @Autowired
    private PersonaDao personaDao;

    @Override
    public List<Persona> findAll() {
        // TODO Auto-generated method stub
        return (List<Persona>) personaDao.findAll();
    }

    @Override
    public void save(Persona persona) {
        // TODO Auto-generated method stub
        personaDao.save(persona);
    }

    @Override
    public Persona findOne(Long id) {
        // TODO Auto-generated method stub
        return personaDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        personaDao.deleteById(id);
    }

    @Override
    public Optional<Persona> findByCi(String ci) {
        // TODO Auto-generated method stub
        return personaDao.findByCi(ci);
    }

    @Override
    public Optional<Persona> findByNumCelular(String numCelular) {
        // TODO Auto-generated method stub
        return personaDao.findByNumCelular(numCelular);
    }


}
