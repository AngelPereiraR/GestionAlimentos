package daw2a.gestionalimentos.entities;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Seccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private Integer limite;
    private Integer accesibilidad;

    @ManyToOne
    @JoinColumn(name = "id_almacen")
    private Almacen almacen;

    @OneToMany(mappedBy = "seccion")
    private List<Recipiente> listaRecipientes;

    @ElementCollection
    private List<String> listaCategorias;
}
