package co.edu.ucn.practicas.negocio;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.ucn.practicas.persistencia.Empresa;
import co.edu.ucn.practicas.persistencia.Estudiante;
import co.edu.ucn.practicas.persistencia.EstudianteRepository;
import co.edu.ucn.practicas.persistencia.Postulacion;
import co.edu.ucn.practicas.persistencia.PostulacionRepository;
import co.edu.ucn.practicas.persistencia.Vacante;
import co.edu.ucn.practicas.persistencia.VacanteRepository;

@ExtendWith(MockitoExtension.class)
class PostulacionServiceTest {

    @Mock
    private PostulacionRepository postulacionRepository;

    @Mock
    private EstudianteRepository estudianteRepository;

    @Mock
    private VacanteRepository vacanteRepository;

    @InjectMocks
    private PostulacionService postulacionService;

    private Estudiante estudiante;
    private Vacante vacante;

    @BeforeEach
    void prepararDatos() {
        estudiante = new Estudiante("1020304050", "Ana Maria Lopez", "ana.lopez@ucn.example",
                "Ingenieria Informatica", 8);
        Empresa empresa = new Empresa("900123456-7", "Soluciones Andinas S.A.S.", "Laura Restrepo",
                "contacto@solucionesandinas.example");
        empresa.setEstado(Empresa.APROBADA);
        vacante = new Vacante(empresa, "Practicante de desarrollo", "Apoyo al equipo backend",
                "Semestre 8 o superior", 2, "HIBRIDA", LocalDate.now().plusDays(30));
    }

    @Test
    @DisplayName("CP05 - No se puede postular sin autorizar el tratamiento de datos (RF08)")
    void postulacionSinAutorizacionDeDatosEsRechazada() {
        assertThatThrownBy(() -> postulacionService.postular(1L, 1L, false))
                .isInstanceOf(ReglaNegocioException.class)
                .hasMessageContaining("autorizar");

        verify(postulacionRepository, never()).save(any(Postulacion.class));
    }

    @Test
    @DisplayName("CP06 - No se admiten dos postulaciones del mismo estudiante a la misma vacante (RF09)")
    void postulacionDuplicadaEsRechazada() {
        when(estudianteRepository.findById(1L)).thenReturn(Optional.of(estudiante));
        when(vacanteRepository.findById(1L)).thenReturn(Optional.of(vacante));
        when(postulacionRepository.existsByEstudianteIdAndVacanteId(1L, 1L)).thenReturn(true);

        assertThatThrownBy(() -> postulacionService.postular(1L, 1L, true))
                .isInstanceOf(ReglaNegocioException.class)
                .hasMessageContaining("ya esta postulado");

        verify(postulacionRepository, never()).save(any(Postulacion.class));
    }

    @Test
    @DisplayName("CP07 - Una postulacion valida queda en estado POSTULADA con la version de la politica (RF08, RF09)")
    void postulacionValidaQuedaEnEstadoPostulada() {
        when(estudianteRepository.findById(1L)).thenReturn(Optional.of(estudiante));
        when(vacanteRepository.findById(1L)).thenReturn(Optional.of(vacante));
        when(postulacionRepository.existsByEstudianteIdAndVacanteId(1L, 1L)).thenReturn(false);
        when(postulacionRepository.save(any(Postulacion.class))).thenAnswer(invocacion -> invocacion.getArgument(0));

        Postulacion postulacion = postulacionService.postular(1L, 1L, true);

        assertThat(postulacion.getEstado()).isEqualTo(Postulacion.POSTULADA);
        assertThat(postulacion.isAutorizaDatos()).isTrue();
        assertThat(postulacion.getVersionPolitica()).isEqualTo(PostulacionService.VERSION_POLITICA);
        assertThat(postulacion.getFechaPostulacion()).isNotNull();
    }
}
