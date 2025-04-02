package system.cama.srl.Models.Entity;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tipoProducto")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TipoProducto implements Serializable{
    
    private static final long serialVersionUID = 2629195288020321924L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_tipo_producto;
    private String nom_tipo_producto;
    private String estado_tipo_producto;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoProducto", fetch = FetchType.LAZY)
    private List<Producto> productos;
}
