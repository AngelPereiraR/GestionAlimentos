package daw2a.gestionalimentos.controllers;

import daw2a.gestionalimentos.dtos.RecipienteDTO;
import daw2a.gestionalimentos.dtos.AlimentoDTO;
import daw2a.gestionalimentos.entities.Recipiente;
import daw2a.gestionalimentos.entities.Alimento;
import daw2a.gestionalimentos.services.RecipienteService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/recipientes")
public class RecipienteController {

    private final RecipienteService recipienteService;

    public RecipienteController(RecipienteService recipienteService) {
        this.recipienteService = recipienteService;
    }

    // Convertir entidad a DTO
    private RecipienteDTO toDTO(Recipiente recipiente) {
        RecipienteDTO dto = new RecipienteDTO();
        dto.setId(recipiente.getId());
        dto.setTamanyo(recipiente.getTamanyo());
        dto.setIdSeccion(recipiente.getSeccion() != null ? recipiente.getSeccion().getId() : null);
        dto.setListAlimentos(recipiente.getListAlimentos() != null
                ? recipiente.getListAlimentos().stream().map(this::toDTO).collect(Collectors.toList())
                : null);
        return dto;
    }

    private AlimentoDTO toDTO(Alimento alimento) {
        AlimentoDTO dto = new AlimentoDTO();
        dto.setId(alimento.getId());
        dto.setNombre(alimento.getNombre());
        dto.setPerecedero(alimento.getPerecedero());
        dto.setAbierto(alimento.getAbierto());
        dto.setTamano(alimento.getTamano());
        dto.setFechaCaducidad(alimento.getFechaCaducidad());
        dto.setCategoria(String.valueOf(alimento.getCategoria()));
        dto.setEstado(String.valueOf(alimento.getEstado()));
        return dto;
    }

    // Convertir DTO a entidad
    private Recipiente toEntity(RecipienteDTO dto) {
        Recipiente recipiente = new Recipiente();
        recipiente.setId(dto.getId());
        recipiente.setTamanyo(dto.getTamanyo());
        // La asignación de la sección y los alimentos dependerá de las relaciones específicas
        // Esto puede requerir obtener las entidades relacionadas desde los servicios o repositorios.
        return recipiente;
    }

    // Obtener todos los recipientes
    @GetMapping
    public ResponseEntity<Page<RecipienteDTO>> obtenerRecipientes(@RequestParam int page, @RequestParam int pageSize) {
        Page<Recipiente> recipientes = recipienteService.obtenerRecipientes(page, pageSize);
        Page<RecipienteDTO> recipientesDTO = recipientes.map(this::toDTO);
        return ResponseEntity.ok(recipientesDTO);
    }

    // Obtener un recipiente específico por ID
    @GetMapping("/{id}")
    public ResponseEntity<RecipienteDTO> obtenerRecipientePorId(@PathVariable Long id) {
        Optional<Recipiente> recipiente = recipienteService.obtenerRecipientePorId(id);
        return recipiente.map(value -> ResponseEntity.ok(toDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear un nuevo recipiente
    @PostMapping
    public ResponseEntity<RecipienteDTO> crearRecipiente(@RequestBody RecipienteDTO recipienteDTO) {
        Recipiente nuevoRecipiente = recipienteService.crearRecipiente(toEntity(recipienteDTO));
        return ResponseEntity.ok(toDTO(nuevoRecipiente));
    }

    // Actualizar un recipiente por ID
    @PutMapping("/{id}")
    public ResponseEntity<RecipienteDTO> actualizarRecipiente(
            @PathVariable Long id,
            @RequestBody RecipienteDTO recipienteDTO) {
        Optional<Recipiente> recipienteActualizado = recipienteService.actualizarRecipiente(id, toEntity(recipienteDTO));
        return recipienteActualizado.map(value -> ResponseEntity.ok(toDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Eliminar un recipiente por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRecipiente(@PathVariable Long id) {
        boolean eliminado = recipienteService.eliminarRecipiente(id);
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
