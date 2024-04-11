package br.com.don.erp.view;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.MatchMode;

import br.com.don.erp.enums.TipoVale;
import br.com.don.erp.model.Colaborador;
import br.com.don.erp.model.Vale;
import br.com.don.erp.service.ColaboradorService;
import br.com.don.erp.service.ValeService;
import br.com.don.erp.session.UserFiles;
import lombok.Data;

@Data
@Named
@ViewScoped
public class ValeView implements Serializable {

	private static final long serialVersionUID = 1L;

	private Vale valeSelecionado;

	private Colaborador colaboradorSelecionado;

	private List<Vale> vales;

	private List<Vale> filteredVales;

	private List<FilterMeta> filterBy;

	private BigDecimal totalVales = new BigDecimal(0);

	@Inject
	private ValeService valeService;

	@Inject
	private ColaboradorService colaboradorService;

	@Inject
	private UserFiles userFiles;


    @PostConstruct
	public void init() {
		vales = valeService.listar();

		filterBy = new ArrayList<>();

		//.filterValue(new ArrayList<>(Arrays.asList(LocalDate.now().minusDays(7), LocalDate.now().plusDays(7))))
        filterBy.add(FilterMeta.builder()
                .field("data")
                .filterValue(new ArrayList<>(Arrays.asList(LocalDate.now(), LocalDate.now())))
                .matchMode(MatchMode.BETWEEN)
                .build());
	}


	public List<Colaborador> getColaboradores() {
		return colaboradorService.listarPorNome();
	}


	public List<String> getTiposVale() {
		return Arrays.asList(TipoVale.values()).stream()
		.map(TipoVale::getDescricao)
		.toList();
	}


	public void adicionarVale(Colaborador colaborador) {
		colaboradorSelecionado = colaborador;
		valeSelecionado = new Vale();
		valeSelecionado.setData(LocalDate.now());
	}


	public void salvarVale() {
		try {
			valeSelecionado.setColaborador(colaboradorSelecionado);

			vales();

	        prepararValeImpressao();

		} catch (Exception e) {
			System.out.println(e.toString());
		}

		if (valeSelecionado.getId() == null) {
            vales.add(valeSelecionado);

			valeService.cadastrarVale(valeSelecionado);

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Vale Lançado", "Valor: R$ " + valeSelecionado.getValor().toString().replace(".", ","));
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        else {
			valeService.atualizarVale(valeSelecionado);

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Vale Atualizado", "Valor: R$ " + valeSelecionado.getValor().toString().replace(".", ","));
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

		PrimeFaces.current().executeScript("PF('valeDialog').hide();");

		PrimeFaces.current().ajax().update("@root:@id(dt-vales)");

        init();
    }


	public void deletarValeSelecionado() {
        vales.remove(valeSelecionado);

        valeService.deletarVale(valeSelecionado);

		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Vale Removido", "");
        FacesContext.getCurrentInstance().addMessage(null, msg);

        PrimeFaces.current().ajax().update("@root:@id(dt-vales)");

        valeSelecionado = null;
    }


	public void vales(){
		valeSelecionado.setColaborador(colaboradorSelecionado);

		vales = valeService.buscarPorColaborador(colaboradorSelecionado);

		calcularTotalVale(vales);
	}


	public void calcularTotalVale(List<Vale> vales) {
		this.totalVales = new BigDecimal(0);

		for (Vale vale : vales) {
			this.totalVales = vale.getValor().add(this.totalVales);
		}
	}


	public void cancelarVale() {
		valeSelecionado = null;
	}


	public void prepararValeImpressao() {
		StringBuilder conteudo = new StringBuilder()
		.append(valeSelecionado.getColaborador().getNome())
		.append(System.lineSeparator())
		.append(valeSelecionado.getValor())
		.append(System.lineSeparator())
		.append(valeSelecionado.getDataFormatada());

		InputStream inputStream = new ByteArrayInputStream(conteudo.toString().getBytes());

		userFiles.setInputStream(inputStream);
	}
}