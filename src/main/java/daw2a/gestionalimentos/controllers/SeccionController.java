package daw2a.gestionalimentos.controllers;

import daw2a.gestionalimentos.dtos.AlimentoDTO;
import daw2a.gestionalimentos.dtos.RecipienteDTO;
import daw2a.gestionalimentos.dtos.SeccionDTO;
import daw2a.gestionalimentos.entities.Alimento;
import daw2a.gestionalimentos.entities.Almacen;
import daw2a.gestionalimentos.entities.Recipiente;
import daw2a.gestionalimentos.entities.Seccion;
import daw2a.gestionalimentos.enums.CategoriaSelect;
import daw2a.gestionalimentos.enums.EstadoSelect;
import daw2a.gestionalimentos.services.AlmacenService;
import daw2a.gestionalimentos.services.SeccionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/secciones")
public class SeccionController {

    private final SeccionService seccionService;
    private final AlmacenService almacenService;

    public SeccionController(SeccionService seccionService, AlmacenService almacenService) {
        this.seccionService = seccionService;
        this.almacenService = almacenService;
    }

    // Convertir entidad a DTO
    private SeccionDTO toDTO(Seccion seccion) {
        SeccionDTO dto = new SeccionDTO();
        dto.setId(seccion.getId());
        dto.setNombre(seccion.getNombre());
        dto.setAccesibilidad(seccion.getAccesibilidad());
        dto.setIdAlmacen(seccion.getAlmacen().getId());
        dto.setLimite(seccion.getLimite());
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
        seccion.setAccesibilidad(dto.getAccesibilidad());
        seccion.setLimite(dto.getLimite());
        // Asignar la sección (puedes obtenerla desde un servicio si es necesario)
        if (dto.getIdAlmacen() != null) {
            // Obtén la entidad Seccion mediante su id
            Optional<Almacen> almacen = almacenService.obtenerAlmacenPorId(dto.getIdAlmacen());
            Almacen newAlmacen = new Almacen();
            newAlmacen.setId(almacen.get().getId());
            newAlmacen.setNombre(almacen.get().getNombre());
            newAlmacen.setTemp(almacen.get().getTemp());
            newAlmacen.setListSeccion(almacen.get().getListSeccion());
            seccion.setAlmacen(newAlmacen);
        }

        if (dto.getListaRecipientes() != null) {
            List<Recipiente> recipientes = dto.getListaRecipientes().stream()
                    .map(this::toEntity)
                    .collect(Collectors.toList());
            seccion.setListaRecipientes(recipientes);
        }
        return seccion;
    }
    private Recipiente toEntity(RecipienteDTO dto) {
        Recipiente recipiente = new Recipiente();
        recipiente.setId(dto.getId());
        recipiente.setTamanyo(dto.getTamanyo());

        // Asignar la sección (puedes obtenerla desde un servicio si es necesario)
        if (dto.getIdSeccion() != null) {
            // Obtén la entidad Seccion mediante su id
            Optional<Seccion> seccion = seccionService.obtenerSeccionPorId(dto.getIdSeccion());
            Seccion newSeccion = new Seccion();
            newSeccion.setId(seccion.get().getId());
            newSeccion.setNombre(seccion.get().getNombre());
            newSeccion.setAccesibilidad(seccion.get().getAccesibilidad());
            newSeccion.setLimite(seccion.get().getLimite());
            newSeccion.setAlmacen(seccion.get().getAlmacen());
            newSeccion.setListaRecipientes(seccion.get().getListaRecipientes());
            recipiente.setSeccion(newSeccion);
        }

        // Asignar los alimentos
        if (dto.getListAlimentos() != null) {
            List<Alimento> alimentos = dto.getListAlimentos().stream().map(alimentoDTO -> {
                Alimento alimento = new Alimento();
                alimento.setId(alimentoDTO.getId());
                alimento.setNombre(alimentoDTO.getNombre());
                alimento.setPerecedero(alimentoDTO.getPerecedero());
                alimento.setAbierto(alimentoDTO.getAbierto());
                alimento.setTamano(alimentoDTO.getTamano());
                alimento.setFechaCaducidad(alimentoDTO.getFechaCaducidad());
                alimento.setCategoria(CategoriaSelect.valueOf(alimentoDTO.getCategoria()));
                alimento.setEstado(EstadoSelect.valueOf(alimentoDTO.getEstado()));
                return alimento;
            }).collect(Collectors.toList());

            recipiente.setListAlimentos(alimentos);
        }

        return recipiente;
    }

    @Operation(summary = "Obtener todas las secciones", description = "Obtiene una lista de todas las secciones con paginación.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Secciones obtenidas correctamente"),
            @ApiResponse(responseCode = "500", description = "Error al obtener las secciones")
    })
    @GetMapping
    public ResponseEntity<Page<SeccionDTO>> obtenerSecciones(@RequestParam int page, @RequestParam int pageSize) {
        Page<Seccion> secciones = seccionService.obtenerSecciones(page, pageSize);
        Page<SeccionDTO> seccionesDTO = secciones.map(this::toDTO);
        return ResponseEntity.ok(seccionesDTO);
    }

    @Operation(summary = "Obtener una sección específica por ID", description = "Obtiene una sección específica dado su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sección encontrada"),
            @ApiResponse(responseCode = "404", description = "Sección no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SeccionDTO> obtenerSeccionPorId(@PathVariable Long id) {
        Optional<Seccion> seccion = seccionService.obtenerSeccionPorId(id);
        return seccion.map(value -> ResponseEntity.ok(toDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear una nueva sección", description = "Crea una nueva sección y la guarda en la base de datos.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Sección creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta")
    })
    @PostMapping
    public ResponseEntity<SeccionDTO> crearSeccion(@RequestBody SeccionDTO seccionDTO) {
        Seccion nuevaSeccion = seccionService.crearSeccion(toEntity(seccionDTO));
        return ResponseEntity.ok(toDTO(nuevaSeccion));
    }

    @Operation(summary = "Actualizar una sección por ID", description = "Actualiza una sección existente por su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sección actualizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Sección no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<SeccionDTO> actualizarSeccion(
            @PathVariable Long id,
            @RequestBody SeccionDTO seccionDTO) {
        Optional<Seccion> seccionActualizada = seccionService.actualizarSeccion(id, toEntity(seccionDTO));
        return seccionActualizada.map(value -> ResponseEntity.ok(toDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar una sección por ID", description = "Elimina una sección dada su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Sección eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Sección no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSeccion(@PathVariable Long id) {
        boolean eliminada = seccionService.eliminarSeccion(id);
        return eliminada ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
