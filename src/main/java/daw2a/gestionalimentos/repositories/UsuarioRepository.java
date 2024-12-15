package daw2a.gestionalimentos.repositories;

import daw2a.gestionalimentos.entities.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    //Buscar usuario por correo electrónico
    Optional<Usuario> findUsuarioByEmail(String email);

    Page<Usuario> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);
}
