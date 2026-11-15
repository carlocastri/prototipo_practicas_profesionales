package co.edu.ucn.practicas.negocio;

/** Se lanza cuando el recurso solicitado no existe (respuesta HTTP 404). */
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
