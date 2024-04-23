package br.com.don.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.don.models.Colaborador;

@Repository
public interface ColaboradorRepository extends JpaRepository<Colaborador, Long> {

    @Query(value = "SELECT * FROM Colaborador ORDER BY nome", nativeQuery = true)
    List<Colaborador> listarPorNome();

    @Query(value = "SELECT * FROM Colaborador WHERE :propertyName = :value", nativeQuery = true)
    Colaborador findByProperty(@Param("propertyName") String propertyName, @Param("value") Object value);

    @Query(value = "SELECT * FROM Colaborador WHERE :propertyName = :value", nativeQuery = true)
    List<Colaborador> findAllByProperty(@Param("propertyName") String propertyName, @Param("value") Object value);
}
