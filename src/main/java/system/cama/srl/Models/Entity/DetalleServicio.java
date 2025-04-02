package system.cama.srl.Models.Entity;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "detalleServicio")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DetalleServicio implements Serializable{
    
    private static final long serialVersionUID = 2629195288020321924L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_det_servicio;
    private String descripcion;
    private Double precio;
    private String ruta_imagen;
    private String estado_det_servicio;

    @ManyToOne
    @JoinColumn(name = "id_servicio")
    private Servicio servicio;

}
