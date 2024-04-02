package br.com.don.erp.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import br.com.don.erp.model.Colaborador;
import br.com.don.erp.repository.ColaboradorRepository;

@Named
public class ColaboradorService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ColaboradorRepository repository;


	public List<Colaborador> listar(){
		return repository.findAll();
	}


	public Colaborador buscar(Colaborador colaborador){
		return repository.find(colaborador);
	}


	public List<Colaborador> buscarPorNome(String nome){
		return repository.findAllByProperty("colaborador.nome", nome);
	}


	public void cadastrarColaborador(Colaborador colaborador) {
		repository.save(colaborador);
	}


	public void atualizarColaborador(Colaborador colaborador) {
		repository.update(colaborador);
	}


	public void removerColaborador(Colaborador colaborador) {
		repository.delete(colaborador);
	}


	public void removerColaboradores(List<Colaborador> colaboradores) {
		for (Colaborador colaborador : colaboradores) {
			this.removerColaborador(colaborador);
		}
	}
}
