package system.cama.srl.Models.Entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "subenlace")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SubEnlace implements Serializable{
    
    private static final long serialVersionUID = 2629195288020321924L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_sub_enlace;
    private String nom_sub_enlace;
    private String ruta_subenlace;
    private String icono_subenlace;
    private String estado_sub_enlace;

    @ManyToOne
    @JoinColumn(name = "id_enlace")
    @JsonIgnore
    private Enlace enlace;
}
