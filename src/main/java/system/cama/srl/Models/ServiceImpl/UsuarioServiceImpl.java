package system.cama.srl.Models.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import system.cama.srl.Models.Dao.UsuarioDao;
import system.cama.srl.Models.Entity.Enlace;
import system.cama.srl.Models.Entity.Usuario;
import system.cama.srl.Models.Service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService{

    @Autowired
    private UsuarioDao usuarioDao;

    @Override
    public List<Usuario> findAll() {
        // TODO Auto-generated method stub
        return (List<Usuario>) usuarioDao.findAll();
    }

    @Override
    public void save(Usuario usuario) {
        // TODO Auto-generated method stub
        usuarioDao.save(usuario);
    }

    @Override
    public Usuario findOne(Long id) {
        // TODO Auto-generated method stub
        return usuarioDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        usuarioDao.deleteById(id);
    }

    @Override
    public Usuario getUsuario(String usuario, String contrasena) {
        // TODO Auto-generated method stub
        return usuarioDao.getUsuario(usuario, contrasena);
    }
    
    @Transactional(readOnly = true) // Mantiene la sesión activa para evitar LazyInitializationException
    public Usuario obtenerUsuarioConMenu(Long id) {
        Usuario usuario = usuarioDao.findById(id).orElse(null);

        if (usuario != null) {
            // Forzar la carga de enlaces y subEnlaces
            usuario.getRol().getEnlaces().size(); // Accede a la lista para inicializarla

            for (Enlace enlace : usuario.getRol().getEnlaces()) {
                enlace.getSubEnlaces().size(); // Inicializa la lista de subEnlaces
            }
        }

        return usuario;
    }
}
