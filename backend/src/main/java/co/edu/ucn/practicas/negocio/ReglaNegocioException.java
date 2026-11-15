package co.edu.ucn.practicas.negocio;

/** Se lanza cuando una operacion incumple una regla de negocio (respuesta HTTP 409). */
public class ReglaNegocioException extends RuntimeException {

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
