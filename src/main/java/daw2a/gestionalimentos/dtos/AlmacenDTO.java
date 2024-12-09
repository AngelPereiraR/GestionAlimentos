package daw2a.gestionalimentos.dtos;
import lombok.Data;
import java.util.List;

@Data
public class AlmacenDTO {
    private Long id;
    private String nombre;
    private String temp;
    private List<SeccionDTO> listSeccion;
}
