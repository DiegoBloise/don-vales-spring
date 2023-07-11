package br.com.don.erp.repository;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.don.erp.model.Entrega;

public class Entregas implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public List<Entrega> listar(){
		return manager.createQuery("from Entrega", Entrega.class).getResultList();
	}

	public void salvar(Entrega entrega) {
		manager.getTransaction().begin();
		manager.merge(entrega);
		manager.getTransaction().commit();
	}
	
	public List<Entrega> buscarPorEntregador(String entregador){
		String jpql = "select e from Entrega e where e.entregador = :entregador";
		return manager.createQuery(jpql, Entrega.class).setParameter("entregador", entregador)
				.getResultList();
	}
	
	public List<Entrega> buscarPorEntregadorData(String entregador, LocalDate data){
		String jpql = "select e from Entrega e where e.entregador = :entregador AND e.data = :data";
		return manager.createQuery(jpql, Entrega.class).setParameter("entregador", entregador).setParameter("data", data)
				.getResultList();
	}
	
	public List<Entrega> buscarPorEntregadorDataInicioDataFim(String entregador, LocalDate dataInicio, LocalDate dataFim){
		String jpql = "select e from Entrega e where e.entregador = :entregador AND e.data >= :dataInicio AND e.data <= :dataFim order by e.data";
		return manager.createQuery(jpql, Entrega.class)
				.setParameter("entregador", entregador)
				.setParameter("dataInicio", dataInicio)
				.setParameter("dataFim", dataFim)
				.getResultList();
	}
	
	public List<Entrega> buscarPorData(LocalDate data){
		String jpql = "select e from Entrega e where e.data = :data";
		return manager.createQuery(jpql, Entrega.class).setParameter("data", data)
				.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<String> buscarEntregadoresporData(LocalDate data){
		String jpql = "select DISTINCT (e.entregador) from Entrega e where e.data = :data order by e.entregador";
		return manager.createQuery(jpql).setParameter("data", data).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<String> buscarEntregadoresporDataInicioFim(LocalDate dataInicio, LocalDate dataFim) {
		String jpql = "select DISTINCT (e.entregador) from Entrega e where e.data >= :dataInicio AND e.data <= :dataFim order by e.entregador";
		return manager.createQuery(jpql).setParameter("dataInicio", dataInicio)
				.setParameter("dataFim", dataFim).getResultList();
	}

	public Long buscarMovimento(LocalDate data) {
		String jpql = "select count(e) from Entrega e where e.data = :data";
		return  (Long) manager.createQuery(jpql).setParameter("data", data)
				.getSingleResult();
	}
	
	public Integer deletarMovimento(LocalDate data) {
		String jpql = "delete from Entrega e where e.data = :data";
		Integer retorno; 
		manager.getTransaction().begin();
		retorno = manager.createQuery(jpql).setParameter("data", data).executeUpdate();
		manager.getTransaction().commit();
		
		return retorno;
	}
}
