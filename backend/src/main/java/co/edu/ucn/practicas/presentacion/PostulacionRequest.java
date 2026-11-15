package co.edu.ucn.practicas.presentacion;

import jakarta.validation.constraints.NotNull;

public record PostulacionRequest(
        @NotNull(message = "El estudiante es obligatorio") Long idEstudiante,
        @NotNull(message = "La vacante es obligatoria") Long idVacante,
        boolean autorizaDatos) {
}
