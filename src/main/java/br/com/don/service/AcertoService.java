package br.com.don.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;

import br.com.don.util.Util;
import lombok.Data;

@Data
public class AcertoService implements Serializable {

	private static final long serialVersionUID = 1L;

	private LocalDate data;

	private Integer qtdeEntregasDia;

	private Integer qtdeIFood;

	private BigDecimal valorValeDia;

	private BigDecimal valorDiaria;

	private final String QUEBRALINHA ="\n";

	private final String TRACEJADO = "--------------------------------";


	public BigDecimal getValorDiaria() {
		boolean isFimDeSemana = false;
		if(data.getDayOfWeek().equals(DayOfWeek.FRIDAY) ||
			data.getDayOfWeek().equals(DayOfWeek.SATURDAY)||
			data.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
			isFimDeSemana = true;
		}

		if(isFimDeSemana) {
			if(qtdeEntregasDia > 40) {
				this.valorDiaria = new BigDecimal("50.00");
			}else if(qtdeEntregasDia > 30) {
				this.valorDiaria = new BigDecimal("45.00");
			}else if(qtdeEntregasDia > 20) {
				this.valorDiaria = new BigDecimal("40.00");
			}else if(qtdeEntregasDia > 9) {
				this.valorDiaria = new BigDecimal("35.00");
			}else {
				this.valorDiaria = new BigDecimal("0.00");
			}
		}else {
			if(qtdeEntregasDia > 9) {
				this.valorDiaria = new BigDecimal("35.00");
			}else {
				this.valorDiaria = new BigDecimal("0.00");
			}
		}

		return this.valorDiaria;
	}


	public String getDataFormatada() {
		return Util.dataSemanaFormatada(data);
	}


	public String toString() {
		StringBuffer buffer = new StringBuffer()
		.append("*").append(Util.diaDaSemana(data.getDayOfWeek()))
		.append(" ")
		.append(Util.localDateFormatado(this.data)).append("*")
		.append(QUEBRALINHA)
		.append("Qtde de entregas: ").append(this.qtdeEntregasDia)
		.append(QUEBRALINHA)
		.append("Valor da Diária: R$ ").append(getValorDiaria())
		.append(QUEBRALINHA)
		.append("Qtde de IFood: ").append(this.qtdeIFood)
		.append(QUEBRALINHA)
		.append("Vale: R$ ").append(this.valorValeDia)
		.append(QUEBRALINHA)
		.append(TRACEJADO)
		.append(QUEBRALINHA);

		return buffer.toString();

	}


	/* public void apagarMovimento() {
		Integer retorno = 0;
		retorno = uploadService.deleterMovimento(dataMovimento);
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Data do movimento apagada!",
						Util.localDateFormatado(dataMovimento) + System.lineSeparator() + retorno
								+ " registros foram apagados!"));
	}


    public void fileUpload(FileUploadEvent event) {

		try {

			Long verificaData = uploadService.buscarMovimento(dataMovimento);

			if (verificaData != null && verificaData > 0) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Já existe um movimento com a data selecionada!", Util.localDateFormatado(dataMovimento)));
			} else {
				UploadedFile file = event.getFile();

				Integer entregasSalvas = uploadService.trataXML2(file.getInputStream());

				entregas = uploadService.buscarPorData(dataMovimento);
				entregadores = uploadService.listarEntregadoresPorData(dataMovimento);

				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Foram salvos "+ (entregasSalvas + " registros"), Util.localDateFormatado(dataMovimento)));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
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

				//Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				//StringSelection selection = new StringSelection(texto.toString());
				//clipboard.setContents(selection, null);

				copyValeToClipboard();

			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Erro", e.getMessage()));
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Necessário cadastrar uma chave pix para continuar"));
		}
	}*/

/* 
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


	public void sendValeToWhatsapp() {
		was = new WhatsAppSender();

		if(was.setup()) {
			was.sendText(entregadorSelecionado.getTelefoneWhatsApp(), textoVale.toString());
		}
	} */
}
