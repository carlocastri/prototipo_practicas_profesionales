package co.edu.ucn.practicas.negocio;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.ucn.practicas.persistencia.Empresa;
import co.edu.ucn.practicas.persistencia.EmpresaRepository;

@ExtendWith(MockitoExtension.class)
class EmpresaServiceTest {

    @Mock
    private EmpresaRepository empresaRepository;

    @InjectMocks
    private EmpresaService empresaService;

    @Test
    @DisplayName("CP01 - Registrar empresa con NIT nuevo la deja pendiente de validacion (RF03)")
    void registrarEmpresaConNitNuevoQuedaPendienteDeValidacion() {
        when(empresaRepository.existsByNit("900123456-7")).thenReturn(false);
        when(empresaRepository.save(any(Empresa.class))).thenAnswer(invocacion -> invocacion.getArgument(0));

        Empresa empresa = empresaService.registrar("900123456-7", "Soluciones Andinas S.A.S.",
                "Laura Restrepo", "contacto@solucionesandinas.example");

        assertThat(empresa.getEstado()).isEqualTo(Empresa.PENDIENTE_VALIDACION);
        assertThat(empresa.getNit()).isEqualTo("900123456-7");
        verify(empresaRepository).save(any(Empresa.class));
    }

    @Test
    @DisplayName("CP02 - Registrar empresa con NIT duplicado es rechazado (RF03)")
    void registrarEmpresaConNitDuplicadoEsRechazado() {
        when(empresaRepository.existsByNit("900123456-7")).thenReturn(true);

        assertThatThrownBy(() -> empresaService.registrar("900123456-7", "Otra Empresa S.A.S.",
                "Pedro Gomez", "pedro@otraempresa.example"))
                .isInstanceOf(ReglaNegocioException.class)
                .hasMessageContaining("NIT");

        verify(empresaRepository, never()).save(any(Empresa.class));
    }
}
