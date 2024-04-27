package br.com.don.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.don.models.Colaborador;

@Repository
public interface ColaboradorRepository extends JpaRepository<Colaborador, Long> {

    @Query("SELECT c FROM Colaborador c ORDER BY c.nome")
    List<Colaborador> listarPorNome();

    @Query("SELECT c FROM Colaborador c WHERE c.:propertyName = :value")
    Colaborador findByProperty(@Param("propertyName") String propertyName, @Param("value") Object value);

    @Query("SELECT c FROM Colaborador c WHERE c.:propertyName = :value")
    List<Colaborador> findAllByProperty(@Param("propertyName") String propertyName, @Param("value") Object value);
}
