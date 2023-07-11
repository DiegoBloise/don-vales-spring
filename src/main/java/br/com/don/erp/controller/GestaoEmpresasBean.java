package br.com.don.erp.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.don.erp.model.Empresa;
import br.com.don.erp.repository.Empresas;

@Named
@ViewScoped
public class GestaoEmpresasBean implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Inject
	private Empresas empresas;
	
	private List<Empresa> todasEmpresas;
	
	public void consultar() {
		todasEmpresas = empresas.todas();
	}
	
	public void adicionar() {
		todasEmpresas = empresas.todasAtualidas();
	}
	
	public void adicionar2() {
		todasEmpresas = empresas.todasAtualidas2();
	}

	public List<Empresa> getTodasEmpresas() {
		return todasEmpresas;
	}
	
	public String teste() {
		return "TESTANDO";
	}

}
