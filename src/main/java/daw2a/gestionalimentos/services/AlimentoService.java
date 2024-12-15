package daw2a.gestionalimentos.services;

import daw2a.gestionalimentos.entities.Alimento;
import daw2a.gestionalimentos.enums.EstadoSelect;
import daw2a.gestionalimentos.repositories.AlimentoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AlimentoService {

    private final AlimentoRepository alimentoRepository;

    public AlimentoService(AlimentoRepository alimentoRepository) {
        this.alimentoRepository = alimentoRepository;
    }

    // Obtener todos los alimentos con paginación
    public Page<Alimento> obtenerAlimentos(int page, int size) {
        return alimentoRepository.findAll(PageRequest.of(page, size));
    }

    // Obtener un alimento por ID
    public Optional<Alimento> obtenerAlimentoPorId(Long id) {
        return alimentoRepository.findById(id);
    }

    // Crear un nuevo alimento
    @Transactional
    public Alimento crearAlimento(Alimento alimento) {
        return alimentoRepository.save(alimento);
    }

    // Actualizar un alimento por ID
    @Transactional
    public Optional<Alimento> actualizarAlimento(Long id, Alimento alimentoActualizado) {
        return alimentoRepository.findById(id).map(alimentoExistente -> {
            alimentoActualizado.setId(id);
            return alimentoRepository.save(alimentoActualizado);
        });
    }

    // Eliminar un alimento por ID
    @Transactional
    public boolean eliminarAlimento(Long id) {
        if (alimentoRepository.existsById(id)) {
            alimentoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Verificar y mover alimentos próximos a caducar al congelador
    @Transactional
    public List<Alimento> moverAlimentosACongelador(LocalDate fechaLimite, EstadoSelect estado) {
        List<Alimento> alimentos = alimentoRepository.findByFechaCaducidadBeforeAndEstado(fechaLimite, estado);
        alimentos.forEach(alimento -> {
            alimento.setEstado(EstadoSelect.CONGELADO);
            alimento.getRecipiente().setId(1L); // Cambiar a congelador
        });
        return alimentoRepository.saveAll(alimentos);
    }

    // Rotación de productos (FIFO)
    @Transactional
    public void rotarProductos() {
        List<Alimento> alimentos = alimentoRepository.findAllByOrderByFechaCaducidadAsc();
        alimentos.forEach(alimento -> {
            if (alimento.getFechaCaducidad().isBefore(LocalDate.now())) {
                alimentoRepository.delete(alimento); // Eliminar los alimentos caducados
            }
        });
    }

    // Alertas de alimentos próximos a caducar
    public List<Alimento> alertasDeCaducidad(LocalDate fechaAviso) {
        return alimentoRepository.findByFechaCaducidadBefore(fechaAviso);
    }

    // Controlar los alimentos más usados
    public List<Alimento> controlarAlimentosMasUsados(int topN) {
        return alimentoRepository.findTopNByOrderByNumeroDeUsosDesc(topN);
    }

    // Cantidad total y disponibilidad por ubicación
    public Map<String, Long> obtenerDisponibilidadPorUbicacion() {
        return alimentoRepository.countByUbicacion().stream()
                .collect(Collectors.toMap(
                        resultado -> (String) resultado[0],
                        resultado -> (Long) resultado[1]
                ));
    }

    // Resumen de alimentos próximos a caducar agrupados por ubicación
    public Map<String, List<Alimento>> obtenerProximosACaducarPorUbicacion(LocalDate fecha) {
        return alimentoRepository.findProximosACaducarAgrupadosPorUbicacion(fecha).stream()
                .collect(Collectors.toMap(
                        resultado -> (String) resultado[0],
                        resultado -> (List<Alimento>) resultado[1]
                ));
    }
}
