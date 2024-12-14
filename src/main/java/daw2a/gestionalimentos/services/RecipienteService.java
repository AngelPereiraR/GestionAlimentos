package daw2a.gestionalimentos.services;
import daw2a.gestionalimentos.entities.Recipiente;
import daw2a.gestionalimentos.repositories.RecipienteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RecipienteService {

    private final RecipienteRepository recipienteRepository;

    public RecipienteService(RecipienteRepository recipienteRepository) {
        this.recipienteRepository = recipienteRepository;
    }

    // Obtener todos los recipientes con paginación
    public Page<Recipiente> obtenerRecipientes(int page, int size) {
        return recipienteRepository.findAll(PageRequest.of(page, size));
    }

    // Obtener un recipiente por ID
    public Optional<Recipiente> obtenerRecipientePorId(Long id) {
        return recipienteRepository.findById(id);
    }

    // Crear un nuevo recipiente
    public Recipiente crearRecipiente(Recipiente recipiente) {
        return recipienteRepository.save(recipiente);
    }

    // Actualizar un recipiente por ID
    public Optional<Recipiente> actualizarRecipiente(Long id, Recipiente recipienteActualizado) {
        Optional<Recipiente> recipienteExistente = recipienteRepository.findById(id);
        if (recipienteExistente.isPresent()) {
            recipienteActualizado.setId(id);
            return Optional.of(recipienteRepository.save(recipienteActualizado));
        }
        return Optional.empty();
    }

    // Eliminar un recipiente por ID
    public boolean eliminarRecipiente(Long id) {
        if (recipienteRepository.existsById(id)) {
            recipienteRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Función adicional: Comprobar espacio disponible en un recipiente (según volumen, peso, etc.)
    public boolean verificarEspacioDisponible(Long recipienteId, double cantidad) {
        Optional<Recipiente> recipienteOpt = recipienteRepository.findById(recipienteId);
        if (recipienteOpt.isPresent()) {
            Recipiente recipiente = recipienteOpt.get();
            return recipiente.getTamanyo() >= cantidad;
        }
        return false;
    }
}
