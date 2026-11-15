package co.edu.ucn.practicas.persistencia;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/** Tabla VACANTE del modelo entidad-relacion (Taller 2, seccion 5.3). */
@Entity
@Table(name = "vacante")
public class Vacante {

    public static final String PUBLICADA = "PUBLICADA";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vacante")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_empresa", nullable = false)
    private Empresa empresa;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(nullable = false, length = 1000)
    private String descripcion;

    @Column(name = "criterios_seleccion", nullable = false, length = 1000)
    private String criteriosSeleccion;

    @Column(nullable = false)
    private Integer cupos;

    @Column(nullable = false, length = 20)
    private String modalidad;

    @Column(name = "fecha_cierre", nullable = false)
    private LocalDate fechaCierre;

    @Column(nullable = false, length = 20)
    private String estado = PUBLICADA;

    public Vacante() {
    }

    public Vacante(Empresa empresa, String titulo, String descripcion, String criteriosSeleccion,
                   Integer cupos, String modalidad, LocalDate fechaCierre) {
        this.empresa = empresa;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.criteriosSeleccion = criteriosSeleccion;
        this.cupos = cupos;
        this.modalidad = modalidad;
        this.fechaCierre = fechaCierre;
    }

    public boolean estaVigente(LocalDate hoy) {
        return !fechaCierre.isBefore(hoy);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Empresa getEmpresa() { return empresa; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getCriteriosSeleccion() { return criteriosSeleccion; }
    public void setCriteriosSeleccion(String criteriosSeleccion) { this.criteriosSeleccion = criteriosSeleccion; }
    public Integer getCupos() { return cupos; }
    public void setCupos(Integer cupos) { this.cupos = cupos; }
    public String getModalidad() { return modalidad; }
    public void setModalidad(String modalidad) { this.modalidad = modalidad; }
    public LocalDate getFechaCierre() { return fechaCierre; }
    public void setFechaCierre(LocalDate fechaCierre) { this.fechaCierre = fechaCierre; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
