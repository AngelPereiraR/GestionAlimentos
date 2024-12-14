package daw2a.gestionalimentos.controllers;
import daw2a.gestionalimentos.dtos.MoverAlimentosRequest;
import daw2a.gestionalimentos.entities.Alimento;
import daw2a.gestionalimentos.services.AlimentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/alimentos")
public class AlimentoController {

    private final AlimentoService alimentoService;

    public AlimentoController(AlimentoService alimentoService) {
        this.alimentoService = alimentoService;
    }

    // Obtener alimentos con paginación
    @GetMapping
    public ResponseEntity<Page<Alimento>> obtenerAlimentos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Alimento> alimentos = alimentoService.obtenerAlimentos(page, size);
        return ResponseEntity.ok(alimentos);
    }

    // Obtener un alimento específico por ID
    @GetMapping("/{id}")
    public ResponseEntity<Alimento> obtenerAlimentoPorId(@PathVariable Long id) {
        return alimentoService.obtenerAlimentoPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear un nuevo alimento
    @PostMapping
    public ResponseEntity<Alimento> crearAlimento(@RequestBody Alimento alimento) {
        Alimento nuevoAlimento = alimentoService.crearAlimento(alimento);
        return ResponseEntity.ok(nuevoAlimento);
    }

    // Actualizar un alimento por ID
    @PutMapping("/{id}")
    public ResponseEntity<Alimento> actualizarAlimento(
            @PathVariable Long id,
            @RequestBody Alimento alimentoActualizado) {
        return alimentoService.actualizarAlimento(id, alimentoActualizado)
                .map(ResponseEntity::ok)
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
    public ResponseEntity<List<Alimento>> controlarAlimentosMasUsados(
            @RequestParam(defaultValue = "10") int topN) {
        List<Alimento> alimentosMasUsados = alimentoService.controlarAlimentosMasUsados(topN);
        return ResponseEntity.ok(alimentosMasUsados);
    }

    // Mover alimentos al congelador
    @PostMapping("/mover-a-congelador")
    public ResponseEntity<List<Alimento>> moverAlimentosACongelador(@RequestBody MoverAlimentosRequest request) {
        List<Alimento> alimentosMovidos = alimentoService.moverAlimentosACongelador(request.getFechaLimite(), request.getEstado());
        return ResponseEntity.ok(alimentosMovidos);
    }


    // Rotar los productos
    @PostMapping("/rotar-productos")
    public ResponseEntity<Void> rotarProductos() {
        alimentoService.rotarProductos();
        return ResponseEntity.noContent().build();
    }
}
