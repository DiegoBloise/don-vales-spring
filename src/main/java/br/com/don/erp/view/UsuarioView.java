package br.com.don.erp.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.push.Push;
import javax.faces.push.PushContext;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.don.erp.model.Address;
import br.com.don.erp.model.Usuario;
import br.com.don.erp.service.EnderecoService;
import lombok.Getter;
import lombok.Setter;


@Named
@ApplicationScoped
public class UsuarioView implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Inject
	private EnderecoService enderecoService;
	
	@Getter
	private List<String> cidades;
	
	@Setter
	@Getter
	private Address endereco = new Address();
	
	@Getter
	@Inject
	private Usuario usuario;
	
	@Getter
	private List<Address> enderecos;
	
	@Inject
	@Push(channel = "push")
	private PushContext push;

	@PostConstruct
	public void init() {
		cidades = new ArrayList<String>();
		cidades.add("São Paulo");
		cidades.add("Ferraz de Vasconcelos");
		cidades.add("Itaquaquecetuba");
		cidades.add("Poá");

	}
	public void buscarEndereco() {
		endereco.setUf("SP");
		enderecos = enderecoService.buscarEndereco(endereco);
		push.send("updateTabela");
	}
	
}
