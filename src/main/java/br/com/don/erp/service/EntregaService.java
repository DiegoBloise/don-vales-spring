package br.com.don.erp.service;

import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import br.com.don.erp.model.Entrega;
import br.com.don.erp.repository.Entregas;
import br.com.don.erp.util.Util;

public class EntregaService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private final String NAO = "N";
	
	private final String DELIVERY = "D";

	@Inject
	private Entregas entregas;

	public List<Entrega> listar() {
		return entregas.listar();
	}

	public List<Entrega> salvarLote(List<Entrega> lista) {

		for (Entrega entrega : lista) {
			entregas.salvar(entrega);
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
						entrega.setEntregador(row.getCell(14).getStringCellValue());
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
						entrega.setEntregador(row.getCell(14).getStringCellValue());
						
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
	
	public List<Entrega> buscarPorEntregador(String entregador){
		return entregas.buscarPorEntregador(entregador);
	}
	
	public List<String> listarEntregadoresporData(LocalDate data){
		return entregas.buscarEntregadoresporData(data);
	}
	
	public List<String> listarEntregadoresporDataInicioFim(LocalDate dataInicio, LocalDate dataFim ){
		return entregas.buscarEntregadoresporDataInicioFim(dataInicio,dataFim);
	}
	
	public List<Entrega> buscarPorEntregadorData(String entregador, LocalDate data){
		return entregas.buscarPorEntregadorData(entregador, data);
	}
	
	public List<Entrega> buscarPorEntregadorDataInicioDataFim(String entregador, LocalDate dataInicio, LocalDate datafim){
		return entregas.buscarPorEntregadorDataInicioDataFim(entregador, dataInicio, datafim);
	}
	
	public List<Entrega> buscarPorData(LocalDate data){
		return entregas.buscarPorData(data);
	}
	
	public Long buscarMovimento(LocalDate data) {
		return entregas.buscarMovimento(data);
	}
	
	public Integer deleterMovimento(LocalDate data) {
		return entregas.deletarMovimento(data);
	}
}
