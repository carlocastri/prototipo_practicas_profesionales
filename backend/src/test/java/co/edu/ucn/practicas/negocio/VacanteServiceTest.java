package co.edu.ucn.practicas.negocio;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.ucn.practicas.persistencia.Empresa;
import co.edu.ucn.practicas.persistencia.EmpresaRepository;
import co.edu.ucn.practicas.persistencia.Vacante;
import co.edu.ucn.practicas.persistencia.VacanteRepository;

@ExtendWith(MockitoExtension.class)
class VacanteServiceTest {

    @Mock
    private VacanteRepository vacanteRepository;

    @Mock
    private EmpresaRepository empresaRepository;

    @InjectMocks
    private VacanteService vacanteService;

    @Test
    @DisplayName("CP03 - Una empresa pendiente de validacion no puede publicar vacantes (RF03, RF05)")
    void empresaPendienteNoPuedePublicarVacantes() {
        Empresa pendiente = new Empresa("900123456-7", "Soluciones Andinas S.A.S.", "Laura Restrepo",
                "contacto@solucionesandinas.example");
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(pendiente));

        assertThatThrownBy(() -> vacanteService.publicar(1L, "Practicante de desarrollo", "Apoyo al equipo backend",
                "Semestre 8 o superior", 2, "HIBRIDA", LocalDate.now().plusDays(30)))
                .isInstanceOf(ReglaNegocioException.class)
                .hasMessageContaining("pendiente de validacion");

        verify(vacanteRepository, never()).save(any(Vacante.class));
    }

    @Test
    @DisplayName("CP04 - Una empresa aprobada publica una vacante en estado PUBLICADA (RF05)")
    void empresaAprobadaPublicaVacante() {
        Empresa aprobada = new Empresa("900123456-7", "Soluciones Andinas S.A.S.", "Laura Restrepo",
                "contacto@solucionesandinas.example");
        aprobada.setEstado(Empresa.APROBADA);
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(aprobada));
        when(vacanteRepository.save(any(Vacante.class))).thenAnswer(invocacion -> invocacion.getArgument(0));

        Vacante vacante = vacanteService.publicar(1L, "Practicante de desarrollo", "Apoyo al equipo backend",
                "Semestre 8 o superior", 2, "HIBRIDA", LocalDate.now().plusDays(30));

        assertThat(vacante.getEstado()).isEqualTo(Vacante.PUBLICADA);
        assertThat(vacante.getEmpresa()).isSameAs(aprobada);
    }
}
