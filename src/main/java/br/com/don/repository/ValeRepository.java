package br.com.don.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.don.enums.TipoVale;
import br.com.don.model.Colaborador;
import br.com.don.model.Entregador;
import br.com.don.model.Vale;

@Repository
public interface ValeRepository extends JpaRepository<Vale, Long> {

    @Query("SELECT v FROM Vale v ORDER BY v.data desc, v.id")
    List<Vale> listarOrdenadoPorData();

    @Query("SELECT v FROM Vale v ORDER BY v.id desc")
    List<Vale> listarOrdenadoPorId();

    @Query("SELECT v FROM Vale v WHERE v.colaborador = :colaborador AND v.data = :data AND v.tipo = :tipo")
    List<Vale> buscarPorColaboradorDataTipo(@Param("colaborador") Colaborador colaborador, @Param("data") LocalDate data, @Param("tipo") TipoVale tipo);

    @Query("SELECT v FROM Vale v WHERE v.data = :data")
    List<Vale> buscarPorData(@Param("data") LocalDate data);

    @Query("SELECT v FROM Vale v WHERE v.colaborador = :colaborador AND v.data >= :dataInicio AND v.data <= :dataFim")
    List<Vale> buscarPorColaboradorDataInicioFim(@Param("colaborador") Colaborador colaborador, @Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);

    @Query("SELECT v FROM Vale v WHERE v.colaborador = :colaborador AND v.data > :data AND v.tipo = :tipo")
    List<Vale> buscarSaldo(@Param("colaborador") Entregador colaborador, @Param("data") LocalDate data);

    @Query("SELECT v FROM Vale v WHERE v.colaborador.id = :id")
    List<Vale> findAllByColaboradorId(@Param("id") Long id);
}
