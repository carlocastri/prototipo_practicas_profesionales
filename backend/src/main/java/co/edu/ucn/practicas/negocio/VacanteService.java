package co.edu.ucn.practicas.negocio;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.ucn.practicas.persistencia.Empresa;
import co.edu.ucn.practicas.persistencia.EmpresaRepository;
import co.edu.ucn.practicas.persistencia.Vacante;
import co.edu.ucn.practicas.persistencia.VacanteRepository;

/** Modulo M3. Vacantes: publicacion (RF05) y consulta de vacantes vigentes (RF06). */
@Service
public class VacanteService {

    private final VacanteRepository vacanteRepository;
    private final EmpresaRepository empresaRepository;

    public VacanteService(VacanteRepository vacanteRepository, EmpresaRepository empresaRepository) {
        this.vacanteRepository = vacanteRepository;
        this.empresaRepository = empresaRepository;
    }

    /** RF03/RF05: una empresa no puede publicar vacantes hasta ser aprobada. */
    @Transactional
    public Vacante publicar(Long idEmpresa, String titulo, String descripcion, String criteriosSeleccion,
                            Integer cupos, String modalidad, LocalDate fechaCierre) {
        Empresa empresa = empresaRepository.findById(idEmpresa)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la empresa con id " + idEmpresa + "."));
        if (!empresa.estaAprobada()) {
            throw new ReglaNegocioException(
                    "La empresa esta pendiente de validacion y no puede publicar vacantes.");
        }
        Vacante vacante = new Vacante(empresa, titulo, descripcion, criteriosSeleccion, cupos, modalidad, fechaCierre);
        return vacanteRepository.save(vacante);
    }

    /** RF06: solo se listan las vacantes vigentes (fecha de cierre no vencida). */
    @Transactional(readOnly = true)
    public List<Vacante> listarVigentes() {
        return vacanteRepository.findByFechaCierreGreaterThanEqualOrderByFechaCierreAsc(LocalDate.now());
    }
}
