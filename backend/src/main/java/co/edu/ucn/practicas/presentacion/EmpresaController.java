package co.edu.ucn.practicas.presentacion;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.ucn.practicas.negocio.EmpresaService;
import co.edu.ucn.practicas.persistencia.Empresa;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/empresas")
public class EmpresaController {

    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Empresa registrar(@Valid @RequestBody EmpresaRequest solicitud) {
        return empresaService.registrar(solicitud.nit(), solicitud.razonSocial(),
                solicitud.representanteLegal(), solicitud.correo());
    }

    @PutMapping("/{id}/aprobar")
    public Empresa aprobar(@PathVariable("id") Long id) {
        return empresaService.aprobar(id);
    }

    @GetMapping
    public List<Empresa> listar() {
        return empresaService.listar();
    }
}
