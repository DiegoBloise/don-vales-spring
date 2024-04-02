package br.com.don.erp.service;

import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import br.com.don.erp.model.Entrega;
import br.com.don.erp.model.Entregador;
import br.com.don.erp.repository.EntregaRepository;
import br.com.don.erp.util.Util;

@Named
public class EntregaService implements Serializable {

	private static final long serialVersionUID = 1L;

	private final String NAO = "N";

	private final String DELIVERY = "D";

	@Inject
	private EntregaRepository repository;

	@Inject
	private EntregadorService entregadorService;


	public List<Entrega> listar() {
		return repository.findAll();
	}


	public List<Entrega> salvarLote(List<Entrega> lista) {

		for (Entrega entrega : lista) {
			repository.save(entrega);
		}
		return lista;
	}


	public List<Entrega> trataXML(InputStream inputStream, LocalDate dataMovimento) {

		List<Entrega> listaEntregas = new ArrayList<Entrega>();
		try {
			Workbook workbook = WorkbookFactory.create(inputStream);

			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> iterator = sheet.iterator();
			Row row = iterator.next();

			while (iterator.hasNext()) {
				row = iterator.next();
				try {
					if (row.getCell(3).getStringCellValue().equals(DELIVERY)
							&& row.getCell(11).getStringCellValue().equals(NAO)) {
						Entrega entrega = new Entrega();
						Double numPedido = row.getCell(0).getNumericCellValue();
						entrega.setPedido(numPedido.intValue());

						// TODO: e se houver mais entregadores com o mesmo nome ? talve uma caixa de seleção para o entregador certo...
						String nomeDoEntregador = row.getCell(14).getStringCellValue();
						Entregador entregador = entregadorService.buscarPorNome(nomeDoEntregador);

						if (entregador != null) {
							entrega.setEntregador(entregador);
						} else {
							entregador = new Entregador();
							entregador.setNome(nomeDoEntregador);
							entrega.setEntregador(entregador);
						}

						entrega.setData(dataMovimento);
						entrega.setValor(new BigDecimal(row.getCell(13).getNumericCellValue()));
						listaEntregas.add(entrega);
					}
				} catch (Exception e) {
					System.out.println("Falha ao ler registro: " + row.getRowNum());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listaEntregas;
	}


	public List<Entrega> trataXML2(InputStream inputStream) {

		List<Entrega> listaEntregas = new ArrayList<Entrega>();
		try {
			Workbook workbook = WorkbookFactory.create(inputStream);

			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> iterator = sheet.iterator();
			Row row = iterator.next();
			LocalDate dataMovimento = null;

			while (iterator.hasNext()) {
				row = iterator.next();

				if(dataMovimento == null) {
					dataMovimento = Util.converteDataHoraLocalDate(row.getCell(7).getStringCellValue());	
				}

				try {

					if (row.getCell(3).getStringCellValue().equals(DELIVERY)
							&& row.getCell(11).getStringCellValue().equals(NAO)) {
						Entrega entrega = new Entrega();
						Double numPedido = row.getCell(0).getNumericCellValue();
						entrega.setPedido(numPedido.intValue());

						// TODO: e se houver mais entregadores com o mesmo nome ? talve uma caixa de seleção para o entregador certo...
						String nomeDoEntregador = row.getCell(14).getStringCellValue();
						Entregador entregador = entregadorService.buscarPorNome(nomeDoEntregador);

						if (entregador != null) {
							entrega.setEntregador(entregador);
						} else {
							entregador = new Entregador();
							entregador.setNome(nomeDoEntregador);
							entrega.setEntregador(entregador);
						}

						LocalDate dataComparacao = Util.converteDataHoraLocalDate(row.getCell(7).getStringCellValue());


						if(numPedido < 10 && !dataComparacao.equals(dataMovimento)){
							dataMovimento = dataComparacao;
						}

						entrega.setData(dataMovimento);
						entrega.setValor(new BigDecimal(row.getCell(13).getNumericCellValue()));
						listaEntregas.add(entrega);
					}

				} catch (Exception e) {
					System.out.println("Erro ao ler xls " +  row.getRowNum() + " \n erro"+ e.getMessage());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return listaEntregas;

	}


	public List<Entrega> buscarPorEntregador(Entregador entregador){
		return repository.findAllByProperty("entregador", entregador);
	}


	public List<Entregador> listarEntregadoresPorData(LocalDate data){
		return repository.buscarEntregadoresPorData(data);
	}


	public List<Entregador> listarEntregadoresPorDataInicioFim(LocalDate dataInicio, LocalDate dataFim ){
		return repository.buscarEntregadoresPorDataInicioFim(dataInicio,dataFim);
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


	public Long buscarMovimento(LocalDate data) {
		return repository.buscarMovimento(data);
	}


	public Integer deleterMovimento(LocalDate data) {
		return repository.deletarMovimento(data);
	}
}
