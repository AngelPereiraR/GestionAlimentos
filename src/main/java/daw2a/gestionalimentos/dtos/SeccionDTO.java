package daw2a.gestionalimentos.dtos;
import lombok.Data;
import java.util.List;

@Data
public class SeccionDTO {
    private Long id;
    private String nombre;
    private Integer limite;
    private Integer accesibilidad;
    private Long idAlmacen;
    private List<RecipienteDTO> listaRecipientes;
}
