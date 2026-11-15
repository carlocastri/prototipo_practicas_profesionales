package co.edu.ucn.practicas.presentacion;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.ucn.practicas.negocio.PostulacionService;
import co.edu.ucn.practicas.persistencia.Postulacion;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/postulaciones")
public class PostulacionController {

    private final PostulacionService postulacionService;

    public PostulacionController(PostulacionService postulacionService) {
        this.postulacionService = postulacionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Postulacion postular(@Valid @RequestBody PostulacionRequest solicitud) {
        return postulacionService.postular(solicitud.idEstudiante(), solicitud.idVacante(), solicitud.autorizaDatos());
    }

    @GetMapping
    public List<Postulacion> listar() {
        return postulacionService.listar();
    }
}
