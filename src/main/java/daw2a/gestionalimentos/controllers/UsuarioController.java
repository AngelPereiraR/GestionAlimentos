package daw2a.gestionalimentos.controllers;

import daw2a.gestionalimentos.dtos.usuario.CrearUsuarioDTO;
import daw2a.gestionalimentos.dtos.usuario.ModificarUsuarioDTO;
import daw2a.gestionalimentos.dtos.usuario.UsuarioDetalleDTO;
import daw2a.gestionalimentos.dtos.usuario.UsuarioListadoDTO;
import daw2a.gestionalimentos.entities.Usuario;
import daw2a.gestionalimentos.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService, PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Listar usuarios", description = "Lista todos los usuarios con soporte para paginación y filtrado opcional por nombre.")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<UsuarioListadoDTO>> listarUsuarios(@RequestParam(required = false) String nombre,
                                                                  Pageable pageable) {
        Page<UsuarioListadoDTO> usuarios = usuarioService.listarUsuarios(nombre, pageable);
        return ResponseEntity.ok(usuarios);
    }

    @Operation(summary = "Mostrar usuarios en vista", description = "Vista que muestra los usuarios en el frontend.")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/vista")
    public String listarUsuariosVista(Model model,
                                      @RequestParam(required = false) String nombre,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size) {
        Page<UsuarioListadoDTO> usuariosPage = usuarioService.listarUsuarios(nombre, PageRequest.of(page, size));
        model.addAttribute("usuarios", usuariosPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", usuariosPage.getTotalPages());
        model.addAttribute("nombreFiltro", nombre);

        int previousPage = (page > 0) ? page - 1 : 0;
        int nextPage = (page < usuariosPage.getTotalPages() - 1) ? page + 1 : page;
        model.addAttribute("previousPage", previousPage);
        model.addAttribute("nextPage", nextPage);

        return "usuarios";
    }

    @Operation(summary = "Obtener detalles de un usuario", description = "Obtiene los detalles de un usuario específico por su ID.")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDetalleDTO> obtenerUsuario(@PathVariable Long id) {
        UsuarioDetalleDTO usuario = usuarioService.obtenerUsuario(id);
        return ResponseEntity.ok(usuario);
    }

    @Operation(summary = "Obtener detalles de un usuario por email", description = "Obtiene los detalles de un usuario específico por su email.")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/email")
    public ResponseEntity<UsuarioDetalleDTO> obtenerUsuarioPorEmail(@RequestParam("email") String email) {
        UsuarioDetalleDTO usuario = usuarioService.obtenerUsuarioByEmail(email);
        return ResponseEntity.ok(usuario);
    }

    @Operation(summary = "Crear nuevo usuario", description = "Crea un nuevo usuario a partir de un DTO.")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UsuarioDetalleDTO> crearUsuario(@RequestBody @Valid CrearUsuarioDTO crearUsuarioDTO) {
        UsuarioDetalleDTO nuevoUsuario = usuarioService.crearUsuario(crearUsuarioDTO);
        return ResponseEntity.ok(nuevoUsuario);
    }

    @Operation(summary = "Obtener detalles del usuario autenticado", description = "Obtiene los detalles del usuario actualmente autenticado.")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<Usuario> obtenerMiPerfil() {
        Usuario usuario = usuarioService.obtenerMiPerfil();
        return ResponseEntity.ok(usuario);
    }

    @Operation(summary = "Actualizar usuario", description = "Actualiza los detalles de un usuario específico por su ID.")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDetalleDTO> actualizarUsuario(@PathVariable Long id,
                                                               @RequestBody ModificarUsuarioDTO modificarUsuarioDTO) {
        UsuarioDetalleDTO usuario = usuarioService.actualizarUsuario(id, modificarUsuarioDTO);
        return ResponseEntity.ok(usuario);
    }

    @Operation(summary = "Cambiar rol de un usuario", description = "Permite cambiar el rol de un usuario.")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/cambiar-rol")
    public ResponseEntity<UsuarioDetalleDTO> cambiarRol(@PathVariable Long id, @RequestParam String nuevoRol) {
        UsuarioDetalleDTO usuarioActualizado = usuarioService.cambiarRol(id, nuevoRol);
        return ResponseEntity.ok(usuarioActualizado);
    }

    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario por su ID.")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
