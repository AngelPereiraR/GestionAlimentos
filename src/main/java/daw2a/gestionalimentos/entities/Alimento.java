package daw2a.gestionalimentos.entities;
import daw2a.gestionalimentos.enums.CategoriaSelect;
import daw2a.gestionalimentos.enums.EstadoSelect;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Alimento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private Boolean perecedero;
    private Boolean abierto;
    private Integer tamano;
    private LocalDate fechaCaducidad;

    @ManyToOne
    @JoinColumn(name = "id_seccion")
    private Seccion seccion;

    @ManyToOne
    @JoinColumn(name = "id_recipiente")
    private Recipiente recipiente;

    @Enumerated(EnumType.STRING)
    private CategoriaSelect categoria;

    @Enumerated(EnumType.STRING)
    private EstadoSelect estado;
}