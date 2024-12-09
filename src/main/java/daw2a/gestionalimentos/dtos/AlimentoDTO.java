package daw2a.gestionalimentos.dtos;
import lombok.Data;
import java.time.LocalDate;

@Data
public class AlimentoDTO {
    private Long id;
    private String nombre;
    private Boolean perecedero;
    private Boolean abierto;
    private Integer tamano;
    private LocalDate fechaCaducidad;
    private String categoria;
    private String estado;
}
