package system.cama.srl.Models.Dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import system.cama.srl.Models.Entity.Usuario;

public interface UsuarioDao extends CrudRepository<Usuario , Long>{
    
    @Query(value = "SELECT u.* FROM usuario u WHERE u.user_name = ?1 AND u.contrasena = ?2", nativeQuery = true)
    public Usuario getUsuario(String usuario,String contrasena);
    
}
