package co.edu.ucn.practicas.persistencia;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VacanteRepository extends JpaRepository<Vacante, Long> {

    List<Vacante> findByFechaCierreGreaterThanEqualOrderByFechaCierreAsc(LocalDate fecha);
}
