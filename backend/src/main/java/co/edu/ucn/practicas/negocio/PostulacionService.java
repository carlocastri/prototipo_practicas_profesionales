package co.edu.ucn.practicas.negocio;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.ucn.practicas.persistencia.Estudiante;
import co.edu.ucn.practicas.persistencia.EstudianteRepository;
import co.edu.ucn.practicas.persistencia.Postulacion;
import co.edu.ucn.practicas.persistencia.PostulacionRepository;
import co.edu.ucn.practicas.persistencia.Vacante;
import co.edu.ucn.practicas.persistencia.VacanteRepository;

/** Modulo M4. Postulacion: autorizacion de datos (RF08) y postulacion a una vacante (RF09). */
@Service
public class PostulacionService {

    /** Version de la politica de tratamiento de datos que acepta el estudiante. */
    public static final String VERSION_POLITICA = "1.0";

    private final PostulacionRepository postulacionRepository;
    private final EstudianteRepository estudianteRepository;
    private final VacanteRepository vacanteRepository;

    public PostulacionService(PostulacionRepository postulacionRepository,
                              EstudianteRepository estudianteRepository,
                              VacanteRepository vacanteRepository) {
        this.postulacionRepository = postulacionRepository;
        this.estudianteRepository = estudianteRepository;
        this.vacanteRepository = vacanteRepository;
    }

    @Transactional
    public Postulacion postular(Long idEstudiante, Long idVacante, boolean autorizaDatos) {
        // RF08: no se puede completar una postulacion sin autorizacion de tratamiento de datos.
        if (!autorizaDatos) {
            throw new ReglaNegocioException(
                    "Debe autorizar el tratamiento de datos personales para postularse (Ley 1581 de 2012).");
        }
        Estudiante estudiante = estudianteRepository.findById(idEstudiante)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el estudiante con id " + idEstudiante + "."));
        Vacante vacante = vacanteRepository.findById(idVacante)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la vacante con id " + idVacante + "."));
        if (!vacante.estaVigente(LocalDate.now())) {
            throw new ReglaNegocioException("La vacante ya cerro y no admite postulaciones.");
        }
        // RF09: no se admiten dos postulaciones del mismo estudiante a la misma vacante.
        if (postulacionRepository.existsByEstudianteIdAndVacanteId(idEstudiante, idVacante)) {
            throw new ReglaNegocioException("El estudiante ya esta postulado a esta vacante.");
        }
        Postulacion postulacion = new Postulacion(estudiante, vacante, true, VERSION_POLITICA, LocalDateTime.now());
        postulacion.setEstado(Postulacion.POSTULADA);
        return postulacionRepository.save(postulacion);
    }

    @Transactional(readOnly = true)
    public List<Postulacion> listar() {
        return postulacionRepository.findAll();
    }
}
