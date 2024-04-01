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

import br.com.don.erp.enums.TipoChavePix;
import br.com.don.erp.enums.TipoColaborador;
import br.com.don.erp.model.Colaborador;
import br.com.don.erp.model.Vale;
import br.com.don.erp.service.ColaboradorService;
import br.com.don.erp.service.ValeService;
import lombok.Data;

@Data
@Named
@ViewScoped
public class ColaboradorView implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<TipoColaborador> tipoColaborador;

	private List<TipoChavePix> tipoChavePix;

	private Colaborador colaboradorSelecionado;

	private List<Colaborador> colaboradores;

	private List<Colaborador> colaboradoresSelecionados;

	private Vale valeSelecionado;

	private List<Vale> vales;

	private BigDecimal totalVales = new BigDecimal(0);

	private String valorVale;

	@Inject
	private ColaboradorService colaboradorService;

	@Inject
	private ValeService valeService;

	@Inject
	private FileDownloadView fileDownloadView;

	private List<Vale> filteredVales;

	private List<FilterMeta> filterBy;


	@PostConstruct
	public void init() {

		tipoColaborador = Arrays.asList(TipoColaborador.values());
		tipoChavePix = Arrays.asList(TipoChavePix.values());

		colaboradores = colaboradorService.getColaboradores();
        colaboradoresSelecionados = new ArrayList<>();

		vales = valeService.listar();
		valorVale = null;

		filterBy = new ArrayList<>();

		//.filterValue(new ArrayList<>(Arrays.asList(LocalDate.now().minusDays(7), LocalDate.now().plusDays(7))))
        filterBy.add(FilterMeta.builder()
                .field("data")
                .filterValue(new ArrayList<>(Arrays.asList(LocalDate.now(), LocalDate.now())))
                .matchMode(MatchMode.BETWEEN)
                .build());
	}


	public void adicionarVale(Colaborador colaborador) {
		System.out.println("TO AQUI");
		colaboradorSelecionado = colaborador;
		valeSelecionado = new Vale();
		valeSelecionado.setData(LocalDate.now());
	}


	public void salvarVale() {
		try {
			valeSelecionado.setColaborador(colaboradorSelecionado);
	        valeSelecionado.setValor(new BigDecimal(valorVale));

			valeService.salvar(valeSelecionado);

			vales();

	        StringBuilder conteudo = new StringBuilder()
					.append(valeSelecionado.getColaborador().getNome())
					.append(System.lineSeparator())
					.append(valeSelecionado.getValor())
					.append(System.lineSeparator())
					.append(valeSelecionado.getDataFormatada());

			InputStream inputStream = new ByteArrayInputStream(conteudo.toString().getBytes());

			fileDownloadView.setInputStream(inputStream);

		} catch (Exception e) {
			System.out.println(e.toString());
		}

		if (valeSelecionado.getId() == null) {
            vales.add(valeSelecionado);
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Vale Lançado", "Valor: R$ " + valorVale);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Vale Atualizado", "Valor: R$ " + valeSelecionado.getValor());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

		PrimeFaces.current().executeScript("PF('valeDialog').hide();");

		PrimeFaces.current().executeScript("PF('imprimeValeDialog').show();");
        PrimeFaces.current().ajax().update("form:dt-colaboradores");

        init();
    }


	public void deletarValeSelecionado() {
        vales.remove(valeSelecionado);

        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Vale Removido", "");
        FacesContext.getCurrentInstance().addMessage(null, msg);

        PrimeFaces.current().ajax().update("form:dt-colaboradores");

        valeService.deletarVale(valeSelecionado);

        valeSelecionado = null;
    }


	public void deletarVale() {
		valeService.deletarVale(valeSelecionado);

		vales();
	}


	public void adicionarColaborador() {
        colaboradorSelecionado = new Colaborador();
    }


    public void salvarColaborador() {
        if (colaboradorSelecionado.getId() == null) {

			/* if (isChavePixTelefone) {
				colaboradorSelecionado.setChavePix("+55" + colaboradorSelecionado.getChavePix());
			} */

			colaboradores.add(colaboradorSelecionado);

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Colaborador Cadastrado", "Colaborador: " + colaboradorSelecionado.getNome());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Colaborador Atualizado", "Colaborador: " + colaboradorSelecionado.getNome());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

        PrimeFaces.current().executeScript("PF('ColaboradorDialog').hide()");
        PrimeFaces.current().ajax().update("form:dt-colaboradores");

        colaboradorService.cadastrarColaborador(colaboradorSelecionado);

        init();
    }


    public void deletarColaboradorSelecionado() {
        colaboradores.remove(colaboradorSelecionado);

        colaboradoresSelecionados.remove(colaboradorSelecionado);

        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Colaborador Removido", "Colaborador: " + colaboradorSelecionado.getNome());
        FacesContext.getCurrentInstance().addMessage(null, msg);

        PrimeFaces.current().ajax().update("form:dt-colaboradores");

        colaboradorService.removerColaborador(colaboradorSelecionado);

        colaboradorSelecionado = null;
    }


	public void deletarColaboradoresSelecionados() {
        colaboradores.removeAll(colaboradoresSelecionados);

        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Colaboradores Removidos", "Todas os colaboradores selecionados foram removidos.");
        FacesContext.getCurrentInstance().addMessage(null, msg);

        PrimeFaces.current().ajax().update("form:dt-colaboradores");
        PrimeFaces.current().executeScript("PF('dtColaboradores').clearFilters()");

        colaboradorService.removerColaboradores(colaboradoresSelecionados);

        colaboradoresSelecionados.clear();
    }


	public void vales(){
		valeSelecionado.setColaborador(colaboradorSelecionado);

		vales = valeService.buscarPorColaborador(colaboradorSelecionado);

		calcularTotalVale(vales);
	}


	public List<Vale> getValesDoDia() {
		return valeService.listar();
	}


	public void calcularTotalVale(List<Vale> vales) {
		this.totalVales = new BigDecimal(0);

		for (Vale vale : vales) {
			this.totalVales = vale.getValor().add(this.totalVales);
		}
	}


	public List<Vale>valesDoColaborador(Colaborador colaborador) {
		return valeService.buscarPorColaborador(colaborador);
	}


	public BigDecimal totalDoColaborador(Colaborador colaborador) {
		BigDecimal total = new BigDecimal(0);

		List<Vale> vales = valeService.buscarPorColaborador(colaborador);

		for (Vale vale : vales) {
			total = vale.getValor().add(total);
		}

		return total;
	}


    public String getDeleteButtonMessage() {
        if (hasColaboradoresSelecionados()) {
            int size = colaboradoresSelecionados.size();
            return size > 1 ? size + " colaboradores selecionados" : "1 colaborador selecionado";
        }

        return "Remover";
    }


    public boolean hasColaboradoresSelecionados() {
        return colaboradoresSelecionados != null && !colaboradoresSelecionados.isEmpty();
    }
}
