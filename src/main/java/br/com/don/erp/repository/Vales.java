package br.com.don.erp.repository;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.don.erp.enums.TipoVale;
import br.com.don.erp.model.Colaborador;
import br.com.don.erp.model.Vale;

public class Vales implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;


	public List<Vale> listar(){
		return manager.createQuery("from Vale", Vale.class).getResultList();
	}


	public List<Vale> listarOrdenadoPorData(){
		String jpql = "select v from Vale v order by v.data desc, id";
		return manager.createQuery(jpql, Vale.class).getResultList();
	}


	public List<Vale> listarOrdenadoPorId(){
		String jpql = "select v from Vale v order by v.id desc";
		return manager.createQuery(jpql, Vale.class).getResultList();
	}


	public void salvar(Vale vale) {
		manager.getTransaction().begin();
		manager.merge(vale);
		manager.getTransaction().commit();
	}


	public List<Vale> buscarPorColaborador(Colaborador colaborador){
		String jpql = "select v from Vale v where v.colaborador = :colaborador order by v.data desc";
		return manager.createQuery(jpql, Vale.class).setParameter("colaborador", colaborador)
				.getResultList();
	}


	public List<Vale> buscarPorColaboradorDataTipo(Colaborador colaborador, LocalDate data, TipoVale tipo){
		String jpql = "select v from Vale v where v.colaborador = :colaborador AND v.data = :data AND v.tipo = :tipo";
		return manager.createQuery(jpql, Vale.class)
				.setParameter("colaborador", colaborador)
				.setParameter("data", data)
				.setParameter("tipo", tipo)
				.getResultList();
	}


	public List<Vale> buscarPorData(LocalDate data){
		String jpql = "select v from Vale v where v.data = :data";
		return manager.createQuery(jpql, Vale.class).setParameter("data", data)
				.getResultList();
	}


	public List<Vale> buscarPorColaboradorDataInicioFim(Colaborador colaborador, LocalDate dataInicio, LocalDate dataFim){
		String jpql = "select v from Vale v where v.colaborador = :colaborador AND v.data >= :dataInicio AND v.data <= :dataFim";
		return manager.createQuery(jpql, Vale.class)
				.setParameter("colaborador", colaborador)
				.setParameter("dataInicio", dataInicio)
				.setParameter("dataFim", dataFim)
				.getResultList();
	}


	public void deletarVale(Vale vale) {
		String jpql = "delete from Vale v where v.id = :id ";
		manager.getTransaction().begin();
		manager.createQuery(jpql)
				.setParameter("id", vale.getId())
				.executeUpdate();
		manager.getTransaction().commit();
	}
}
