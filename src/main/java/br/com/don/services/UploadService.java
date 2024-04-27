package br.com.don.services;

import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.don.models.Entrega;
import br.com.don.models.Entregador;
import br.com.don.util.Util;

@Service
public class UploadService implements Serializable {

	private static final long serialVersionUID = 1L;

	private final String NAO = "N";

	private final String DELIVERY = "D";

	@Autowired
	private EntregadorService entregadorService;

	@Autowired
	private EntregaService entregaService;


	public List<Entrega> buscarPorData(LocalDate data){
		return entregaService.buscarPorData(data);
	}


	public List<Entregador> listarEntregadoresPorData(LocalDate data){
		return entregadorService.listarEntregadoresPorData(data);
	}


	public Long buscarMovimento(LocalDate data) {
		return entregaService.buscarMovimento(data);
	}


	public Integer deleterMovimento(LocalDate data) {
		return entregaService.deleterMovimento(data);
	}


	public List<Entrega> salvarLote(List<Entrega> lista) {
		return entregaService.salvarLote(lista);
	}


	public Integer trataXML2(InputStream inputStream) {

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

					if (row.getCell(3).getStringCellValue().equals(DELIVERY) &&
						row.getCell(11).getStringCellValue().equals(NAO)) {

						Entrega entrega = new Entrega();
						Double numPedido = row.getCell(0).getNumericCellValue();
						entrega.setPedido(numPedido.intValue());

						LocalDate dataComparacao = Util.converteDataHoraLocalDate(row.getCell(7).getStringCellValue());

						if(numPedido < 10 && !dataComparacao.equals(dataMovimento)){
							dataMovimento = dataComparacao;
						}

						entrega.setData(dataMovimento);
						entrega.setValor(new BigDecimal(row.getCell(13).getNumericCellValue()));

						////////////////////////////////////////////////////////////////////////////////////////////////////////////////
						// TODO: e se houver mais entregadores com o mesmo nome ? talve uma caixa de seleção para o entregador certo...
						String nomeDoEntregador = row.getCell(14).getStringCellValue();
						checkEntregador(nomeDoEntregador, entrega);
						////////////////////////////////////////////////////////////////////////////////////////////////////////////////

						checkEntrega(entrega, listaEntregas);
					}

				} catch (Exception e) {
					System.out.println("Erro ao ler xls " +  row.getRowNum() + " \n erro"+ e.getMessage());
					return null;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		try {
			entregaService.salvarLote(listaEntregas);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return listaEntregas.size();
	}


	public void checkEntregador(String nomeDoEntregador, Entrega entrega) {
		try {
			Entregador entregador = entregadorService.buscarPorNome(nomeDoEntregador);
			try {
				if (entregador != null) {
					entregador.adicionarEntrega(entrega);
				} else {
					entregador = new Entregador();
					entregador.setNome(nomeDoEntregador);
					entregador.adicionarEntrega(entrega);
				}
				entregadorService.salvarEntregador(entregador);
			} catch (Exception e) {
				System.out.println("checkEntregador1: " + e);
			}
		} catch (Exception e) {
			System.out.println("checkEntregador2: " + e);
		}
	}


	public void checkEntrega(Entrega entrega, List<Entrega> entregas) {
		try {
			List<Entrega> entregaExistente = entregaService.buscarPorPedidoDataEntregador(entrega.getPedido(), entrega.getData(), entrega.getEntregador());

			if (entregaExistente != null) {
			} else {
				entregas.add(entrega);
			}
		} catch (Exception e) {
			System.out.println("checkEntrega: " + e);
		}
	}


	/* public List<Entrega> trataXML(InputStream inputStream, LocalDate dataMovimento) {

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


						////////////////////////////////////////////////////////////////////////////////////////////////////////////////
						// TODO: e se houver mais entregadores com o mesmo nome ? talve uma caixa de seleção para o entregador certo...
						String nomeDoEntregador = row.getCell(14).getStringCellValue();
						checkEntregador(nomeDoEntregador, entrega);
						////////////////////////////////////////////////////////////////////////////////////////////////////////////////


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
	} */
}
