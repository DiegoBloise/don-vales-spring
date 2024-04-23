package br.com.don.services;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.don.dtos.EntregadorDto;
import br.com.don.enums.TipoVale;
import br.com.don.mappers.EntregadorMapper;
import br.com.don.models.Entrega;
import br.com.don.models.Entregador;
import br.com.don.models.Vale;
import br.com.don.repositories.EntregadorRepository;
import br.com.don.util.Acerto;
import br.com.don.util.Util;

@Service
public class EntregadorService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private EntregadorRepository repository;

	@Autowired
	private EntregadorMapper mapper;

	@Autowired
	private EntregaService entregaService;

	@Autowired
	private ValeService valeService;


	public List<EntregadorDto> listar(){
		return repository.findAll().stream()
            .map(entregaodor -> {
                return mapper.toDto(entregaodor);
            })
            .collect(Collectors.toList());
	}


	public List<Entregador> listarPorNome() {
		return repository.listarPorNome();
	}


	public Entregador buscar(Entregador entregador){
		return repository.find(entregador.getId());
	}


	public EntregadorDto buscarPorId(Long id) {
		Optional<Entregador> entregadorOptional = repository.findById(id);

        if(entregadorOptional.isEmpty()) {
            return null;
        }

        return mapper.toDto(entregadorOptional.get());
	}


	public Entregador buscarPorNome(String nome){
		return repository.findByProperty("nome", nome);
	}


	public EntregadorDto salvarEntregador(EntregadorDto entregadorDto) {
		return mapper.toDto(
			repository.save(
				mapper.toEntregador(entregadorDto)
			)
		);
	}


	public void deletarEntregador(Entregador entregador) {
		repository.delete(entregador);
	}


	public void deletarEntregadorPorId(Long id) {
		repository.deleteById(id);
	}


	public void deletarEntregadores(List<Entregador> entregadores) {
		for (Entregador entregador : entregadores) {
			this.deletarEntregador(entregador);
		}
	}


	public List<Entregador> listarEntregadoresPorData(LocalDate data){
		return repository.listarEntregadoresPorData(data);
	}


	public List<Entregador> listarEntregadoresPorDataInicioFim(LocalDate dataInicio, LocalDate dataFim ){
		return repository.listarEntregadoresPorDataInicioFim(dataInicio,dataFim);
	}


	public Entregador cadastrarEntregador(Entregador entregador) {
		return repository.save(entregador);
	}


	public void realizarAcerto(Entregador entregadorSelecionado) {

		List<Acerto> acertos;
		List<Entrega> entregas;

		Date dataInicio = new Date();
		Date dataFim = new Date();

		StringBuffer textoVale;

		String QUEBRALINHA = System.lineSeparator();

		List<Vale> vales;


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

				copyValeToClipboard(textoVale.toString());

			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		} else {
			System.out.println("Atenção: Necessário cadastrar uma chave pix para continuar");
		}
	}


	public void copyValeToClipboard(String texto) {
		texto = texto.replace("\n", "\\n").replace("\r", "\\r").replace("'", "\\'");

		@SuppressWarnings("unused")
		String script = "navigator.clipboard.writeText('" + texto + "');";

		System.out.println("IMPLEMENTAR LOGICA");
	}
}
