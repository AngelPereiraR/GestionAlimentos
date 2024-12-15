package daw2a.gestionalimentos.security.controller;

import daw2a.gestionalimentos.dtos.usuario.CrearUsuarioDTO;
import daw2a.gestionalimentos.entities.Usuario;
import daw2a.gestionalimentos.security.dto.AuthResponse;
import daw2a.gestionalimentos.security.dto.LoginUsuarioDTO;
import daw2a.gestionalimentos.security.jwt.JwtUtil;
import daw2a.gestionalimentos.security.user.CustomUserDetails;
import daw2a.gestionalimentos.services.TokenBlacklistService;
import daw2a.gestionalimentos.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsuarioService usuarioService;
    private final TokenBlacklistService tokenBlacklistService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UsuarioService usuarioService, TokenBlacklistService tokenBlacklistService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.usuarioService = usuarioService;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @Operation(summary = "Autenticar usuario", description = "Autentica al usuario y genera un token JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token generado correctamente"),
            @ApiResponse(responseCode = "400", description = "Credenciales incorrectas")
    })
    @PostMapping("/authenticate")
    public AuthResponse authenticate(@RequestBody LoginUsuarioDTO request, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        String token = jwtUtil.generateToken((CustomUserDetails) authentication.getPrincipal());

        // Crear la cookie
        Cookie jwtCookie = new Cookie("jwt", token);
        jwtCookie.setHttpOnly(true); // Solo accesible desde el servidor
        jwtCookie.setSecure(true); // Solo en conexiones HTTPS
        jwtCookie.setPath("/"); // Disponible para toda la aplicación
        jwtCookie.setMaxAge(60 * 60 * 10); // Validez de 10 horas

        // Agregar la cookie a la respuesta
        response.addCookie(jwtCookie);
        return new AuthResponse(token);
    }

    @Operation(summary = "Cerrar sesión", description = "Cierra sesión del usuario, añadiendo el token a la blacklist y eliminando la cookie.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logout exitoso"),
            @ApiResponse(responseCode = "400", description = "No se encontró un token válido para cerrar sesión")
    })
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        String jwt = jwtUtil.obtenerTokenDeRequest(request);

        if (jwt != null && !jwt.isEmpty()) {
            tokenBlacklistService.addTokenToBlacklist(jwt);

            // Invalidar cookie JWT
            Cookie jwtCookie = new Cookie("jwt", null);
            jwtCookie.setPath("/");
            jwtCookie.setHttpOnly(true);
            jwtCookie.setMaxAge(0); // Eliminar inmediatamente
            response.addCookie(jwtCookie);

            return ResponseEntity.ok("Logout exitoso. Token añadido a la blacklist y cookie eliminada.");
        }

        return ResponseEntity.badRequest().body("No se encontró un token válido para cerrar sesión.");
    }

    @Operation(summary = "Registrar nuevo usuario", description = "Registra un nuevo usuario y genera un token JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de registro inválidos")
    })
    @PostMapping("/register")
    public AuthResponse register(@RequestBody @Valid CrearUsuarioDTO crearUsuarioDTO) {
        // Registrar usuario usando el servicio
        Usuario nuevoUsuario = usuarioService.registrarUsuario(crearUsuarioDTO);

        // Generar token JWT para el nuevo usuario
        String token = jwtUtil.generateToken(new CustomUserDetails(nuevoUsuario));

        // Devolver el token en la respuesta
        return new AuthResponse(token);
    }
}
