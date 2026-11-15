package co.edu.ucn.practicas.persistencia;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    boolean existsByNit(String nit);
}
