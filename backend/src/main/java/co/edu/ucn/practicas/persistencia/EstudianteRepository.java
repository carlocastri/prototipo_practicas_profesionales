package co.edu.ucn.practicas.persistencia;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {

    boolean existsByDocumento(String documento);
}
