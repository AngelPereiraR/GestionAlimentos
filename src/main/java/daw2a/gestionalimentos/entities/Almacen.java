package daw2a.gestionalimentos.entities;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Almacen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String temp;

    @OneToMany(mappedBy = "almacen")
    private List<Seccion> listSeccion;
}
