package system.cama.srl.Models.Dao;

import org.springframework.data.jpa.repository.JpaRepository;

import system.cama.srl.Models.Entity.Usuario;
import system.cama.srl.Models.Entity.VerificacionCorreo;

public interface VerificacionCorreoDao extends JpaRepository<VerificacionCorreo, Long> {
    VerificacionCorreo findByToken(String token);
    void deleteByUsuario(Usuario usuario);
    VerificacionCorreo findByUsuario(Usuario usuario); // Método para buscar por usuario
    
}
