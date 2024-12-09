package daw2a.gestionalimentos.dtos;
import lombok.Data;
import java.util.List;

@Data
public class RecipienteDTO {
    private Long id;
    private Long tamanyo;
    private Long idSeccion;
    private List<AlimentoDTO> listAlimentos;
}
