package daw2a.gestionalimentos.controllers;
import daw2a.gestionalimentos.entities.Recipiente;
import daw2a.gestionalimentos.services.RecipienteService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/recipientes")
public class RecipienteController {

    private final RecipienteService recipienteService;

    public RecipienteController(RecipienteService recipienteService) {
        this.recipienteService = recipienteService;
    }

    // Obtener todos los recipientes
    @GetMapping
    public ResponseEntity<Page<Recipiente>> obtenerRecipientes(@RequestParam int page, @RequestParam int pageSize) {
        Page<Recipiente> recipientes = recipienteService.obtenerRecipientes(page, pageSize);
        return ResponseEntity.ok(recipientes);
    }

    // Obtener un recipiente específico por ID
    @GetMapping("/{id}")
    public ResponseEntity<Recipiente> obtenerRecipientePorId(@PathVariable Long id) {
        Optional<Recipiente> recipiente = recipienteService.obtenerRecipientePorId(id);
        return recipiente.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear un nuevo recipiente
    @PostMapping
    public ResponseEntity<Recipiente> crearRecipiente(@RequestBody Recipiente recipiente) {
        Recipiente nuevoRecipiente = recipienteService.crearRecipiente(recipiente);
        return ResponseEntity.ok(nuevoRecipiente);
    }

    // Actualizar un recipiente por ID
    @PutMapping("/{id}")
    public ResponseEntity<Recipiente> actualizarRecipiente(
            @PathVariable Long id,
            @RequestBody Recipiente recipiente) {
        Optional<Recipiente> recipienteActualizado = recipienteService.actualizarRecipiente(id, recipiente);
        return recipienteActualizado.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Eliminar un recipiente por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRecipiente(@PathVariable Long id) {
        boolean eliminado = recipienteService.eliminarRecipiente(id);
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
