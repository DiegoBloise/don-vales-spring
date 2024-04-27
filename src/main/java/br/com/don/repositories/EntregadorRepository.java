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

   /*  @Query("select u from User u where u.firstname = :firstname or u.lastname = :lastname")
    User findByLastnameOrFirstname(@Param("lastname") String lastname,@Param("firstname") String firstname); */

    @Query("SELECT e FROM Entregador e ORDER BY e.nome")
    List<Entregador> listarPorNome();

    @Query("SELECT DISTINCT e.entregador FROM Entrega e WHERE e.data = :data")
    List<Entregador> listarEntregadoresPorData(@Param("data") LocalDate data);

    @Query("SELECT DISTINCT e.entregador FROM Entrega e WHERE e.data BETWEEN :dataInicio AND :dataFim ORDER BY e.entregador.nome")
    List<Entregador> listarEntregadoresPorDataInicioFim(@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);

    @Query("SELECT e FROM Entregador e WHERE id = :entregadorId")
    Entregador find(@Param("entregadorId") Long entregadorId);

    Entregador findByNome(String nome);
}
