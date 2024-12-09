package daw2a.gestionalimentos.services;
import daw2a.gestionalimentos.entities.Alimento;
import daw2a.gestionalimentos.enums.EstadoSelect;
import daw2a.gestionalimentos.repositories.AlimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AlimentoService {

    @Autowired
    private AlimentoRepository alimentoRepository;

    // Obtener todos los alimentos con paginación
    public Page<Alimento> obtenerAlimentos(int page, int size) {
        return alimentoRepository.findAll(PageRequest.of(page, size));
    }

    // Obtener un alimento por ID
    public Optional<Alimento> obtenerAlimentoPorId(Long id) {
        return alimentoRepository.findById(id);
    }

    // Crear un nuevo alimento
    public Alimento crearAlimento(Alimento alimento) {
        return alimentoRepository.save(alimento);
    }

    // Actualizar un alimento por ID
    public Optional<Alimento> actualizarAlimento(Long id, Alimento alimentoActualizado) {
        Optional<Alimento> alimentoExistente = alimentoRepository.findById(id);
        if (alimentoExistente.isPresent()) {
            alimentoActualizado.setId(id);
            return Optional.of(alimentoRepository.save(alimentoActualizado));
        }
        return Optional.empty();
    }

    // Eliminar un alimento por ID
    public boolean eliminarAlimento(Long id) {
        if (alimentoRepository.existsById(id)) {
            alimentoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Verificar y mover alimentos próximos a caducar al congelador
    public List<Alimento> moverAlimentosACongelador(LocalDate fechaLimite) {
        List<Alimento> alimentosProximosACaducar = alimentoRepository.findByFechaCaducidadBeforeAndCongeladoFalse(fechaLimite);
        for (Alimento alimento : alimentosProximosACaducar) {
            alimento.setEstado(EstadoSelect.CONGELADO);
            alimentoRepository.save(alimento); // Se marca como congelado
        }
        return alimentosProximosACaducar;
    }

    // Rotación de productos (FIFO)
    public void rotarProductos() {
        List<Alimento> alimentos = alimentoRepository.findAllByOrderByFechaEntradaAsc(); // Asumimos que los alimentos tienen un campo `fechaEntrada`
        for (Alimento alimento : alimentos) {
            if (alimento.getFechaCaducidad().isBefore(LocalDate.now())) {
                alimentoRepository.delete(alimento); // Eliminar los alimentos caducados
            }
        }
    }

    // Controlar los alimentos más usados (puedes definir la lógica según el negocio)
    public List<Alimento> controlarAlimentosMasUsados(int topN) {
        return alimentoRepository.findTopNByOrderByNumeroDeUsosDesc(PageRequest.of(0, topN));
    }
}
