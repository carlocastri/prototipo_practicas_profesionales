package co.edu.ucn.practicas.negocio;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.ucn.practicas.persistencia.Estudiante;
import co.edu.ucn.practicas.persistencia.EstudianteRepository;

/** Registro de estudiantes del prototipo. */
@Service
public class EstudianteService {

    private final EstudianteRepository estudianteRepository;

    public EstudianteService(EstudianteRepository estudianteRepository) {
        this.estudianteRepository = estudianteRepository;
    }

    @Transactional
    public Estudiante registrar(String documento, String nombre, String correo, String programa, Integer semestre) {
        if (estudianteRepository.existsByDocumento(documento)) {
            throw new ReglaNegocioException("Ya existe un estudiante registrado con el documento " + documento + ".");
        }
        return estudianteRepository.save(new Estudiante(documento, nombre, correo, programa, semestre));
    }

    @Transactional(readOnly = true)
    public List<Estudiante> listar() {
        return estudianteRepository.findAll();
    }
}
