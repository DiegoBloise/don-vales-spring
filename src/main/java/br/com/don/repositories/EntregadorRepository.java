package br.com.don.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.don.models.Entregador;

@Repository
public interface EntregadorRepository extends JpaRepository<Entregador, Long> {

    @Query(value = "SELECT * FROM Entregador ORDER BY nome", nativeQuery = true)
    List<Entregador> listarPorNome();

    @Query(value = "SELECT DISTINCT e.entregador FROM Entrega e WHERE e.data = :data", nativeQuery = true)
    List<Entregador> listarEntregadoresPorData(@Param("data") LocalDate data);

    @Query(value = "SELECT DISTINCT e.entregador FROM Entrega e WHERE e.data BETWEEN :dataInicio AND :dataFim ORDER BY e.entregador.nome", nativeQuery = true)
    List<Entregador> listarEntregadoresPorDataInicioFim(@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);

    @Query(value = "SELECT * FROM Entregador WHERE id = :entregadorId", nativeQuery = true)
    Entregador find(@Param("entregadorId") Long entregadorId);

    @Query(value = "SELECT * FROM Entregador WHERE :propertyName = :value", nativeQuery = true)
    List<Entregador> findAllByProperty(@Param("propertyName") String propertyName, @Param("value") Object value);

    @Query(value = "SELECT * FROM Entregador WHERE :propertyName = :value", nativeQuery = true)
    Entregador findByProperty(@Param("propertyName") String propertyName, @Param("value") Object value);
}
