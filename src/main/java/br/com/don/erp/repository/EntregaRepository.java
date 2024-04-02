package br.com.don.erp.repository;

import java.time.LocalDate;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import br.com.don.erp.model.Entrega;
import br.com.don.erp.model.Entregador;

@Named
public class EntregaRepository extends GenericRepository<Entrega, Long> {

	@Inject
    private EntityManager entityManager;


	public List<Entrega> buscarPorEntregadorData(Entregador entregador, LocalDate data){
		String jpql = "select e from Entrega e where e.entregador = :entregador AND e.data = :data";
		try {
			return entityManager.createQuery(jpql, Entrega.class)
				.setParameter("entregador", entregador)
				.setParameter("data", data).getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}


	public List<Entrega> buscarPorEntregadorDataInicioDataFim(Entregador entregador, LocalDate dataInicio, LocalDate dataFim){
		String jpql = "select e from Entrega e where e.entregador = :entregador AND e.data >= :dataInicio AND e.data <= :dataFim order by e.data";
		try {
			return entityManager.createQuery(jpql, Entrega.class)
				.setParameter("entregador", entregador)
				.setParameter("dataInicio", dataInicio)
				.setParameter("dataFim", dataFim)
				.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}


	public List<Entrega> buscarPorData(LocalDate data){
		String jpql = "select e from Entrega e where e.data = :data";
		try {
			return entityManager.createQuery(jpql, Entrega.class)
				.setParameter("data", data)
				.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}


	public List<Entregador> buscarEntregadoresPorData(LocalDate data) {
        String jpql = "SELECT DISTINCT e.entregador FROM Entrega e WHERE e.data = :data";
        try {
			return entityManager.createQuery(jpql, Entregador.class)
				.setParameter("data", data)
				.getResultList();
		} catch (NoResultException e) {
			return null;
		}
    }


    public List<Entregador> buscarEntregadoresPorDataInicioFim(LocalDate dataInicio, LocalDate dataFim) {
        String jpql = "SELECT DISTINCT e.entregador FROM Entrega e WHERE e.data BETWEEN :dataInicio AND :dataFim";
        try {
			return entityManager.createQuery(jpql, Entregador.class)
				.setParameter("dataInicio", dataInicio)
				.setParameter("dataFim", dataFim)
				.getResultList();
		} catch (NoResultException e) {
			return null;
		}
    }


	public Long buscarMovimento(LocalDate data) {
		String jpql = "select count(e) from Entrega e where e.data = :data";
		try {
			return (Long) entityManager.createQuery(jpql)
				.setParameter("data", data)
				.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}


	public Integer deletarMovimento(LocalDate data) {
		String jpql = "delete from Entrega e where e.data = :data";
		try {
			Integer retorno;
			entityManager.getTransaction().begin();
			retorno = entityManager.createQuery(jpql).setParameter("data", data).executeUpdate();
			entityManager.getTransaction().commit();
			return retorno;
		} catch (NoResultException e) {
			return null;
		}
	}
}
