package co.edu.ucn.practicas.presentacion;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record EmpresaRequest(
        @NotBlank(message = "El NIT es obligatorio")
        @Pattern(regexp = "\\d{9}-?\\d", message = "El NIT debe tener 9 digitos y digito de verificacion")
        String nit,
        @NotBlank(message = "La razon social es obligatoria") String razonSocial,
        @NotBlank(message = "El representante legal es obligatorio") String representanteLegal,
        @NotBlank(message = "El correo es obligatorio") @Email(message = "El correo no es valido") String correo) {
}
