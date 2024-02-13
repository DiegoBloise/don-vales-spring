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

import br.com.don.erp.model.Acerto;
import br.com.don.erp.model.Entrega;
import br.com.don.erp.model.TipoVale;
import br.com.don.erp.model.Vale;
import br.com.don.erp.service.EntregaService;
import br.com.don.erp.service.ValeService;
import br.com.don.erp.util.Util;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class EntregaView implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Entrega> entregas;

	private Integer qtdeEntregas;

	@Getter
	private BigDecimal valorTotalEntregas = new BigDecimal("0.00");

	@Getter
	private BigDecimal valorSaldo = new BigDecimal("0.00");

	@Getter
	private BigDecimal valorTotalDiarias = new BigDecimal("0.00");

	@Getter
	private BigDecimal valorTotalIfood = new BigDecimal("0.00");

	@Getter
	private BigDecimal valorTotalVales = new BigDecimal("0.00");

	@Getter
	private BigDecimal valorTotalSemDesconto = new BigDecimal("0.00");

	@Getter
	private BigDecimal valorTotalComDesconto = new BigDecimal("0.00");

	private Integer qtdeTotalIFood = 0;

	@Getter
	@Setter
	private String entregadorSelecionado;

	private Date dataSelecionada;

	private LocalDate dataMovimento;

	@Getter
	private List<String> entregadores;

	@Getter
	private Integer qtdeTotalDias;

	@Getter
	@Setter
	private Double valorVale;

	@Getter
	@Setter
	private List<Vale> vales;

	@Getter
	private List<TipoVale> tipoVale;

	@Inject
	private EntregaService entregaService;

	@Inject
	private ValeService valeService;

	@Getter
	@Setter
	private Vale vale;

	@Setter
	@Getter
	private Date dataInicio;

	@Setter
	@Getter
	private Date dataFim;

	@Getter
	private List<Acerto> acertos;
	
	@Setter
	@Getter
	private Vale valeSelecionado;
	
	private final String QUEBRALINHA = System.lineSeparator();

	@PostConstruct
	public void init() {
		entregas = new ArrayList<Entrega>();
		dataSelecionada = new Date();
		dataMovimento = Util.converteLocalDate(dataSelecionada);
		tipoVale = Arrays.asList(TipoVale.values());
		vale = new Vale();
		entregadores = entregaService.listarEntregadoresporData(dataMovimento);
		vales = valeService.listarOrdenadoPorData();
		dataInicio = new Date();
		dataFim = new Date();
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
		
		if (entregadorSelecionado != null && !entregadorSelecionado.isEmpty()) {
		
		try {	
			entregas = new ArrayList<>();
			entregas = entregaService.buscarPorEntregadorDataInicioDataFim(entregadorSelecionado,
					Util.converteLocalDate(dataInicio), Util.converteLocalDate(dataFim));

			LocalDate dataComparacao = null;
			int i = 0;
			int qtdeIfoodDia = 0;
			int qtdeEntregaDia = 0;
			acertos = new ArrayList<>();
			qtdeEntregas = entregas.size();
			valorTotalEntregas = new BigDecimal(0.0);
			BigDecimal totalVale = new BigDecimal(0);
			qtdeTotalDias = 0;
			valorSaldo = new BigDecimal(0);
			valorTotalDiarias = new BigDecimal(0);
			valorTotalVales = new BigDecimal(0);
			valorTotalSemDesconto = new BigDecimal(0);
			valorTotalComDesconto = new BigDecimal(0);

			qtdeTotalIFood = 0;
			
			StringBuffer texto = new StringBuffer();
			texto.append("*").append(entregadorSelecionado).append("*").append(QUEBRALINHA);

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

					vales = valeService.buscarPorEntregadorDataTipo(entregadorSelecionado, ent.getData(),
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
					vales = valeService.buscarPorEntregadorDataTipo(entregadorSelecionado, ent.getData(),
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
			valorTotalIfood = new BigDecimal(qtdeTotalIFood * 3.00);
			valorTotalSemDesconto = valorTotalDiarias.add(valorTotalEntregas).add(valorTotalIfood);
			valorTotalComDesconto = valorTotalSemDesconto.subtract(valorTotalVales).subtract(valorSaldo);
			
			texto.append("Total de Entregas: R$ ").append(valorTotalEntregas)
			.append(QUEBRALINHA);
			texto.append("Total de Diárias: R$ ").append(valorTotalDiarias)
			.append(QUEBRALINHA)
			.append("Total IFood: R$ ").append(valorTotalIfood).append(".00")
			.append(QUEBRALINHA)
			.append("Total Sem Desconto: R$ ").append(valorTotalSemDesconto)
			.append(QUEBRALINHA)
			.append("Total de Vales: R$ ").append(valorTotalVales)
			.append(QUEBRALINHA)
			.append("Saldo: R$ ").append(valorSaldo)
			.append(QUEBRALINHA)
			.append("*RECEBER* R$: ")
			.append(valorTotalComDesconto)
			.append(QUEBRALINHA).append("*OBRIGADO E DEUS ABENÇÕE*");
			
			
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				StringSelection selection = new StringSelection(texto.toString());
				clipboard.setContents(selection, null);
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Erro", e.getMessage()));
			}
			
			
		}else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Selecione Entregador", "Entregador deve ser selecionado"));
		}
			
	}
	
	public void buscarVales() {
		vales = valeService.buscarPorEntregadorDataInicioFim(vale.getEntregador(), dataMovimento, LocalDate.now());
	}
	
	public void apagarVale() {
		valeService.deletarVale(valeSelecionado);
		vales = valeService.listarOrdenadoPorData();
	}

	public void buscarEntregadoresAcerto() {
		entregadores = entregaService.listarEntregadoresporDataInicioFim(Util.converteLocalDate(dataInicio),
				Util.converteLocalDate(dataFim));
	}

	public List<Entrega> getEntregas() {
		return entregas;
	}

	public Integer getQtdeEntregas() {
		return qtdeEntregas;
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

	public void exemplos() {

		System.out.println("Aqui");
		// formatar data
		// DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		// LocalDate localDate = LocalDate.parse(data,formato);
		// formato.format(localDate);

		// Messages
		// FacesContext facesContext = FacesContext.getCurrentInstance();
		// facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
		// "Campos Obrigatórios", "Valor, Tipo e Entregador" ));
	}

	public Date getDataSelecionada() {
		return dataSelecionada;
	}

	public void setDataSelecionada(Date dataSelecionada) {
		this.dataSelecionada = dataSelecionada;
	}

	public void exibirEntregadorSelecionado() {
		if (!entregadorSelecionado.equals("")) {
			entregas = entregaService.buscarPorEntregadorData(entregadorSelecionado, dataMovimento);
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
	}

}
