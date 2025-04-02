package system.cama.srl.Models.Service;

import java.util.List;

import system.cama.srl.Models.Entity.Usuario;

public interface UsuarioService {
    
    public List<Usuario> findAll();

	public void save(Usuario usuario);

	public Usuario findOne(Long id);

	public void delete(Long id);

	public Usuario getUsuario(String usuario,String contrasena);
}
