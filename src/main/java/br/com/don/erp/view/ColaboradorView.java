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
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.MatchMode;
import org.primefaces.model.StreamedContent;

import br.com.don.erp.model.Colaborador;
import br.com.don.erp.model.TipoColaborador;
import br.com.don.erp.model.Vale;
import br.com.don.erp.service.ColaboradorService;
import br.com.don.erp.service.ValeService;
import lombok.Data;

@Data
@Named
@SessionScoped
public class ColaboradorView implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<TipoColaborador> tipoColaborador;

	private Colaborador colaboradorSelecionado;

	private List<Colaborador> colaboradores;

	private List<Colaborador> colaboradoresSelecionados;

	private Vale valeSelecionado;

	private List<Vale> vales;

	private LocalDate dataVale;

	private BigDecimal totalVales = new BigDecimal(0);

	private StreamedContent file;

	private InputStream is;

	private String valorVale;

	private Boolean isChavePixTelefone;

	@Inject
	private ColaboradorService colaboradorService;

	@Inject
	private ValeService valeService;


	private List<Vale> filteredVales;
	private List<FilterMeta> filterBy;


	@PostConstruct
	public void init() {
		dataVale = LocalDate.now();

		inicializarObjetos();

		filterBy = new ArrayList<>();

		//.filterValue(new ArrayList<>(Arrays.asList(LocalDate.now().minusDays(7), LocalDate.now().plusDays(7))))
        filterBy.add(FilterMeta.builder()
                .field("data")
                .filterValue(new ArrayList<>(Arrays.asList(LocalDate.now(), LocalDate.now())))
                .matchMode(MatchMode.BETWEEN)
                .build());
	}


	public void setDate() {
		valeSelecionado.setData(dataVale);
	}


	public void salvarVale() {
		try {
			valeSelecionado.setEntregador(colaboradorSelecionado.getNome());
	        valeSelecionado.setValor(new BigDecimal(valorVale));

			valeService.salvar(valeSelecionado);

			vales();

	        StringBuilder conteudo = new StringBuilder()
					.append(valeSelecionado.getEntregador())
					.append(System.lineSeparator())
					.append(valeSelecionado.getValor())
					.append(System.lineSeparator())
					.append(valeSelecionado.getDataFormatada());
	    	is = new ByteArrayInputStream(conteudo.toString().getBytes());
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

        inicializarObjetos();
    }


	public void download() {
		file = DefaultStreamedContent.builder()
                .name("vale.txt")
                .contentType("text/plain")
                .stream(() -> is)
                .build();

		System.out.println("Imprimindo...");
	}


	public void deletarVale() {
		valeService.deletarVale(valeSelecionado);

		vales();
	}


	public void vales(){
		valeSelecionado.setEntregador(colaboradorSelecionado.getNome());

		vales = valeService.buscarPorEntregador(colaboradorSelecionado.getNome());

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
		return valeService.buscarPorEntregador(colaborador.getNome());
	}


	public BigDecimal totalDoColaborador(Colaborador colaborador) {
		BigDecimal total = new BigDecimal(0);

		List<Vale> vales = valeService.buscarPorEntregador(colaborador.getNome());

		for (Vale vale : vales) {
			total = vale.getValor().add(total);
		}

		return total;
	}


	public void inicializarObjetos() {
		tipoColaborador = Arrays.asList(TipoColaborador.values());

		colaboradores = colaboradorService.getColaboradores();
        colaboradorSelecionado = new Colaborador();
        colaboradoresSelecionados = new ArrayList<>();

		valeSelecionado = new Vale();
		vales = valeService.listar();
		valorVale = null;

		isChavePixTelefone = null;
    }


    public void adicionar() {
        colaboradorSelecionado = new Colaborador();
    }


    public void salvar() {
        if (colaboradorSelecionado.getId() == null) {

			if (isChavePixTelefone) {
				colaboradorSelecionado.setChavePix("+55" + colaboradorSelecionado.getChavePix());
			}

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

        inicializarObjetos();
    }


    public void deletarSelecionado() {
        colaboradores.remove(colaboradorSelecionado);

        colaboradoresSelecionados.remove(colaboradorSelecionado);

        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Colaborador Removido", "Colaborador: " + colaboradorSelecionado.getNome());
        FacesContext.getCurrentInstance().addMessage(null, msg);

        PrimeFaces.current().ajax().update("form:dt-colaboradores");

        colaboradorService.removerColaborador(colaboradorSelecionado);

        colaboradorSelecionado = null;
    }


	public void deletarValeSelecionado() {
        vales.remove(valeSelecionado);

        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Vale Removido", "");
        FacesContext.getCurrentInstance().addMessage(null, msg);

        PrimeFaces.current().ajax().update("form:dt-colaboradores");

        valeService.deletarVale(valeSelecionado);

        valeSelecionado = null;
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


    public void deletarColaboradoresSelecionados() {
        colaboradores.removeAll(colaboradoresSelecionados);

        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Colaboradores Removidos", "Todas os colaboradores selecionados foram removidos.");
        FacesContext.getCurrentInstance().addMessage(null, msg);

        PrimeFaces.current().ajax().update("form:dt-colaboradores");
        PrimeFaces.current().executeScript("PF('dtColaboradores').clearFilters()");

        colaboradorService.removerColaboradores(colaboradoresSelecionados);

        colaboradoresSelecionados.clear();
    }
}
