package br.com.don.erp.service;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.inject.Inject;

import br.com.don.erp.enums.TipoVale;
import br.com.don.erp.model.Colaborador;
import br.com.don.erp.model.Vale;
import br.com.don.erp.repository.Vales;

public class ValeService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Vales vales;


	public List<Vale> listar() {
		return vales.listar();
	}


	public List<Vale> listarOrdenadoPorData() {
		return vales.listarOrdenadoPorData();
	}


	public List<Vale> listarOrdenadoPorId() {
		return vales.listarOrdenadoPorId();
	}


	public Vale salvar(Vale vale) {
		vales.salvar(vale);
		return vale;
	}


	public List<Vale> buscarPorEntregador(String entregador){
		return vales.buscarPorEntregador(entregador);
	}


	public List<Vale> buscarPorEntregadorDataTipo(String entregador, LocalDate data,TipoVale tipo){
		return vales.buscarPorEntregadorDataTipo(entregador, data, tipo);
	}


	public List<Vale> buscarPorData(LocalDate data){
		return vales.buscarPorData(data);
	}


	public List<Vale> buscarPorColaboradorDataInicioFim(Colaborador colaborador,LocalDate dataInicio,LocalDate dataFim){
		return vales.buscarPorColaboradorDataInicioFim(colaborador, dataInicio, dataFim);
	}


	public void deletarVale(Vale vale) {
		vales.deletarVale(vale);
	}

}
