package br.com.don.view.admin;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.primefaces.PrimeFaces;

import br.com.don.enums.TipoVale;
import br.com.don.model.Acerto;
import br.com.don.model.Entrega;
import br.com.don.model.Entregador;
import br.com.don.model.Vale;
import br.com.don.service.EntregaService;
import br.com.don.service.EntregadorService;
import br.com.don.service.ValeService;
import br.com.don.util.Util;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Data;

@Data
@Named
@ViewScoped
public class AcertoView implements Serializable {

	private static final long serialVersionUID = 1L;

	private StringBuffer textoVale;

	private Vale vale;

	private Vale valeSelecionado;

	private List<Vale> vales;

	private List<TipoVale> tipoVale;

	private List<Entrega> entregas;

	private List<Entregador> entregadores;

	private List<Entregador> entregadoresDt;

	private List<Acerto> acertos;

	private final String QUEBRALINHA = System.lineSeparator();

	private Date dataSelecionada;

	private Date dataInicio;

	private Date dataFim;

	private LocalDate dataMovimento;

	private Entregador entregadorSelecionado;

	@Inject
	private EntregaService entregaService;

	@Inject
	private ValeService valeService;

	@Inject
	private EntregadorService entregadorService;


	@PostConstruct
	public void init() {
		entregas = new ArrayList<>();
		acertos = new ArrayList<>();
		entregadores = new ArrayList<>();

		entregadoresDt = entregadorService.listarPorNome();

		entregadorSelecionado = null;

		dataSelecionada = new Date();
		dataInicio = new Date();
		dataFim = new Date();
		dataMovimento = Util.converteLocalDate(dataSelecionada);

		vale = new Vale();

		tipoVale = Arrays.asList(TipoVale.values());

		vales = valeService.listarOrdenadoPorData();

		textoVale = null;
	}


	public void novoAcerto() {
		PrimeFaces.current().executeScript("PF('acertoDialog').show()");
	}


	public void realizarAcerto() {

		if(entregadorSelecionado.getPix().getChave() != null ) {

			try {
				acertos = new ArrayList<>();
				entregas = new ArrayList<>();

				entregas = entregaService.buscarPorEntregadorDataInicioDataFim((Entregador) entregadorSelecionado,
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

				textoVale = new StringBuffer();
				textoVale.append("*")
					.append(entregadorSelecionado.getNome())
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

						vales = valeService.buscarPorColaboradorDataTipo(entregadorSelecionado, ent.getData(),
							TipoVale.DINHEIRO);

						if (null != vales) {
							for (Vale vale : vales) {
								totalVale = vale.getValor().add(totalVale);
								valorTotalVales = vale.getValor().add(valorTotalVales);

							}
						}

						acerto.setValorValeDia(totalVale);
						acertos.add(acerto);
						textoVale.append(acerto.toString());

						valorTotalDiarias = acerto.getValorDiaria().add(valorTotalDiarias);
						dataComparacao = null;
						qtdeIfoodDia = 0;
						qtdeEntregaDia = 0;
						totalVale = new BigDecimal(0);
					}
				}

				// busca valor do saldo
				vales = valeService.buscarSaldo(entregadorSelecionado, Util.converteLocalDate(dataInicio));

				if (null != vales) {
					for (Vale vale : vales) {
						valorSaldo = vale.getValor().add(valorSaldo);
					}
				}

				BigDecimal valorTotalIfood = new BigDecimal(qtdeTotalIFood * 3.00);
				BigDecimal valorTotalSemDesconto = valorTotalDiarias.add(valorTotalEntregas).add(valorTotalIfood);
				BigDecimal valorTotalComDesconto = valorTotalSemDesconto.subtract(valorTotalVales).subtract(valorSaldo);

				entregadorSelecionado.setQtdTotalDias(qtdeTotalDias);
				entregadorSelecionado.setQtdEntregas(entregas.size());
				entregadorSelecionado.setValorTotalEntregas(valorTotalEntregas);
				entregadorSelecionado.setValorTotalIfood(valorTotalIfood);
				entregadorSelecionado.setValorTotalSemDesconto(valorTotalSemDesconto);
				entregadorSelecionado.setValorTotalComDesconto(valorTotalComDesconto);
				entregadorSelecionado.setValorTotalDiarias(valorTotalDiarias);
				entregadorSelecionado.setValorTotalVales(valorTotalVales);
				entregadorSelecionado.setValorSaldo(valorSaldo);

				textoVale.append("Total de Entregas: R$ ").append(entregadorSelecionado.getValorTotalEntregas())
					.append(QUEBRALINHA)
					.append("Total de Diárias: R$ ").append(entregadorSelecionado.getValorTotalDiarias())
					.append(QUEBRALINHA)
					.append("Total IFood: R$ ").append(entregadorSelecionado.getValorTotalIfood()).append(".00")
					.append(QUEBRALINHA)
					.append("Total Sem Desconto: R$ ").append(entregadorSelecionado.getValorTotalSemDesconto())
					.append(QUEBRALINHA)
					.append("Total de Vales: R$ ").append(entregadorSelecionado.getValorTotalVales())
					.append(QUEBRALINHA)
					.append("Saldo: R$ ").append(entregadorSelecionado.getValorSaldo())
					.append(QUEBRALINHA)
					.append("*RECEBER* R$: ")
					.append(entregadorSelecionado.getValorTotalComDesconto())
					.append(QUEBRALINHA).append("*OBRIGADO E DEUS ABENÇÕE*");

				/* Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				StringSelection selection = new StringSelection(texto.toString());
				clipboard.setContents(selection, null); */

				copyValeToClipboard();

			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Erro", e.getMessage()));
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Necessário cadastrar uma chave pix para continuar"));
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

		entregadores = entregadorService.listarEntregadoresPorDataInicioFim(Util.converteLocalDate(dataInicio),
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


	public void cancelar() {
		init();
	}


	public void copyValeToClipboard() {
		String texto = textoVale.toString();

		texto = texto.replace("\n", "\\n").replace("\r", "\\r").replace("'", "\\'");

		String script = "navigator.clipboard.writeText('" + texto + "');";

		PrimeFaces.current().executeScript(script);

		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Recibo copiado", "Recibo copiado para a área de transferência.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
}