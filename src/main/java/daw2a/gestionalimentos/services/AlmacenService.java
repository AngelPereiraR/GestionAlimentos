package daw2a.gestionalimentos.services;
import daw2a.gestionalimentos.entities.Almacen;
import daw2a.gestionalimentos.repositories.AlmacenRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AlmacenService {

    private final AlmacenRepository almacenRepository;

    public AlmacenService(AlmacenRepository almacenRepository) {
        this.almacenRepository = almacenRepository;
    }

    // Obtener todos los almacenes con paginación
    public Page<Almacen> obtenerAlmacenes(int page, int size) {
        return almacenRepository.findAll(PageRequest.of(page, size));
    }

    // Obtener un almacén por ID
    public Optional<Almacen> obtenerAlmacenPorId(Long id) {
        return almacenRepository.findById(id);
    }

    // Crear un nuevo almacén
    public Almacen crearAlmacen(Almacen almacen) {
        return almacenRepository.save(almacen);
    }

    // Actualizar un almacén por ID
    public Optional<Almacen> actualizarAlmacen(Long id, Almacen almacenActualizado) {
        Optional<Almacen> almacenExistente = almacenRepository.findById(id);
        if (almacenExistente.isPresent()) {
            almacenActualizado.setId(id);
            return Optional.of(almacenRepository.save(almacenActualizado));
        }
        return Optional.empty();
    }

    // Eliminar un almacén por ID
    public boolean eliminarAlmacen(Long id) {
        if (almacenRepository.existsById(id)) {
            almacenRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
