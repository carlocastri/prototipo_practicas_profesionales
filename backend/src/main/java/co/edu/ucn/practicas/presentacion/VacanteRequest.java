package co.edu.ucn.practicas.presentacion;

import java.time.LocalDate;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VacanteRequest(
        @NotNull(message = "La empresa es obligatoria") Long idEmpresa,
        @NotBlank(message = "El titulo es obligatorio") String titulo,
        @NotBlank(message = "La descripcion es obligatoria") String descripcion,
        @NotBlank(message = "Los criterios de seleccion son obligatorios") String criteriosSeleccion,
        @NotNull(message = "Los cupos son obligatorios") @Min(value = 1, message = "Debe haber al menos un cupo") Integer cupos,
        @NotBlank(message = "La modalidad es obligatoria") String modalidad,
        @NotNull(message = "La fecha de cierre es obligatoria")
        @FutureOrPresent(message = "La fecha de cierre no puede ser anterior a hoy")
        LocalDate fechaCierre) {
}
