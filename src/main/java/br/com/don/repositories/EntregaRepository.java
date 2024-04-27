package br.com.don.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.don.models.Entrega;
import br.com.don.models.Entregador;

@Repository
public interface EntregaRepository extends JpaRepository<Entrega, Long> {

    @Query("SELECT e FROM Entrega e WHERE e.pedido = :pedido AND e.data = :data AND e.entregador = :entregador")
    Entrega buscarPorPedidoDataEntregador(@Param("pedido") Integer pedido, @Param("data") LocalDate data, @Param("entregador") Entregador entregador);

    @Query("SELECT e FROM Entrega e WHERE e.entregador = :entregador AND e.data = :data")
    List<Entrega> buscarPorEntregadorData(@Param("entregador") Entregador entregador, @Param("data") LocalDate data);

    @Query("SELECT e FROM Entrega e WHERE e.entregador = :entregador AND e.data >= :dataInicio AND e.data <= :dataFim ORDER BY e.data")
    List<Entrega> buscarPorEntregadorDataInicioDataFim(@Param("entregador") Entregador entregador, @Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);

    @Query("SELECT e FROM Entrega e WHERE e.data = :data")
    List<Entrega> buscarPorData(@Param("data") LocalDate data);

    @Query("SELECT COUNT(e) FROM Entrega e WHERE e.data = :data")
    Long buscarMovimento(@Param("data") LocalDate data);

    @Query("DELETE FROM Entrega e WHERE e.data = :data")
    Integer deletarMovimento(@Param("data") LocalDate data);

    @Query("SELECT e FROM Entrega e WHERE e.:propertyName = :value")
    List<Entrega> findAllByProperty(@Param("propertyName") String propertyName, @Param("value") Object value);
}
