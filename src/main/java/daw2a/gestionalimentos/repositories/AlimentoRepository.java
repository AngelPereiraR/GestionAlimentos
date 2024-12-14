package daw2a.gestionalimentos.repositories;
import daw2a.gestionalimentos.entities.Alimento;
import daw2a.gestionalimentos.enums.EstadoSelect;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AlimentoRepository extends JpaRepository<Alimento, Long> {
    @Query("SELECT a FROM Alimento a WHERE a.fechaCaducidad < :fecha AND a.estado != :estado")
    List<Alimento> findByFechaCaducidadBeforeAndEstado(@Param("fecha") LocalDate fecha, @Param("estado") EstadoSelect estado);


    List<Alimento> findAllByOrderByFechaCaducidadAsc();

    List<Alimento> findTopNByOrderByNumeroDeUsosDesc(PageRequest of);
}
