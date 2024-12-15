package daw2a.gestionalimentos.controllers;

import daw2a.gestionalimentos.dtos.AlimentoDTO;
import daw2a.gestionalimentos.entities.Alimento;
import daw2a.gestionalimentos.services.AlimentoService;
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
        return alimento;
    }

    // Obtener alimentos con paginación
    @GetMapping
    public ResponseEntity<List<AlimentoDTO>> obtenerAlimentos(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size) {
        List<AlimentoDTO> alimentosDTO = alimentoService.obtenerAlimentos(page, size)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(alimentosDTO);
    }

    // Obtener disponibilidad por ubicación
    @GetMapping("/disponibilidad")
    public ResponseEntity<Map<String, Long>> obtenerDisponibilidadPorUbicacion() {
        Map<String, Long> disponibilidad = alimentoService.obtenerDisponibilidadPorUbicacion();
        return ResponseEntity.ok(disponibilidad);
    }

    // Obtener alimentos próximos a caducar por ubicación
    @GetMapping("/proximos-caducar")
    public ResponseEntity<Map<String, List<AlimentoDTO>>> obtenerProximosACaducarPorUbicacion(
            @RequestParam(defaultValue = "7") int diasAviso) {
        LocalDate fechaAviso = LocalDate.now().plusDays(diasAviso);
        Map<String, List<Alimento>> proximosACaducar = alimentoService.obtenerProximosACaducarPorUbicacion(fechaAviso);

        // Convertir entidades a DTOs
        Map<String, List<AlimentoDTO>> proximosACaducarDTO = proximosACaducar.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream().map(this::toDTO).collect(Collectors.toList())
                ));

        return ResponseEntity.ok(proximosACaducarDTO);
    }

    // Obtener alimentos más usados
    @GetMapping("/mas-usados")
    public ResponseEntity<List<AlimentoDTO>> obtenerAlimentosMasUsados(@RequestParam(defaultValue = "5") int topN) {
        List<AlimentoDTO> alimentosMasUsados = alimentoService.controlarAlimentosMasUsados(topN)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(alimentosMasUsados);
    }

    // Obtener un alimento por ID
    @GetMapping("/{id}")
    public ResponseEntity<AlimentoDTO> obtenerAlimentoPorId(@PathVariable Long id) {
        return alimentoService.obtenerAlimentoPorId(id)
                .map(alimento -> ResponseEntity.ok(toDTO(alimento)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear un nuevo alimento
    @PostMapping
    public ResponseEntity<AlimentoDTO> crearAlimento(@RequestBody AlimentoDTO alimentoDTO) {
        Alimento nuevoAlimento = alimentoService.crearAlimento(toEntity(alimentoDTO));
        return ResponseEntity.ok(toDTO(nuevoAlimento));
    }

    // Actualizar un alimento por ID
    @PutMapping("/{id}")
    public ResponseEntity<AlimentoDTO> actualizarAlimento(@PathVariable Long id, @RequestBody AlimentoDTO alimentoDTO) {
        return alimentoService.actualizarAlimento(id, toEntity(alimentoDTO))
                .map(alimento -> ResponseEntity.ok(toDTO(alimento)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Eliminar un alimento por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAlimento(@PathVariable Long id) {
        if (alimentoService.eliminarAlimento(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
