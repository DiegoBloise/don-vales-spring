package br.com.don.erp.view;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
import javax.servlet.ServletContext;

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
	private Double valorVale;
	
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
		}else {
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
	
	
	public StreamedContent getSalvarVale() {
		
		ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		String path = servletContext.getRealPath("/") + "vale.txt";
		
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(vale.getEntregador() + "\n" + vale.getValor() +  "\n" + vale.getDataFormatada());
            vale.setValor(new BigDecimal(valorVale)); 
            valeService.salvar(vale);
            vales();
            
            System.out.println("Arquivo gerado com sucesso.");
        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao gerar o arquivo: " + e.getMessage());
        }

        FacesContext.getCurrentInstance() .getExternalContext().getRequestServletPath();

		
		 StreamedContent  file = DefaultStreamedContent.builder()
		 .name("vale.txt").contentType("text/plain") .stream(()->
		 FacesContext.getCurrentInstance() .getExternalContext().getResourceAsStream(path)).build();
		 
        return file;
    }
	
	private void listarColaboradores() {
		colaboradores = colaboradorService.listar();
	}
	
	public void vales(){
		System.out.println("Listando vales " + colaboradorSelecionado.getNome());
		vale.setEntregador(colaboradorSelecionado.getNome());
		totalVales = new BigDecimal(0);
		vales  = valeService.buscarPorEntregador(colaboradorSelecionado.getNome());
		
		
		for (Vale vale : vales) {
			totalVales = vale.getValor().add(totalVales);
		}
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome.toUpperCase().trim();
	}
}
