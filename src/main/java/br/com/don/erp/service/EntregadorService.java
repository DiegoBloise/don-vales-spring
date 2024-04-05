package br.com.don.erp.service;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import br.com.don.erp.model.Entregador;
import br.com.don.erp.repository.EntregadorRepository;

@Named
public class EntregadorService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntregadorRepository repository;


	public List<Entregador> listar(){
		return repository.findAll();
	}


	public List<Entregador> listarPorNome() {
		return repository.listarPorNome();
	}


	public Entregador buscar(Entregador entregador){
		return repository.find(entregador);
	}


	public Entregador buscarPorId(Long id) {
		return repository.findById(id);
	}


	public Entregador buscarPorNome(String nome){
		return repository.findByProperty("nome", nome);
	}


	public void cadastrarEntregador(Entregador entregador) {
		repository.save(entregador);
	}


	public void removerEntregador(Entregador entregador) {
		repository.delete(entregador);
	}


	public void removerEntregadores(List<Entregador> entregadores) {
		for (Entregador entregador : entregadores) {
			this.removerEntregador(entregador);
		}
	}


	public List<Entregador> listarEntregadoresPorData(LocalDate data){
		return repository.listarEntregadoresPorData(data);
	}


	public List<Entregador> listarEntregadoresPorDataInicioFim(LocalDate dataInicio, LocalDate dataFim ){
		return repository.listarEntregadoresPorDataInicioFim(dataInicio,dataFim);
	}
}
