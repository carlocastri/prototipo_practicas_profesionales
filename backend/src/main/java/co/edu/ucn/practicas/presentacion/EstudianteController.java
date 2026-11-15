package co.edu.ucn.practicas.presentacion;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.ucn.practicas.negocio.EstudianteService;
import co.edu.ucn.practicas.persistencia.Estudiante;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    private final EstudianteService estudianteService;

    public EstudianteController(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Estudiante registrar(@Valid @RequestBody EstudianteRequest solicitud) {
        return estudianteService.registrar(solicitud.documento(), solicitud.nombre(), solicitud.correo(),
                solicitud.programa(), solicitud.semestre());
    }

    @GetMapping
    public List<Estudiante> listar() {
        return estudianteService.listar();
    }
}
