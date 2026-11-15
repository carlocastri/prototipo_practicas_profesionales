package co.edu.ucn.practicas.presentacion;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EstudianteRequest(
        @NotBlank(message = "El documento es obligatorio") String documento,
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        @NotBlank(message = "El correo es obligatorio") @Email(message = "El correo no es valido") String correo,
        @NotBlank(message = "El programa es obligatorio") String programa,
        @NotNull(message = "El semestre es obligatorio")
        @Min(value = 1, message = "El semestre debe estar entre 1 y 12")
        @Max(value = 12, message = "El semestre debe estar entre 1 y 12")
        Integer semestre) {
}
