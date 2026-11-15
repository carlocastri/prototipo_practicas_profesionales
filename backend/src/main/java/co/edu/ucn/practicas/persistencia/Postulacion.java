package co.edu.ucn.practicas.persistencia;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * Tabla POSTULACION: resuelve la relacion muchos a muchos entre ESTUDIANTE y VACANTE,
 * con restriccion de unicidad sobre el par estudiante-vacante (Taller 2, seccion 5.3).
 */
@Entity
@Table(name = "postulacion",
       uniqueConstraints = @UniqueConstraint(name = "uk_postulacion_estudiante_vacante",
                                             columnNames = {"id_estudiante", "id_vacante"}))
public class Postulacion {

    public static final String POSTULADA = "POSTULADA";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_postulacion")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_estudiante", nullable = false)
    private Estudiante estudiante;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_vacante", nullable = false)
    private Vacante vacante;

    @Column(nullable = false, length = 20)
    private String estado = POSTULADA;

    @Column(name = "autoriza_datos", nullable = false)
    private boolean autorizaDatos;

    @Column(name = "version_politica", nullable = false, length = 10)
    private String versionPolitica;

    @Column(name = "fecha_postulacion", nullable = false)
    private LocalDateTime fechaPostulacion;

    public Postulacion() {
    }

    public Postulacion(Estudiante estudiante, Vacante vacante, boolean autorizaDatos,
                       String versionPolitica, LocalDateTime fechaPostulacion) {
        this.estudiante = estudiante;
        this.vacante = vacante;
        this.autorizaDatos = autorizaDatos;
        this.versionPolitica = versionPolitica;
        this.fechaPostulacion = fechaPostulacion;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Estudiante getEstudiante() { return estudiante; }
    public void setEstudiante(Estudiante estudiante) { this.estudiante = estudiante; }
    public Vacante getVacante() { return vacante; }
    public void setVacante(Vacante vacante) { this.vacante = vacante; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public boolean isAutorizaDatos() { return autorizaDatos; }
    public void setAutorizaDatos(boolean autorizaDatos) { this.autorizaDatos = autorizaDatos; }
    public String getVersionPolitica() { return versionPolitica; }
    public void setVersionPolitica(String versionPolitica) { this.versionPolitica = versionPolitica; }
    public LocalDateTime getFechaPostulacion() { return fechaPostulacion; }
    public void setFechaPostulacion(LocalDateTime fechaPostulacion) { this.fechaPostulacion = fechaPostulacion; }
}
