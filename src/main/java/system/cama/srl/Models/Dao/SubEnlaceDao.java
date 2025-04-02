package system.cama.srl.Models.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import system.cama.srl.Models.Entity.SubEnlace;

public interface SubEnlaceDao extends CrudRepository<SubEnlace , Long>{
    

    @Query(value = """
            select s.* from subenlace s
            left join enlace e on e.id_enlace = s.id_enlace
            left join rol r on r.id_rol = e.id_rol
            left join usuario u on u.id_rol = r.id_rol
            where u.id_usuario = ?1
                """, nativeQuery = true)
    public List<SubEnlace> obtenerSubEnlacesPorIdUsuario(Long id);
}
