package daw2a.gestionalimentos.controllers;

import daw2a.gestionalimentos.dtos.AlimentoDTO;
import daw2a.gestionalimentos.dtos.RecipienteDTO;
import daw2a.gestionalimentos.dtos.SeccionDTO;
import daw2a.gestionalimentos.entities.Alimento;
import daw2a.gestionalimentos.entities.Recipiente;
import daw2a.gestionalimentos.entities.Seccion;
import daw2a.gestionalimentos.services.SeccionService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/secciones")
public class SeccionController {

    private final SeccionService seccionService;

    public SeccionController(SeccionService seccionService) {
        this.seccionService = seccionService;
    }

    // Convertir entidad a DTO
    private SeccionDTO toDTO(Seccion seccion) {
        SeccionDTO dto = new SeccionDTO();
        dto.setId(seccion.getId());
        dto.setNombre(seccion.getNombre());
        // Si la sección tiene una lista de recipientes, convertirla también
        dto.setListaRecipientes(seccion.getListaRecipientes() != null
                ? seccion.getListaRecipientes().stream().map(this::toDTO).collect(Collectors.toList())
                : null);
        return dto;
    }

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
    private Seccion toEntity(SeccionDTO dto) {
        Seccion seccion = new Seccion();
        seccion.setId(dto.getId());
        seccion.setNombre(dto.getNombre());
        // Aquí podrías agregar la lógica para asignar recipientes si es necesario
        return seccion;
    }

    // Obtener todas las secciones
    @GetMapping
    public ResponseEntity<Page<SeccionDTO>> obtenerSecciones(@RequestParam int page, @RequestParam int pageSize) {
        Page<Seccion> secciones = seccionService.obtenerSecciones(page, pageSize);
        Page<SeccionDTO> seccionesDTO = secciones.map(this::toDTO);
        return ResponseEntity.ok(seccionesDTO);
    }

    // Obtener una sección específica por ID
    @GetMapping("/{id}")
    public ResponseEntity<SeccionDTO> obtenerSeccionPorId(@PathVariable Long id) {
        Optional<Seccion> seccion = seccionService.obtenerSeccionPorId(id);
        return seccion.map(value -> ResponseEntity.ok(toDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear una nueva sección
    @PostMapping
    public ResponseEntity<SeccionDTO> crearSeccion(@RequestBody SeccionDTO seccionDTO) {
        Seccion nuevaSeccion = seccionService.crearSeccion(toEntity(seccionDTO));
        return ResponseEntity.ok(toDTO(nuevaSeccion));
    }

    // Actualizar una sección por ID
    @PutMapping("/{id}")
    public ResponseEntity<SeccionDTO> actualizarSeccion(
            @PathVariable Long id,
            @RequestBody SeccionDTO seccionDTO) {
        Optional<Seccion> seccionActualizada = seccionService.actualizarSeccion(id, toEntity(seccionDTO));
        return seccionActualizada.map(value -> ResponseEntity.ok(toDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Eliminar una sección por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSeccion(@PathVariable Long id) {
        boolean eliminada = seccionService.eliminarSeccion(id);
        return eliminada ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
