package daw2a.gestionalimentos.repositories;
import daw2a.gestionalimentos.entities.Alimento;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AlimentoRepository extends JpaRepository<Alimento, Long> {
    List<Alimento> findByFechaCaducidadBeforeAndCongeladoFalse(LocalDate localDate);

    List<Alimento> findAllByOrderByFechaEntradaAsc();

    List<Alimento> findTopNByOrderByNumeroDeUsosDesc(PageRequest of);
}
