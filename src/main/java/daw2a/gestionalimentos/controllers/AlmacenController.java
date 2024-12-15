package daw2a.gestionalimentos.controllers;

import daw2a.gestionalimentos.dtos.AlimentoDTO;
import daw2a.gestionalimentos.dtos.AlmacenDTO;
import daw2a.gestionalimentos.dtos.RecipienteDTO;
import daw2a.gestionalimentos.dtos.SeccionDTO;
import daw2a.gestionalimentos.entities.Alimento;
import daw2a.gestionalimentos.entities.Almacen;
import daw2a.gestionalimentos.entities.Recipiente;
import daw2a.gestionalimentos.entities.Seccion;
import daw2a.gestionalimentos.repositories.AlmacenRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/almacenes")
public class AlmacenController {

    private final AlmacenRepository almacenRepository;

    public AlmacenController(AlmacenRepository almacenRepository) {
        this.almacenRepository = almacenRepository;
    }

    // Convertir entidad a DTO
    private AlmacenDTO toDTO(Almacen almacen) {
        AlmacenDTO dto = new AlmacenDTO();
        dto.setId(almacen.getId());
        dto.setNombre(almacen.getNombre());
        dto.setTemp(almacen.getTemp());
        dto.setListSeccion(almacen.getListSeccion() != null
                ? almacen.getListSeccion().stream().map(this::toDTO).collect(Collectors.toList())
                : null);
        return dto;
    }

    private SeccionDTO toDTO(Seccion seccion) {
        SeccionDTO dto = new SeccionDTO();
        dto.setId(seccion.getId());
        dto.setNombre(seccion.getNombre());
        dto.setIdAlmacen(seccion.getId());
        dto.setLimite(seccion.getLimite());
        dto.setAccesibilidad(seccion.getAccesibilidad());
        dto.setListaRecipientes(seccion.getListaRecipientes() != null ? seccion.getListaRecipientes().stream().map(this::toDTO).collect(Collectors.toList()) : null);
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
        dto.setCategoria(alimento.getCategoria().toString());
        dto.setEstado(alimento.getEstado().toString());
        return dto;
    }

    // Convertir DTO a entidad
    private Almacen toEntity(AlmacenDTO dto) {
        Almacen almacen = new Almacen();
        almacen.setId(dto.getId());
        almacen.setNombre(dto.getNombre());
        almacen.setTemp(dto.getTemp());
        if (dto.getListSeccion() != null) {
            List<Seccion> secciones = dto.getListSeccion().stream()
                    .map(this::toEntity)
                    .collect(Collectors.toList());
            almacen.setListSeccion(secciones);
        }
        return almacen;
    }

    private Seccion toEntity(SeccionDTO dto) {
        Seccion seccion = new Seccion();
        seccion.setId(dto.getId());
        seccion.setNombre(dto.getNombre());
        return seccion;
    }

    // Obtener todos los almacenes con paginación
    @GetMapping
    public ResponseEntity<Page<AlmacenDTO>> obtenerAlmacenes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Almacen> almacenes = almacenRepository.findAll(PageRequest.of(page, size));
        Page<AlmacenDTO> almacenesDTO = almacenes.map(this::toDTO);
        return ResponseEntity.ok(almacenesDTO);
    }

    // Obtener un almacén por ID
    @GetMapping("/{id}")
    public ResponseEntity<AlmacenDTO> obtenerAlmacenPorId(@PathVariable Long id) {
        Optional<Almacen> almacen = almacenRepository.findById(id);
        return almacen.map(value -> ResponseEntity.ok(toDTO(value)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear un nuevo almacén
    @PostMapping
    public ResponseEntity<AlmacenDTO> guardarAlmacen(@RequestBody AlmacenDTO almacenDTO) {
        Almacen nuevoAlmacen = almacenRepository.save(toEntity(almacenDTO));
        return ResponseEntity.status(201).body(toDTO(nuevoAlmacen));
    }

    // Actualizar un almacén por ID
    @PutMapping("/{id}")
    public ResponseEntity<AlmacenDTO> actualizarAlmacen(@PathVariable Long id, @RequestBody AlmacenDTO almacenDTO) {
        Optional<Almacen> almacenExistente = almacenRepository.findById(id);
        if (almacenExistente.isPresent()) {
            Almacen almacenActualizado = toEntity(almacenDTO);
            almacenActualizado.setId(id);
            Almacen almacenGuardado = almacenRepository.save(almacenActualizado);
            return ResponseEntity.ok(toDTO(almacenGuardado));
        }
        return ResponseEntity.notFound().build();
    }

    // Eliminar un almacén por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAlmacen(@PathVariable Long id) {
        if (almacenRepository.existsById(id)) {
            almacenRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
