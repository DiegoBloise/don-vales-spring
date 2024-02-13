package br.com.don.erp.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import br.com.don.erp.model.Colaborador;
import br.com.don.erp.repository.Colaboradores;

public class ColaboradorService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Colaboradores colaboradores;


	public List<Colaborador> listar(){
		return colaboradores.listar();
	}


	public void salvar(Colaborador colaborador) {
		colaboradores.salvar(colaborador);
	}


	public Integer deletar(Colaborador colaborador) {
		return colaboradores.deletar(colaborador);
	}


	public Colaborador buscar(Colaborador colaborador){
		return colaboradores.buscar(colaborador);
	}


	public List<Colaborador> getColaboradores() {
		return colaboradores.listar();
	}


	public void cadastrarColaborador(Colaborador colaborador) {
		colaboradores.salvar(colaborador);
	}


	public void removerColaborador(Colaborador colaborador) {
		colaboradores.deletar(colaborador);
	}


	public void removerColaboradores(List<Colaborador> colaboradores) {
		for (Colaborador colaborador : colaboradores) {
			this.removerColaborador(colaborador);
		}
	}
}
