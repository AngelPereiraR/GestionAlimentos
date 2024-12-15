package daw2a.gestionalimentos.dtos.usuario;

import lombok.Data;

@Data
public class UsuarioDetalleDTO {
    private Long id;
    private String nombre;
    private String email;
    private String rol;
}

