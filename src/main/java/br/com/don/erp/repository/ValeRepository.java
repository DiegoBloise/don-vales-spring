package br.com.don.erp.repository;

import java.time.LocalDate;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import br.com.don.erp.enums.TipoVale;
import br.com.don.erp.model.Colaborador;
import br.com.don.erp.model.Entregador;
import br.com.don.erp.model.Vale;


@Named
public class ValeRepository extends GenericRepository<Vale, Long> {

	@Inject
    private EntityManager entityManager;


	public List<Vale> listarOrdenadoPorData(){
		String jpql = "select v from Vale v order by v.data desc, id";
		try {
			return entityManager.createQuery(jpql, Vale.class)
				.getResultList();
		} catch (NoResultException e) {
            return null;
        }
	}


	public List<Vale> listarOrdenadoPorId(){
		String jpql = "select v from Vale v order by v.id desc";
		try {
			return entityManager.createQuery(jpql, Vale.class)
				.getResultList();
		} catch (NoResultException e) {
            return null;
        }
	}


	public List<Vale> buscarPorColaboradorDataTipo(Colaborador colaborador, LocalDate data, TipoVale tipo){
		String jpql = "select v from Vale v where v.colaborador = :colaborador AND v.data = :data AND v.tipo = :tipo";
		try {
			return entityManager.createQuery(jpql, Vale.class)
				.setParameter("colaborador", colaborador)
				.setParameter("data", data)
				.setParameter("tipo", tipo)
				.getResultList();
		} catch (NoResultException e) {
            return null;
        }
	}


	public List<Vale> buscarPorData(LocalDate data){
		String jpql = "select v from Vale v where v.data = :data";
		try {
			return entityManager.createQuery(jpql, Vale.class)
				.setParameter("data", data)
				.getResultList();
		} catch (NoResultException e) {
            return null;
        }
	}


	public List<Vale> buscarPorColaboradorDataInicioFim(Colaborador colaborador, LocalDate dataInicio, LocalDate dataFim){
		String jpql = "select v from Vale v where v.colaborador = :colaborador AND v.data >= :dataInicio AND v.data <= :dataFim";
		try {
			return entityManager.createQuery(jpql, Vale.class)
				.setParameter("colaborador", colaborador)
				.setParameter("dataInicio", dataInicio)
				.setParameter("dataFim", dataFim)
				.getResultList();
		} catch (NoResultException e) {
            return null;
        }
	}


	public List<Vale> buscarSaldo(Entregador colaborador, LocalDate data){
		String jpql = "SELECT v FROM Vale v WHERE v.colaborador = :colaborador AND v.data > :data AND v.tipo = :tipo";
		return entityManager.createQuery(jpql, Vale.class)
				.setParameter("colaborador", colaborador)
				.setParameter("data", data)
				.setParameter("tipo", TipoVale.SALDO)
				.getResultList();
	}
}
