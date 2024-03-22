package br.com.don.erp.repository;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.don.erp.enums.TipoVale;
import br.com.don.erp.model.Vale;

public class Vales implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;


	public List<Vale> listar(){
		return manager.createQuery("from Vale", Vale.class).getResultList();
	}


	public List<Vale> listarOrdenadoPorData(){
		String jpql = "select e from Vale e order by e.data desc, id";
		return manager.createQuery(jpql, Vale.class).getResultList();
	}


	public List<Vale> listarOrdenadoPorId(){
		String jpql = "select e from Vale e order by e.id desc";
		return manager.createQuery(jpql, Vale.class).getResultList();
	}


	public void salvar(Vale vale) {
		manager.getTransaction().begin();
		manager.merge(vale);
		manager.getTransaction().commit();
	}


	public List<Vale> buscarPorEntregador(String entregador){
		String jpql = "select e from Vale e where e.entregador = :entregador order by e.data desc";
		return manager.createQuery(jpql, Vale.class).setParameter("entregador", entregador)
				.getResultList();
	}


	public List<Vale> buscarPorEntregadorDataTipo(String entregador, LocalDate data, TipoVale tipo){
		String jpql = "select e from Vale e where e.entregador = :entregador AND e.data = :data AND e.tipoVale = :tipo";
		return manager.createQuery(jpql, Vale.class)
				.setParameter("entregador", entregador)
				.setParameter("data", data)
				.setParameter("tipo", tipo)
				.getResultList();
	}


	public List<Vale> buscarPorData(LocalDate data){
		String jpql = "select e from Vale e where e.data = :data";
		return manager.createQuery(jpql, Vale.class).setParameter("data", data)
				.getResultList();
	}


	public List<Vale> buscarPorEntregadorDataInicioFim(String entregador, LocalDate dataInicio, LocalDate dataFim){
		String jpql = "select e from Vale e where e.entregador = :entregador AND e.data >= :dataInicio AND e.data <= :dataFim";
		return manager.createQuery(jpql, Vale.class)
				.setParameter("entregador", entregador)
				.setParameter("dataInicio", dataInicio)
				.setParameter("dataFim", dataFim)
				.getResultList();
	}


	public void deletarVale(Vale vale) {
		String jpql = "delete from Vale e where e.id = :id ";
		manager.getTransaction().begin();
		manager.createQuery(jpql)
				.setParameter("id", vale.getId())
				.executeUpdate();
		manager.getTransaction().commit();
	}
}
