package br.com.don.erp.service;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import br.com.don.erp.enums.TipoVale;
import br.com.don.erp.model.Colaborador;
import br.com.don.erp.model.Vale;
import br.com.don.erp.repository.ValeRepository;

@Named
public class ValeService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ValeRepository repository;


	public List<Vale> listar() {
		return repository.findAll();
	}


	public List<Vale> listarOrdenadoPorData() {
		return repository.listarOrdenadoPorData();
	}


	public List<Vale> listarOrdenadoPorId() {
		return repository.listarOrdenadoPorId();
	}


	public void cadastrarVale(Vale vale) {
		repository.save(vale);
	}


	public void atualizarVale(Vale vale) {
		repository.update(vale);
	}


	public List<Vale> buscarPorColaborador(Colaborador colaborador){
		return repository.findAllByProperty("colaborador", colaborador);
	}


	public List<Vale> buscarPorColaboradorDataTipo(Colaborador colaborador, LocalDate data,TipoVale tipo){
		return repository.buscarPorColaboradorDataTipo(colaborador, data, tipo);
	}


	public List<Vale> buscarPorData(LocalDate data){
		return repository.buscarPorData(data);
	}


	public List<Vale> buscarPorColaboradorDataInicioFim(Colaborador colaborador,LocalDate dataInicio,LocalDate dataFim){
		return repository.buscarPorColaboradorDataInicioFim(colaborador, dataInicio, dataFim);
	}


	public void deletarVale(Vale vale) {
		repository.delete(vale);
	}

}
