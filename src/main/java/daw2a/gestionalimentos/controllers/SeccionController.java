package daw2a.gestionalimentos.controllers;
import daw2a.gestionalimentos.entities.Seccion;
import daw2a.gestionalimentos.services.SeccionService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/secciones")
public class SeccionController {

    private final SeccionService seccionService;

    public SeccionController(SeccionService seccionService) {
        this.seccionService = seccionService;
    }

    // Obtener todas las secciones
    @GetMapping
    public ResponseEntity<Page<Seccion>> obtenerSecciones(@RequestParam int page, @RequestParam int pageSize) {
        Page<Seccion> secciones = seccionService.obtenerSecciones(page, pageSize);
        return ResponseEntity.ok(secciones);
    }

    // Obtener una sección específica por ID
    @GetMapping("/{id}")
    public ResponseEntity<Seccion> obtenerSeccionPorId(@PathVariable Long id) {
        Optional<Seccion> seccion = seccionService.obtenerSeccionPorId(id);
        return seccion.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear una nueva sección
    @PostMapping
    public ResponseEntity<Seccion> crearSeccion(@RequestBody Seccion seccion) {
        Seccion nuevaSeccion = seccionService.crearSeccion(seccion);
        return ResponseEntity.ok(nuevaSeccion);
    }

    // Actualizar una sección por ID
    @PutMapping("/{id}")
    public ResponseEntity<Seccion> actualizarSeccion(
            @PathVariable Long id,
            @RequestBody Seccion seccion) {
        Optional<Seccion> seccionActualizada = seccionService.actualizarSeccion(id, seccion);
        return seccionActualizada.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Eliminar una sección por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSeccion(@PathVariable Long id) {
        boolean eliminada = seccionService.eliminarSeccion(id);
        return eliminada ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
