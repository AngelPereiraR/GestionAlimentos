package daw2a.gestionalimentos.repositories;
import daw2a.gestionalimentos.entities.Recipiente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipienteRepository extends JpaRepository<Recipiente, Long> {
    Page<Recipiente> findByTipo(String tipo, PageRequest of);
}
