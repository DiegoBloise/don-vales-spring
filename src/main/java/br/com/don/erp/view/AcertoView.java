package br.com.don.erp.view;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.file.UploadedFile;

import br.com.don.erp.enums.TipoVale;
import br.com.don.erp.model.Acerto;
import br.com.don.erp.model.Colaborador;
import br.com.don.erp.model.Entrega;
import br.com.don.erp.model.Vale;
import br.com.don.erp.service.ColaboradorService;
import br.com.don.erp.service.EntregaService;
import br.com.don.erp.service.ValeService;
import br.com.don.erp.util.Util;
import lombok.Data;

@Data
@Named
@ViewScoped
public class AcertoView implements Serializable {

	private static final long serialVersionUID = 1L;

	private Vale vale;

	private Vale valeSelecionado;

	private List<Vale> vales;

	private List<TipoVale> tipoVale;

	private List<Entrega> entregas;

	private List<Acerto> acertos;

	private List<String> entregadores;

	private final String QUEBRALINHA = System.lineSeparator();

	private String nomeColaborador;

	private Double valorVale;

	private Date dataSelecionada;

	private Date dataInicio;

	private Date dataFim;

	private LocalDate dataMovimento;

	private Colaborador colaboradorSelecionado;

	@Inject
	private EntregaService entregaService;

	@Inject
	private ValeService valeService;

	@Inject
	private ColaboradorService colaboradorService;


	@PostConstruct
	public void init() {
		entregas = new ArrayList<>();
		acertos = new ArrayList<>();

		dataSelecionada = new Date();
		dataInicio = new Date();
		dataFim = new Date();
		dataMovimento = Util.converteLocalDate(dataSelecionada);

		vale = new Vale();

		tipoVale = Arrays.asList(TipoVale.values());

		nomeColaborador = null;

		vales = valeService.listarOrdenadoPorData();
		entregadores = entregaService.listarEntregadoresporData(dataMovimento);
	}


	public void fileUpload(FileUploadEvent event) {

		try {

			Long verificaData = entregaService.buscarMovimento(dataMovimento);

			if (verificaData != null && verificaData > 0) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Já existe um movimento com a data selecionada!", Util.localDateFormatado(dataMovimento)));

			} else {
				UploadedFile file = event.getFile();
				List<Entrega> salvar = entregaService.trataXML2(file.getInputStream());

				entregaService.salvarLote(salvar);
				entregas = entregaService.buscarPorData(dataMovimento);
				entregadores = entregaService.listarEntregadoresporData(dataMovimento);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Foram salvos "+ (salvar.size() + " registros"), Util.localDateFormatado(dataMovimento)));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void onDateSelect(SelectEvent<Date> event) {
		dataMovimento = dataSelecionada.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		entregadores = entregaService.listarEntregadoresporData(dataMovimento);
		/*
		 * dataMovimento =
		 * dataSelecionada.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		 * System.out.println("Local date " + dataMovimento);
		 * System.out.println(getDataMovimentoFormatado()); FacesContext facesContext =
		 * FacesContext.getCurrentInstance(); SimpleDateFormat format = new
		 * SimpleDateFormat("dd/MM/yyyy"); facesContext.addMessage(null, new
		 * FacesMessage(FacesMessage.SEVERITY_INFO, "Data Selecioanda ",
		 * format.format(event.getObject())));
		 */
		// entregas = entregaService.buscarPorEntregador(getEntregadorSelecionado());
		// entregadores = entregaService.listarEntregadores();

	}


	public void salvarVale() {
		vale.setData(Util.converteLocalDate(dataSelecionada));
		vale.setValor(new BigDecimal(valorVale));
		valeService.salvar(vale);
		vale = new Vale();
		valorVale = null;
		vales = valeService.listarOrdenadoPorData();
		entregadores = entregaService.listarEntregadoresporData(dataMovimento);
	}


	public void realizarAcerto() {

		if (nomeColaborador != null && !nomeColaborador.isEmpty()) {

			try {
				colaboradorSelecionado = colaboradorService.buscarPorNome(nomeColaborador);

				acertos = new ArrayList<>();
				entregas = new ArrayList<>();

				entregas = entregaService.buscarPorEntregadorDataInicioDataFim(colaboradorSelecionado.getNome(),
					Util.converteLocalDate(dataInicio), Util.converteLocalDate(dataFim));

				LocalDate dataComparacao = null;

				int i = 0;
				int qtdeIfoodDia = 0;
				int qtdeEntregaDia = 0;
				int qtdeTotalDias = 0;
				int qtdeTotalIFood = 0;

				BigDecimal valorTotalEntregas = new BigDecimal(0.0);
				BigDecimal totalVale = new BigDecimal(0);
				BigDecimal valorTotalDiarias = new BigDecimal(0);
				BigDecimal valorTotalVales = new BigDecimal(0);
				BigDecimal valorSaldo = new BigDecimal(0);

				StringBuffer texto = new StringBuffer();
				texto.append("*")
					.append(colaboradorSelecionado.getNome())
					.append("*")
					.append(QUEBRALINHA);

				for (Entrega ent : entregas) {
					i++;

					if (null == dataComparacao) {
						dataComparacao = ent.getData();
						++qtdeTotalDias;
					}

					if (ent.getValor().compareTo(new BigDecimal(0)) == 0) {
						++qtdeIfoodDia;
						qtdeTotalIFood++;
					}

					qtdeEntregaDia++;

					valorTotalEntregas = ent.getValor().add(valorTotalEntregas);

					if (i == entregas.size() || !dataComparacao.equals(entregas.get(i).getData())) {
						Acerto acerto = new Acerto();

						acerto.setData(ent.getData());
						acerto.setQtdeEntregasDia(qtdeEntregaDia);
						acerto.setQtdeIFood(qtdeIfoodDia);

						vales = valeService.buscarPorColaboradorDataTipo(colaboradorSelecionado, ent.getData(),
							TipoVale.DINHEIRO);

						if (null != vales) {
							for (Vale vale : vales) {
								totalVale = vale.getValor().add(totalVale);
								valorTotalVales = vale.getValor().add(valorTotalVales);

							}
						}

						acerto.setValorValeDia(totalVale);
						acertos.add(acerto);
						texto.append(acerto.toString());

						// busca valor do saldo
						vales = valeService.buscarPorColaboradorDataTipo(
							colaboradorSelecionado,
							ent.getData(),
							TipoVale.SALDO);

						if (null != vales) {
							for (Vale vale : vales) {
								valorSaldo = vale.getValor().add(valorSaldo);
							}
						}

						valorTotalDiarias = acerto.getValorDiaria().add(valorTotalDiarias);
						dataComparacao = null;
						qtdeIfoodDia = 0;
						qtdeEntregaDia = 0;
						totalVale = new BigDecimal(0);
					}
				}

				BigDecimal valorTotalIfood = new BigDecimal(qtdeTotalIFood * 3.00);
				BigDecimal valorTotalSemDesconto = valorTotalDiarias.add(valorTotalEntregas).add(valorTotalIfood);
				BigDecimal valorTotalComDesconto = valorTotalSemDesconto.subtract(valorTotalVales).subtract(valorSaldo);

				colaboradorSelecionado.setQtdTotalDias(qtdeTotalDias);
				colaboradorSelecionado.setQtdEntregas(entregas.size());
				colaboradorSelecionado.setValorTotalEntregas(valorTotalEntregas);
				colaboradorSelecionado.setValorTotalIfood(valorTotalIfood);
				colaboradorSelecionado.setValorTotalSemDesconto(valorTotalSemDesconto);
				colaboradorSelecionado.setValorTotalComDesconto(valorTotalComDesconto);
				colaboradorSelecionado.setValorTotalDiarias(valorTotalDiarias);
				colaboradorSelecionado.setValorTotalVales(valorTotalVales);
				colaboradorSelecionado.setValorSaldo(valorSaldo);

				texto.append("Total de Entregas: R$ ").append(colaboradorSelecionado.getValorTotalEntregas())
					.append(QUEBRALINHA)
					.append("Total de Diárias: R$ ").append(colaboradorSelecionado.getValorTotalDiarias())
					.append(QUEBRALINHA)
					.append("Total IFood: R$ ").append(colaboradorSelecionado.getValorTotalIfood()).append(".00")
					.append(QUEBRALINHA)
					.append("Total Sem Desconto: R$ ").append(colaboradorSelecionado.getValorTotalSemDesconto())
					.append(QUEBRALINHA)
					.append("Total de Vales: R$ ").append(colaboradorSelecionado.getValorTotalVales())
					.append(QUEBRALINHA)
					.append("Saldo: R$ ").append(colaboradorSelecionado.getValorSaldo())
					.append(QUEBRALINHA)
					.append("*RECEBER* R$: ")
					.append(colaboradorSelecionado.getValorTotalComDesconto())
					.append(QUEBRALINHA).append("*OBRIGADO E DEUS ABENÇÕE*");

				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				StringSelection selection = new StringSelection(texto.toString());
				clipboard.setContents(selection, null);

			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Erro", e.getMessage()));
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Selecione Entregador", "Entregador deve ser selecionado"));
		}
	}


	public void buscarVales() {
		vales = valeService.buscarPorColaboradorDataInicioFim(vale.getColaborador(), dataMovimento, LocalDate.now());
	}


	public void apagarVale() {
		valeService.deletarVale(valeSelecionado);
		vales = valeService.listarOrdenadoPorData();
	}


	public void buscarEntregadoresAcerto() {
		FacesMessage msg;

		entregadores = entregaService.listarEntregadoresporDataInicioFim(Util.converteLocalDate(dataInicio),
			Util.converteLocalDate(dataFim));

		if (entregadores.isEmpty()) {
			msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Nenhum entregador encontrado", "Não foi possível encontrar entregadores no período especificado");
		} else {
			msg = new FacesMessage(FacesMessage.SEVERITY_INFO, entregadores.size() + " entregadores encontrados", "Selecione um entregador para prosseguir");
		}

		FacesContext.getCurrentInstance().addMessage(null, msg);
	}


	public List<Entrega> getEntregas() {
		return entregas;
	}


	public String getDataMovimentoFormatado() {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		return format.format(this.dataSelecionada);
	}


	public void apagarMovimento() {
		Integer retorno = 0;
		retorno = entregaService.deleterMovimento(dataMovimento);
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Data do movimento apagada!",
						Util.localDateFormatado(dataMovimento) + System.lineSeparator() + retorno
								+ " registros foram apagados!"));
	}


	public void cancelar() {
		init();
	}


	/* public void exemplos() {

		System.out.println("Aqui");
		// formatar data
		// DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		// LocalDate localDate = LocalDate.parse(data,formato);
		// formato.format(localDate);

		// Messages
		// FacesContext facesContext = FacesContext.getCurrentInstance();
		// facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
		// "Campos Obrigatórios", "Valor, Tipo e Entregador" ));
	} */


	/* public void exibirEntregadorSelecionado() {
		if (!colaboradorSelecionado.getNome().equals("")) {
			entregas = entregaService.buscarPorEntregadorData(colaboradorSelecionado.getNome(), dataMovimento);
			qtdeEntregas = entregas.size();
			qtdeTotalDias = 0;
			// qtdeIFood = 0;
			valorTotalEntregas = new BigDecimal(0.0);
			for (Entrega entrega : entregas) {
				if (entrega.getValor().compareTo(new BigDecimal("0")) == 0) {
					// qtdeIFood++;
				}
				valorTotalEntregas = valorTotalEntregas.add(entrega.getValor());
				entrega.setQtde(++qtdeTotalDias);
			}

		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Selecione Entregador", "Entregador deve ser selecionado"));
		}
	} */
}
