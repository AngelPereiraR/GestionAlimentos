package daw2a.gestionalimentos.services;
import daw2a.gestionalimentos.entities.Seccion;
import daw2a.gestionalimentos.repositories.SeccionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SeccionService {

    private final SeccionRepository seccionRepository;

    public SeccionService(SeccionRepository seccionRepository) {
        this.seccionRepository = seccionRepository;
    }

    // Obtener todas las secciones con paginación
    public Page<Seccion> obtenerSecciones(int page, int size) {
        return seccionRepository.findAll(PageRequest.of(page, size));
    }

    // Obtener una sección por ID
    public Optional<Seccion> obtenerSeccionPorId(Long id) {
        return seccionRepository.findById(id);
    }

    // Crear una nueva sección
    public Seccion crearSeccion(Seccion seccion) {
        return seccionRepository.save(seccion);
    }

    // Actualizar una sección por ID
    public Optional<Seccion> actualizarSeccion(Long id, Seccion seccionActualizada) {
        Optional<Seccion> seccionExistente = seccionRepository.findById(id);
        if (seccionExistente.isPresent()) {
            seccionActualizada.setId(id);
            return Optional.of(seccionRepository.save(seccionActualizada));
        }
        return Optional.empty();
    }

    // Eliminar una sección por ID
    public boolean eliminarSeccion(Long id) {
        if (seccionRepository.existsById(id)) {
            seccionRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Función adicional: Obtener las secciones relacionadas con un almacén
    public Page<Seccion> obtenerSeccionesPorAlmacen(Long almacenId, int page, int size) {
        return seccionRepository.findByAlmacenId(almacenId, PageRequest.of(page, size));
    }
}
