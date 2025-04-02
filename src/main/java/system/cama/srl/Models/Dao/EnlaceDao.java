package system.cama.srl.Models.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import system.cama.srl.Models.Entity.Enlace;

public interface EnlaceDao extends CrudRepository< Enlace, Long>{
    
    @Query(value = """
                SELECT e.*
                FROM enlace e
                LEFT JOIN rol r ON r.id_rol = e.id_rol
                LEFT JOIN usuario u ON u.id_rol = r.id_rol
                WHERE u.id_usuario = ?1
            """, nativeQuery = true)
    public List<Enlace> obtenerEnlacesPorIdUsuario(Long id);
}
