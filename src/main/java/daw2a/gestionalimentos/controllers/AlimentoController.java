package daw2a.gestionalimentos.controllers;

import daw2a.gestionalimentos.dtos.AlimentoDTO;
import daw2a.gestionalimentos.enums.CategoriaSelect;
import daw2a.gestionalimentos.enums.EstadoSelect;
import daw2a.gestionalimentos.services.AlimentoService;
import daw2a.gestionalimentos.entities.Alimento;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import java.util.List;
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
        dto.setCategoria(String.valueOf(alimento.getCategoria()));
        dto.setEstado(String.valueOf(alimento.getEstado()));
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

    // Obtener alimentos con paginación
    @GetMapping
    public ResponseEntity<Page<AlimentoDTO>> obtenerAlimentos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Alimento> alimentos = alimentoService.obtenerAlimentos(page, size);
        Page<AlimentoDTO> alimentosDTO = alimentos.map(this::toDTO);
        return ResponseEntity.ok(alimentosDTO);
    }

    // Obtener un alimento específico por ID
    @GetMapping("/{id}")
    public ResponseEntity<AlimentoDTO> obtenerAlimentoPorId(@PathVariable Long id) {
        return alimentoService.obtenerAlimentoPorId(id)
                .map(alimento -> ResponseEntity.ok(toDTO(alimento)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear un nuevo alimento
    @PostMapping
    public ResponseEntity<AlimentoDTO> crearAlimento(@RequestBody AlimentoDTO alimentoDTO) {
        Alimento nuevoAlimento = alimentoService.crearAlimento(toEntity(alimentoDTO));
        return ResponseEntity.ok(toDTO(nuevoAlimento));
    }

    // Actualizar un alimento por ID
    @PutMapping("/{id}")
    public ResponseEntity<AlimentoDTO> actualizarAlimento(
            @PathVariable Long id,
            @RequestBody AlimentoDTO alimentoDTO) {
        return alimentoService.actualizarAlimento(id, toEntity(alimentoDTO))
                .map(alimento -> ResponseEntity.ok(toDTO(alimento)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Eliminar un alimento por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAlimento(@PathVariable Long id) {
        return alimentoService.eliminarAlimento(id) ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }

    // Obtener los alimentos más usados
    @GetMapping("/mas-usados")
    public ResponseEntity<List<AlimentoDTO>> controlarAlimentosMasUsados(
            @RequestParam(defaultValue = "10") int topN) {
        List<Alimento> alimentosMasUsados = alimentoService.controlarAlimentosMasUsados(topN);
        List<AlimentoDTO> alimentosDTO = alimentosMasUsados.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(alimentosDTO);
    }
}
