package co.edu.ucn.practicas.integracion;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.jayway.jsonpath.JsonPath;

/**
 * Prueba de integracion: recorre la API REST, la capa de negocio y la base de datos H2
 * con el flujo registro de empresa -> aprobacion -> vacante -> estudiante -> postulacion.
 */
@SpringBootTest
class FlujoPracticasIntegrationTest {

    @Autowired
    private WebApplicationContext contexto;

    private MockMvc mockMvc;

    @BeforeEach
    void configurar() {
        mockMvc = MockMvcBuilders.webAppContextSetup(contexto).build();
    }

    @Test
    @DisplayName("PI01 - Flujo completo: registro, aprobacion, publicacion de vacante y postulacion")
    void flujoCompletoDeRegistroPublicacionYPostulacion() throws Exception {
        // 1. La empresa se registra y queda pendiente de validacion.
        String empresaJson = """
                {"nit":"900555111-2","razonSocial":"Tecnologia del Norte S.A.S.",
                 "representanteLegal":"Marta Quintero","correo":"contacto@tecnorte.example"}
                """;
        String respuestaEmpresa = mockMvc.perform(post("/api/empresas")
                        .contentType(MediaType.APPLICATION_JSON).content(empresaJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.estado").value("PENDIENTE_VALIDACION"))
                .andReturn().getResponse().getContentAsString();
        int idEmpresa = JsonPath.read(respuestaEmpresa, "$.id");

        // 2. Sin aprobacion, la empresa no puede publicar vacantes.
        String vacanteJson = """
                {"idEmpresa":%d,"titulo":"Practicante de analitica","descripcion":"Apoyo en tableros de datos",
                 "criteriosSeleccion":"Semestre 7 o superior","cupos":1,"modalidad":"REMOTA","fechaCierre":"%s"}
                """.formatted(idEmpresa, LocalDate.now().plusDays(20));
        mockMvc.perform(post("/api/vacantes").contentType(MediaType.APPLICATION_JSON).content(vacanteJson))
                .andExpect(status().isConflict());

        // 3. El coordinador aprueba la empresa.
        mockMvc.perform(put("/api/empresas/" + idEmpresa + "/aprobar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("APROBADA"));

        // 4. La empresa aprobada publica la vacante.
        String respuestaVacante = mockMvc.perform(post("/api/vacantes")
                        .contentType(MediaType.APPLICATION_JSON).content(vacanteJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.estado").value("PUBLICADA"))
                .andReturn().getResponse().getContentAsString();
        int idVacante = JsonPath.read(respuestaVacante, "$.id");

        // 5. El estudiante se registra.
        String estudianteJson = """
                {"documento":"1098765432","nombre":"Juan Camilo Perez","correo":"juan.perez@ucn.example",
                 "programa":"Ingenieria Informatica","semestre":8}
                """;
        String respuestaEstudiante = mockMvc.perform(post("/api/estudiantes")
                        .contentType(MediaType.APPLICATION_JSON).content(estudianteJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        int idEstudiante = JsonPath.read(respuestaEstudiante, "$.id");

        // 6. El estudiante se postula con autorizacion de datos.
        String postulacionJson = """
                {"idEstudiante":%d,"idVacante":%d,"autorizaDatos":true}
                """.formatted(idEstudiante, idVacante);
        mockMvc.perform(post("/api/postulaciones").contentType(MediaType.APPLICATION_JSON).content(postulacionJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.estado").value("POSTULADA"));

        // 7. Una segunda postulacion a la misma vacante es rechazada.
        mockMvc.perform(post("/api/postulaciones").contentType(MediaType.APPLICATION_JSON).content(postulacionJson))
                .andExpect(status().isConflict());

        // 8. La postulacion queda registrada y la vacante aparece entre las vigentes.
        mockMvc.perform(get("/api/postulaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
        mockMvc.perform(get("/api/vacantes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Practicante de analitica"));
    }

    @Test
    @DisplayName("PI02 - La API rechaza con 400 un registro de empresa con datos invalidos")
    void registroDeEmpresaConDatosInvalidosDevuelve400() throws Exception {
        String empresaInvalida = """
                {"nit":"123","razonSocial":"","representanteLegal":"Sin Nombre","correo":"no-es-correo"}
                """;
        mockMvc.perform(post("/api/empresas").contentType(MediaType.APPLICATION_JSON).content(empresaInvalida))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").exists());
    }
}
