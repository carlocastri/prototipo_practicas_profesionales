package co.edu.ucn.practicas.presentacion;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.ucn.practicas.negocio.VacanteService;
import co.edu.ucn.practicas.persistencia.Vacante;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/vacantes")
public class VacanteController {

    private final VacanteService vacanteService;

    public VacanteController(VacanteService vacanteService) {
        this.vacanteService = vacanteService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Vacante publicar(@Valid @RequestBody VacanteRequest solicitud) {
        return vacanteService.publicar(solicitud.idEmpresa(), solicitud.titulo(), solicitud.descripcion(),
                solicitud.criteriosSeleccion(), solicitud.cupos(), solicitud.modalidad(), solicitud.fechaCierre());
    }

    @GetMapping
    public List<Vacante> listarVigentes() {
        return vacanteService.listarVigentes();
    }
}
