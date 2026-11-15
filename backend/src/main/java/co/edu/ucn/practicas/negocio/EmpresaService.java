package co.edu.ucn.practicas.negocio;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.ucn.practicas.persistencia.Empresa;
import co.edu.ucn.practicas.persistencia.EmpresaRepository;

/** Modulo M2. Empresas: registro (RF03) y validacion por la coordinacion (RF04). */
@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepository;

    public EmpresaService(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    /** RF03: el NIT es unico y la empresa queda pendiente de validacion. */
    @Transactional
    public Empresa registrar(String nit, String razonSocial, String representanteLegal, String correo) {
        if (empresaRepository.existsByNit(nit)) {
            throw new ReglaNegocioException("Ya existe una empresa registrada con el NIT " + nit + ".");
        }
        Empresa empresa = new Empresa(nit, razonSocial, representanteLegal, correo);
        empresa.setEstado(Empresa.PENDIENTE_VALIDACION);
        return empresaRepository.save(empresa);
    }

    /** RF04 (parcial): el coordinador aprueba la empresa. */
    @Transactional
    public Empresa aprobar(Long idEmpresa) {
        Empresa empresa = empresaRepository.findById(idEmpresa)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la empresa con id " + idEmpresa + "."));
        empresa.setEstado(Empresa.APROBADA);
        return empresaRepository.save(empresa);
    }

    @Transactional(readOnly = true)
    public List<Empresa> listar() {
        return empresaRepository.findAll();
    }
}
