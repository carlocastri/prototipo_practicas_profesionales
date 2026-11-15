package co.edu.ucn.practicas.transversal;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import co.edu.ucn.practicas.negocio.RecursoNoEncontradoException;
import co.edu.ucn.practicas.negocio.ReglaNegocioException;

/** Manejo uniforme de errores de la API REST. */
@RestControllerAdvice
public class ManejadorErrores {

    @ExceptionHandler(ReglaNegocioException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> reglaDeNegocio(ReglaNegocioException ex) {
        return Map.of("mensaje", ex.getMessage());
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> noEncontrado(RecursoNoEncontradoException ex) {
        return Map.of("mensaje", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> datosInvalidos(MethodArgumentNotValidException ex) {
        String mensaje = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .findFirst()
                .orElse("Los datos enviados no son validos.");
        return Map.of("mensaje", mensaje);
    }
}
