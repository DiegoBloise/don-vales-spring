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

    @Query(value = "SELECT * FROM Entrega WHERE pedido = :pedido AND data = :data AND entregador = :entregador", nativeQuery = true)
    Entrega buscarPorPedidoDataEntregador(@Param("pedido") Integer pedido, @Param("data") LocalDate data, @Param("entregador") Entregador entregador);

    @Query(value = "SELECT * FROM Entrega WHERE entregador = :entregador AND data = :data", nativeQuery = true)
    List<Entrega> buscarPorEntregadorData(@Param("entregador") Entregador entregador, @Param("data") LocalDate data);

    @Query(value = "SELECT * FROM Entrega WHERE entregador = :entregador AND data >= :dataInicio AND data <= :dataFim ORDER BY data", nativeQuery = true)
    List<Entrega> buscarPorEntregadorDataInicioDataFim(@Param("entregador") Entregador entregador, @Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);

    @Query(value = "SELECT * FROM Entrega WHERE data = :data", nativeQuery = true)
    List<Entrega> buscarPorData(@Param("data") LocalDate data);

    @Query(value = "SELECT COUNT(*) FROM Entrega WHERE data = :data", nativeQuery = true)
    Long buscarMovimento(@Param("data") LocalDate data);

    @Query(value = "DELETE FROM Entrega WHERE data = :data", nativeQuery = true)
    Integer deletarMovimento(@Param("data") LocalDate data);

    @Query(value = "SELECT * FROM Entrega WHERE :propertyName = :value", nativeQuery = true)
    List<Entrega> findAllByProperty(@Param("propertyName") String propertyName, @Param("value") Object value);
}
