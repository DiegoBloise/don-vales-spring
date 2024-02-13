package br.com.don.erp.view;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.don.erp.model.Colaborador;
import br.com.don.erp.model.TipoColaborador;
import br.com.don.erp.model.Vale;
import br.com.don.erp.service.ColaboradorService;
import br.com.don.erp.service.ValeService;
import lombok.Getter;
import lombok.Setter;


@Named
@ViewScoped
public class GestaoColaboradorView implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	private List<TipoColaborador> tipoColaborador;

	@Getter
	@Setter
	private Colaborador colaborador;

	@Getter
	@Setter
	private Colaborador colaboradorSelecionado;

	@Getter
	private List<Colaborador> colaboradores;

	@Inject
	private ColaboradorService colaboradorService;

	@Inject
	private ValeService valeService;

	@Getter
	@Setter
	private String valorVale;

	@Getter
	@Setter
	private List<Vale> vales;

	@Getter
	@Setter
	private LocalDate dataVale;

	@Getter
	@Setter
	private Vale vale;

	private String nome;

	@Getter
	@Setter
	private BigDecimal totalVales = new BigDecimal(0);

	@Getter
	@Setter
	private Vale valeSelecionado;

	@Getter
	private StreamedContent file;

	private InputStream is;


	@PostConstruct
	public void init() {
		tipoColaborador = Arrays.asList(TipoColaborador.values());
		colaborador = new Colaborador();
		vale = new Vale();
		listarColaboradores();
		dataVale = LocalDate.now();
	}


	public void salvar() {
		colaborador.setNome(nome);

		if(colaboradorService.buscar(colaborador)!= null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Colaborador", "Colaborador já cadastrador"));
		} else {
			colaboradorService.salvar(colaborador);
			listarColaboradores();
			colaborador = new Colaborador();
			nome = new String();
		}
	}


	public void deletar() {
		System.out.println(colaboradorSelecionado.getNome());
		colaboradorService.deletar(colaboradorSelecionado);
		listarColaboradores();
	}


	public void salvarVale() {
		try {
			vale.setEntregador(colaboradorSelecionado.getNome());
	        vale.setValor(new BigDecimal(valorVale)); 

			valeService.salvar(vale);

			vales();

	        StringBuilder conteudo = new StringBuilder()
					.append(vale.getEntregador())
					.append(System.lineSeparator())
					.append(vale.getValor())
					.append(System.lineSeparator())
					.append(vale.getDataFormatada());
	    	is = new ByteArrayInputStream(conteudo.toString().getBytes());

	        vale = new Vale();
			valorVale = new String();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
    }


	public void download() {
		file = DefaultStreamedContent.builder()
                .name("vale.txt")
                .contentType("text/plain")
                .stream(() -> is)
                .build();

		System.out.println("Imprimindo...");
	}


	public void teste() {
		vale.setEntregador(colaboradorSelecionado.getNome()); 
		vale.setValor(new

		BigDecimal(valorVale));

		valeService.salvar(vale);

		vales();

		/* StringBuffer fileContent = new StringBuffer() .append(vale.getEntregador())
		 .append(Util.QUEBRA_LINHA) .append(vale.getValor())
		 .append(Util.QUEBRA_LINHA) .append(vale.getDataFormatada());

		 // Configurar o cabeçalho da resposta HTTP para indicar o download do arquivo
		 FacesContext facesContext = FacesContext.getCurrentInstance();
		 ExternalContext externalContext = facesContext.getExternalContext();
		 //externalContext.responseReset();
		 externalContext.setResponseContentType("text/plain");
		 externalContext.setResponseHeader("Content-Disposition",
		 "attachment; filename=\"vale.txt\"");

		 try (OutputStream outputStream = externalContext.getResponseOutputStream()) {
		 outputStream.write(fileContent.toString().getBytes()); outputStream.flush();
		 PrimeFaces.current().ajax().update("dialogs:vales");
		 facesContext.responseComplete();


		  } catch (IOException e) { e.printStackTrace(); }

		*/

		 //PrimeFaces.current().ajax().update("dialogs:vales");
	}


	public void deletarVale() {
		valeService.deletarVale(valeSelecionado);

		vales();
	}


	private void listarColaboradores() {
		colaboradores = colaboradorService.listar();
	}


	public void vales(){
		vale.setEntregador(colaboradorSelecionado.getNome());

		vales = valeService.buscarPorEntregador(colaboradorSelecionado.getNome());

		calcularTotalVale(vales);
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


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome.toUpperCase().trim();
	}
}
