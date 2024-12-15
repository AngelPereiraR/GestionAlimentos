package daw2a.gestionalimentos.controllers;

import daw2a.gestionalimentos.dtos.AlimentoDTO;
import daw2a.gestionalimentos.entities.Alimento;
import daw2a.gestionalimentos.enums.CategoriaSelect;
import daw2a.gestionalimentos.enums.EstadoSelect;
import daw2a.gestionalimentos.services.AlimentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/alimentos")
public class AlimentoController {

    private final AlimentoService alimentoService;

    public AlimentoController(AlimentoService alimentoService) {
        this.alimentoService = alimentoService;
    }

    // Convertir entidad a DTO
    private AlimentoDTO toDTO(Alimento alimento) {
        AlimentoDTO dto = new AlimentoDTO();
        dto.setId(alimento.getId());
        dto.setNombre(alimento.getNombre());
        dto.setPerecedero(alimento.getPerecedero());
        dto.setAbierto(alimento.getAbierto());
        dto.setTamano(alimento.getTamano());
        dto.setFechaCaducidad(alimento.getFechaCaducidad());
        dto.setCategoria(alimento.getCategoria().toString());
        dto.setEstado(alimento.getEstado().toString());
        return dto;
    }

    // Convertir DTO a entidad
    private Alimento toEntity(AlimentoDTO dto) {
        Alimento alimento = new Alimento();
        alimento.setId(dto.getId());
        alimento.setNombre(dto.getNombre());
        alimento.setPerecedero(dto.getPerecedero());
        alimento.setAbierto(dto.getAbierto());
        alimento.setTamano(dto.getTamano());
        alimento.setFechaCaducidad(dto.getFechaCaducidad());
        alimento.setCategoria(CategoriaSelect.valueOf(dto.getCategoria()));
        alimento.setEstado(EstadoSelect.valueOf(dto.getEstado()));
        return alimento;
    }

    /**
     * Obtener todos los alimentos con paginación.
     *
     * @param page Número de página (por defecto 0).
     * @param size Número de elementos por página (por defecto 10).
     * @return Lista de alimentos en formato DTO.
     */
    @GetMapping
    @Operation(summary = "Obtener alimentos", description = "Obtiene todos los alimentos con paginación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de alimentos obtenida exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    public ResponseEntity<List<AlimentoDTO>> obtenerAlimentos(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size) {
        List<AlimentoDTO> alimentosDTO = alimentoService.obtenerAlimentos(page, size)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(alimentosDTO);
    }

    /**
     * Obtener la disponibilidad de alimentos por ubicación.
     *
     * @return Mapa con la cantidad de alimentos por ubicación.
     */
    @GetMapping("/disponibilidad")
    @Operation(summary = "Obtener disponibilidad por ubicación", description = "Obtiene la disponibilidad de alimentos agrupados por ubicación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Disponibilidad de alimentos obtenida exitosamente")
    })
    public ResponseEntity<Map<Long, Long>> obtenerDisponibilidadPorUbicacion() {
        Map<Long, Long> disponibilidad = alimentoService.obtenerDisponibilidadPorUbicacion();
        return ResponseEntity.ok(disponibilidad);
    }

    /**
     * Obtener los alimentos próximos a caducar por ubicación.
     *
     * @param diasAviso Número de días antes de la caducidad para mostrar los alimentos (por defecto 7).
     * @return Mapa de alimentos próximos a caducar por ubicación.
     */
    @GetMapping("/proximos-caducar")
    @Operation(summary = "Obtener alimentos próximos a caducar por ubicación", description = "Obtiene los alimentos que caducan en los próximos días, agrupados por ubicación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alimentos próximos a caducar obtenidos exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    public ResponseEntity<Map<Long, List<AlimentoDTO>>> obtenerProximosACaducarPorUbicacion(
            @RequestParam(defaultValue = "7") int diasAviso) {
        LocalDate fechaAviso = LocalDate.now().plusDays(diasAviso);
        Map<Long, List<Alimento>> proximosACaducar = alimentoService.obtenerProximosACaducarPorUbicacion(fechaAviso);

        // Convertir entidades a DTOs
        Map<Long, List<AlimentoDTO>> proximosACaducarDTO = proximosACaducar.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream().map(this::toDTO).collect(Collectors.toList())
                ));

        return ResponseEntity.ok(proximosACaducarDTO);
    }

    /**
     * Obtener los alimentos más usados.
     *
     * @param topN Número de alimentos más usados a retornar (por defecto 5).
     * @return Lista de alimentos más usados.
     */
    @GetMapping("/mas-usados")
    @Operation(summary = "Obtener alimentos más usados", description = "Obtiene los alimentos más usados en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alimentos más usados obtenidos exitosamente")
    })
    public ResponseEntity<List<AlimentoDTO>> obtenerAlimentosMasUsados(@RequestParam(defaultValue = "5") int topN) {
        List<AlimentoDTO> alimentosMasUsados = alimentoService.controlarAlimentosMasUsados(topN)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(alimentosMasUsados);
    }

    /**
     * Obtener un alimento por su ID.
     *
     * @param id ID del alimento a buscar.
     * @return Alimento encontrado en formato DTO.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener alimento por ID", description = "Obtiene los detalles de un alimento específico por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alimento encontrado"),
            @ApiResponse(responseCode = "404", description = "Alimento no encontrado")
    })
    public ResponseEntity<AlimentoDTO> obtenerAlimentoPorId(@PathVariable Long id) {
        return alimentoService.obtenerAlimentoPorId(id)
                .map(alimento -> ResponseEntity.ok(toDTO(alimento)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crear un nuevo alimento.
     *
     * @param alimentoDTO Datos del alimento a crear.
     * @return Alimento creado en formato DTO.
     */
    @PostMapping
    @Operation(summary = "Crear un nuevo alimento", description = "Crea un nuevo alimento en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alimento creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<AlimentoDTO> crearAlimento(@RequestBody AlimentoDTO alimentoDTO) {
        Alimento nuevoAlimento = alimentoService.crearAlimento(toEntity(alimentoDTO));
        return ResponseEntity.ok(toDTO(nuevoAlimento));
    }

    /**
     * Actualizar un alimento por su ID.
     *
     * @param id ID del alimento a actualizar.
     * @param alimentoDTO Datos del alimento a actualizar.
     * @return Alimento actualizado en formato DTO.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un alimento", description = "Actualiza los detalles de un alimento existente por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alimento actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Alimento no encontrado")
    })
    public ResponseEntity<AlimentoDTO> actualizarAlimento(@PathVariable Long id, @RequestBody AlimentoDTO alimentoDTO) {
        return alimentoService.actualizarAlimento(id, toEntity(alimentoDTO))
                .map(alimento -> ResponseEntity.ok(toDTO(alimento)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Eliminar un alimento por su ID.
     *
     * @param id ID del alimento a eliminar.
     * @return Respuesta sin contenido si el alimento fue eliminado exitosamente.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un alimento", description = "Elimina un alimento del sistema por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Alimento eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Alimento no encontrado")
    })
    public ResponseEntity<Void> eliminarAlimento(@PathVariable Long id) {
        if (alimentoService.eliminarAlimento(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
