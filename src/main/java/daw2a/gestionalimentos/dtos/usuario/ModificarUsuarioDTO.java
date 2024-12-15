package daw2a.gestionalimentos.dtos.usuario;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class ModificarUsuarioDTO {
    private String nombre;

    @Email
    private String email;

    private String password;

    private String rol;
}
