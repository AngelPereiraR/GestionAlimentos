package daw2a.gestionalimentos.services;

import daw2a.gestionalimentos.dtos.usuario.CrearUsuarioDTO;
import daw2a.gestionalimentos.dtos.usuario.ModificarUsuarioDTO;
import daw2a.gestionalimentos.dtos.usuario.UsuarioDetalleDTO;
import daw2a.gestionalimentos.dtos.usuario.UsuarioListadoDTO;
import daw2a.gestionalimentos.entities.Usuario;
import daw2a.gestionalimentos.enums.Rol;
import daw2a.gestionalimentos.exceptions.RecursoDuplicadoException;
import daw2a.gestionalimentos.exceptions.RecursoNoEncontradoException;
import daw2a.gestionalimentos.repositories.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Servicio para gestionar operaciones relacionadas con usuarios en la biblioteca.
 * Proporciona funcionalidades de creación, actualización, eliminación y consulta de usuarios.
 */
@Service
public class UsuarioService {
    /** Mensaje de error estándar para usuarios no encontrados. */
    public static final String USUARIO_NO_ENCONTRADO_CON = "Usuario no encontrado con ";

    /** Repositorio para operaciones de persistencia de usuarios. */
    private final UsuarioRepository usuarioRepository;

    /** Codificador de contraseñas para seguridad. */
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor para inyección de dependencias.
     *
     * @param usuarioRepository Repositorio de usuarios
     * @param passwordEncoder Codificador de contraseñas
     */
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Lista usuarios con soporte de paginación y filtrado opcional por nombre.
     *
     * @param nombre Filtro opcional de nombre de usuario
     * @param pageable Configuración de paginación
     * @return Página de usuarios en formato DTO de listado
     */
    public Page<UsuarioListadoDTO> listarUsuarios(String nombre, Pageable pageable) {
        // Buscar usuarios con filtro de nombre o todos los usuarios si no hay filtro
        Page<Usuario> usuarios = (nombre != null)
                ? usuarioRepository.findByNombreContainingIgnoreCase(nombre, pageable)
                : usuarioRepository.findAll(pageable);

        // Convertir entidades de usuario a DTOs de listado
        return usuarios.map(this::convertirAUsuarioListadoDTO);
    }

    /**
     * Obtiene los detalles de un usuario específico por su ID.
     *
     * @param id Identificador único del usuario
     * @return Detalles del usuario en formato DTO
     * @throws RecursoNoEncontradoException Si no se encuentra el usuario
     */
    public UsuarioDetalleDTO obtenerUsuario(Long id) {
        // Buscar usuario por ID o lanzar excepción si no existe
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(USUARIO_NO_ENCONTRADO_CON + "id " + id));
        return convertirAUsuarioDetalleDTO(usuario);
    }

    /**
     * Obtiene los detalles de un usuario por su correo electrónico.
     *
     * @param email Correo electrónico del usuario
     * @return Detalles del usuario en formato DTO
     * @throws RecursoNoEncontradoException Si no se encuentra el usuario
     */
    public UsuarioDetalleDTO obtenerUsuarioByEmail(String email) {
        // Buscar usuario por email o lanzar excepción si no existe
        Usuario usuario = usuarioRepository.findUsuarioByEmail(email)
                .orElseThrow(() -> new RecursoNoEncontradoException(USUARIO_NO_ENCONTRADO_CON + "email " + email));
        return convertirAUsuarioDetalleDTO(usuario);
    }

    /**
     * Obtiene el perfil del usuario actualmente autenticado.
     *
     * @return Entidad de usuario autenticado
     * @throws RecursoNoEncontradoException Si no hay usuario autenticado
     */
    public Usuario obtenerMiPerfil() {
        // Obtener contexto de autenticación actual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Verificar si el usuario está autenticado
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RecursoNoEncontradoException("Usuario no autenticado.");
        }

        // Obtener el email del usuario autenticado
        String email = authentication.getName();

        // Buscar y devolver el usuario por email
        return usuarioRepository.findUsuarioByEmail(email)
                .orElseThrow(() -> new RecursoNoEncontradoException(USUARIO_NO_ENCONTRADO_CON + "email " + email));
    }

    /**
     * Crea un nuevo usuario en el sistema.
     *
     * @param crearUsuarioDTO Datos para crear un nuevo usuario
     * @return Detalles del usuario creado
     * @throws RecursoDuplicadoException Si el email ya está en uso
     */
    public UsuarioDetalleDTO crearUsuario(CrearUsuarioDTO crearUsuarioDTO) {
        // Preparar entidad de usuario
        Usuario usuario = getUsuario(crearUsuarioDTO);

        // Guardar el usuario en la base de datos
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        // Convertir la entidad guardada a DTO
        return convertirAUsuarioDetalleDTO(usuarioGuardado);
    }

    /**
     * Prepara una entidad de usuario a partir de los datos de creación.
     *
     * @param crearUsuarioDTO Datos para crear un nuevo usuario
     * @return Entidad de usuario preparada
     * @throws RecursoDuplicadoException Si el email ya está en uso
     */
    private Usuario getUsuario(CrearUsuarioDTO crearUsuarioDTO) {
        // Verificar si el email ya está registrado
        if (usuarioRepository.findUsuarioByEmail(crearUsuarioDTO.getEmail()).isPresent()) {
            throw new RecursoDuplicadoException("El email " + crearUsuarioDTO.getEmail() + " ya está en uso.");
        }

        // Crear nueva entidad de usuario
        Usuario usuario = new Usuario();
        usuario.setNombre(crearUsuarioDTO.getNombre());
        usuario.setEmail(crearUsuarioDTO.getEmail());
        // Codificar la contraseña por seguridad
        usuario.setPassword(passwordEncoder.encode(crearUsuarioDTO.getPassword()));
        // Convertir rol a enum
        usuario.setRol(Rol.valueOf(crearUsuarioDTO.getRol().toUpperCase()));
        return usuario;
    }

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param crearUsuarioDTO Datos para registrar un nuevo usuario
     * @return Entidad de usuario registrada
     * @throws RecursoDuplicadoException Si el email ya está en uso
     */
    public Usuario registrarUsuario(CrearUsuarioDTO crearUsuarioDTO) {
        // Preparar entidad de usuario
        Usuario usuario = getUsuario(crearUsuarioDTO);

        // Guardar y devolver el usuario
        return usuarioRepository.save(usuario);
    }

    /**
     * Actualiza un usuario existente.
     *
     * @param id Identificador del usuario a actualizar
     * @param modificarUsuarioDTO Datos para modificar el usuario
     * @return Detalles del usuario actualizado
     * @throws RecursoNoEncontradoException Si el usuario no existe
     */
    public UsuarioDetalleDTO actualizarUsuario(Long id, ModificarUsuarioDTO modificarUsuarioDTO) {
        // Buscar el usuario existente
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(USUARIO_NO_ENCONTRADO_CON + "id " + id));

        // Actualizar campos solo si no son nulos
        if (modificarUsuarioDTO.getNombre() != null) {
            usuario.setNombre(modificarUsuarioDTO.getNombre());
        }
        if (modificarUsuarioDTO.getEmail() != null) {
            usuario.setEmail(modificarUsuarioDTO.getEmail());
        }
        if (modificarUsuarioDTO.getPassword() != null) {
            // Codificar la nueva contraseña
            usuario.setPassword(passwordEncoder.encode(modificarUsuarioDTO.getPassword()));
        }
        if (modificarUsuarioDTO.getRol() != null) {
            // Convertir rol a enum
            usuario.setRol(Rol.valueOf(modificarUsuarioDTO.getRol().toUpperCase()));
        }

        // Guardar cambios y devolver los detalles actualizados
        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        return convertirAUsuarioDetalleDTO(usuarioActualizado);
    }

    /**
     * Elimina un usuario existente.
     *
     * @param id Identificador del usuario a eliminar
     * @throws RecursoNoEncontradoException Si el usuario no existe
     */
    public void eliminarUsuario(Long id) {
        // Buscar el usuario por ID o lanzar una excepción si no existe
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(USUARIO_NO_ENCONTRADO_CON + "id " + id));

        // Eliminar el usuario de la base de datos
        usuarioRepository.delete(usuario);
    }

    /**
     * Cambia el rol de un usuario existente.
     *
     * @param id Identificador del usuario
     * @param nuevoRol Nuevo rol a asignar
     * @return Detalles del usuario con rol actualizado
     * @throws RecursoNoEncontradoException Si el usuario no existe
     */
    public UsuarioDetalleDTO cambiarRol(Long id, String nuevoRol) {
        // Buscar usuario por ID
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(USUARIO_NO_ENCONTRADO_CON + "id " + id));

        // Actualizar rol
        usuario.setRol(Rol.valueOf(nuevoRol.toUpperCase()));

        // Guardar cambios y devolver detalles actualizados
        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        return convertirAUsuarioDetalleDTO(usuarioActualizado);
    }

    /**
     * Convierte una entidad Usuario a un DTO de detalles.
     *
     * @param usuario Entidad de usuario
     * @return DTO con detalles del usuario
     */
    private UsuarioDetalleDTO convertirAUsuarioDetalleDTO(Usuario usuario) {
        UsuarioDetalleDTO dto = new UsuarioDetalleDTO();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setEmail(usuario.getEmail());
        dto.setRol(usuario.getRol().name());
        return dto;
    }

    private UsuarioListadoDTO convertirAUsuarioListadoDTO(Usuario usuario) {
        UsuarioListadoDTO dto = new UsuarioListadoDTO();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setEmail(usuario.getEmail());
        return dto;
    }

    // Obtener un usuario por ID
    public Usuario obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(USUARIO_NO_ENCONTRADO_CON + "id " + id));
    }
}
