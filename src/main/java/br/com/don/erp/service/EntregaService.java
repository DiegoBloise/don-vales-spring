package br.com.don.erp.service;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import br.com.don.erp.model.Entrega;
import br.com.don.erp.model.Entregador;
import br.com.don.erp.repository.EntregaRepository;

@Named
public class EntregaService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntregaRepository repository;


	public List<Entrega> listar() {
		return repository.findAll();
	}


	public Entrega buscarPorPedidoDataEntregador(Integer pedido, LocalDate data, Entregador entregador) {
		return repository.buscarPorPedidoDataEntregador(pedido, data, entregador);
	}


	public List<Entrega> buscarPorEntregador(Entregador entregador){
		return repository.findAllByProperty("entregador", entregador);
	}

	public List<Entrega> buscarPorEntregadorData(Entregador entregador, LocalDate data){
		return repository.buscarPorEntregadorData(entregador, data);
	}


	public List<Entrega> buscarPorEntregadorDataInicioDataFim(Entregador entregador, LocalDate dataInicio, LocalDate datafim){
		return repository.buscarPorEntregadorDataInicioDataFim(entregador, dataInicio, datafim);
	}


	public List<Entrega> buscarPorData(LocalDate data){
		return repository.buscarPorData(data);
	}


	public List<Entrega> salvarLote(List<Entrega> lista) {

		for (Entrega entrega : lista) {
			repository.save(entrega);
		}
		return lista;
	}


	public Integer deleterMovimento(LocalDate data) {
		return repository.deletarMovimento(data);
	}


	public Long buscarMovimento(LocalDate data) {
		return repository.buscarMovimento(data);
	}
}
