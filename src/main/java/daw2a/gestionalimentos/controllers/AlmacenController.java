package daw2a.gestionalimentos.controllers;
import daw2a.gestionalimentos.entities.Almacen;
import daw2a.gestionalimentos.repositories.AlmacenRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/almaenes")
public class AlmacenController {

    private final AlmacenRepository almacenRepository;

    public AlmacenController(AlmacenRepository almacenRepository) {
        this.almacenRepository = almacenRepository;
    }

    // Obtener todos los almacenes con paginación
    @GetMapping
    public ResponseEntity<Page<Almacen>> obtenerAlmacenes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Almacen> almacenes = almacenRepository.findAll(PageRequest.of(page, size));
        return ResponseEntity.ok(almacenes);
    }

    // Obtener un almacén por ID
    @GetMapping("/{id}")
    public ResponseEntity<Almacen> obtenerAlmacenPorId(@PathVariable Long id) {
        Optional<Almacen> almacen = almacenRepository.findById(id);
        return almacen.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Crear un nuevo almacén
    @PostMapping
    public ResponseEntity<Almacen> guardarAlmacen(@RequestBody Almacen almacen) {
        Almacen nuevoAlmacen = almacenRepository.save(almacen);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoAlmacen);
    }

    // Actualizar un almacén por ID
    @PutMapping("/{id}")
    public ResponseEntity<Almacen> actualizarAlmacen(@PathVariable Long id, @RequestBody Almacen almacenActualizado) {
        Optional<Almacen> almacenExistente = almacenRepository.findById(id);
        if (almacenExistente.isPresent()) {
            almacenActualizado.setId(id);
            Almacen almacenGuardado = almacenRepository.save(almacenActualizado);
            return ResponseEntity.ok(almacenGuardado);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Eliminar un almacén por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAlmacen(@PathVariable Long id) {
        if (almacenRepository.existsById(id)) {
            almacenRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
