package daw2a.gestionalimentos.dtos;

import daw2a.gestionalimentos.enums.EstadoSelect;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MoverAlimentosRequest {
    private LocalDate fechaLimite;
    private EstadoSelect estado;

    // Getters y Setters
    public LocalDate getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(LocalDate fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public EstadoSelect getEstado() {
        return estado;
    }

    public void setEstado(EstadoSelect estado) {
        this.estado = estado;
    }
}
