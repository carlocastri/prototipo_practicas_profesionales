package co.edu.ucn.practicas.persistencia;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostulacionRepository extends JpaRepository<Postulacion, Long> {

    boolean existsByEstudianteIdAndVacanteId(Long idEstudiante, Long idVacante);
}
