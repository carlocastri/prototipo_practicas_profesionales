package co.edu.ucn.practicas.persistencia;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** Tabla EMPRESA del modelo entidad-relacion (Taller 2, seccion 5.3). */
@Entity
@Table(name = "empresa")
public class Empresa {

    public static final String PENDIENTE_VALIDACION = "PENDIENTE_VALIDACION";
    public static final String APROBADA = "APROBADA";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empresa")
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String nit;

    @Column(name = "razon_social", nullable = false, length = 150)
    private String razonSocial;

    @Column(name = "representante_legal", nullable = false, length = 150)
    private String representanteLegal;

    @Column(nullable = false, length = 150)
    private String correo;

    @Column(nullable = false, length = 30)
    private String estado = PENDIENTE_VALIDACION;

    public Empresa() {
    }

    public Empresa(String nit, String razonSocial, String representanteLegal, String correo) {
        this.nit = nit;
        this.razonSocial = razonSocial;
        this.representanteLegal = representanteLegal;
        this.correo = correo;
    }

    public boolean estaAprobada() {
        return APROBADA.equals(estado);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNit() { return nit; }
    public void setNit(String nit) { this.nit = nit; }
    public String getRazonSocial() { return razonSocial; }
    public void setRazonSocial(String razonSocial) { this.razonSocial = razonSocial; }
    public String getRepresentanteLegal() { return representanteLegal; }
    public void setRepresentanteLegal(String representanteLegal) { this.representanteLegal = representanteLegal; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
