package br.com.don.erp.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import br.com.don.erp.model.Colaborador;

public class Colaboradores implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;


	public List<Colaborador> listar(){
		return manager.createQuery("from Colaborador order by Nome", Colaborador.class).getResultList();
	}


	public void salvar(Colaborador colaborador) {
		manager.getTransaction().begin();
		manager.merge(colaborador);
		manager.getTransaction().commit();
	}


	public Colaborador buscar(Colaborador colaborador){

		try {
			String jpql = "select e from Colaborador e where e.nome = :nome";
			return manager.createQuery(jpql, Colaborador.class).setParameter("nome", colaborador.getNome())
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}

	}


	public Colaborador buscarPorNome(String nome){

		try {
			String jpql = "select c from Colaborador c where c.nome = :nome";
			return manager.createQuery(jpql, Colaborador.class).setParameter("nome", nome)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}

	}


	public Integer deletar(Colaborador colaborador) {
		String jpql = "delete from Colaborador e where e.nome = :nome";
		Integer retorno; 
		manager.getTransaction().begin();
		retorno = manager.createQuery(jpql).setParameter("nome", colaborador.getNome()).executeUpdate();
		manager.getTransaction().commit();

		return retorno;
	}
}
