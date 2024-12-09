package daw2a.gestionalimentos.entities;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Recipiente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tamanyo;

    @ManyToOne
    @JoinColumn(name = "id_seccion")
    private Seccion seccion;

    @OneToMany(mappedBy = "recipiente")
    private List<Alimento> listAlimentos;
}
