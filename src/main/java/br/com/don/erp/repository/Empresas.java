package br.com.don.erp.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.don.erp.model.Empresa;

public class Empresas implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Empresa empresa;
	
	private List<Empresa> empresas;
	
	@Inject
	private EntityManager manager;
	
	
	public Empresa porId(Long id) {
		return empresa;
	}
	
	public List<Empresa> todas() {
		
		/*
		 * empresas = new ArrayList<Empresa>();
		 * 
		 * Empresa empresa = new Empresa(); empresa.setCnpj("15.380.748/0001-15");
		 * empresa.setRazaoSocial("H B Lucena da Silva Pizzaria ME");
		 * empresa.setNomeFantasia("HJG COMERCIO DE ALIMENTOS LTDA");
		 * 
		 * empresas.add(empresa);
		 */
		return manager.createQuery("from Empresa", Empresa.class).getResultList();

	}
	
	public List<Empresa> todasAtualidas() {
		
		empresas = new ArrayList<Empresa>();
		
		Empresa empresa = new Empresa();
		empresa.setCnpj("15.380.748/0001-15");
		empresa.setRazaoSocial("Atualizadas sem movimento");
		empresa.setNomeFantasia("Atualizadas sem movimento");
		
		empresas.add(empresa);
		return empresas;

	}
	
	public List<Empresa> todasAtualidas2() {
		
		empresas = new ArrayList<Empresa>();
		
		Empresa empresa = new Empresa();
		empresa.setCnpj("15.380.748/0001-15");
		empresa.setRazaoSocial("Atualizadas com movimento");
		empresa.setNomeFantasia("Atualizadas com movimento");
		
		empresas.add(empresa);
		return empresas;

	}
	
	public Empresa guardar(Empresa empresa) {
		return empresa;
	}
	
	public void remover(Empresa empresa) {
		//
	}

}