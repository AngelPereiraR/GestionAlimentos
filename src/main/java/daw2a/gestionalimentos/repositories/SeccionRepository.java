package daw2a.gestionalimentos.repositories;
import daw2a.gestionalimentos.entities.Seccion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeccionRepository extends JpaRepository<Seccion, Long> {
    Page<Seccion> findByTipo(String tipo, PageRequest of);

    Page<Seccion> findByAlmacenId(Long almacenId, PageRequest of);
}
