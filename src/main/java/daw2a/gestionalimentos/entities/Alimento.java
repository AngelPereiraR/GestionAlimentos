package daw2a.gestionalimentos.entities;

import daw2a.gestionalimentos.enums.CategoriaSelect;
import daw2a.gestionalimentos.enums.EstadoSelect;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Setter
@Getter
@NoArgsConstructor @AllArgsConstructor
@ToString
@Builder
public class Alimento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private Boolean perecedero;
    private Boolean abierto;
    private Integer tamano;
    private LocalDate fechaCaducidad;
    private int numeroDeUsos;

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

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Alimento alimento = (Alimento) o;
        return getId() != null && Objects.equals(getId(), alimento.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}