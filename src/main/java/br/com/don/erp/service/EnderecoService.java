package br.com.don.erp.service;

import java.io.Serializable;
import java.util.List;

import br.com.don.erp.model.Address;

public class EnderecoService implements Serializable {

	private static final long serialVersionUID = 1L;

	public List<Address> buscarEndereco(Address endereco) {

		/*
		 * String URL =
		 * MessageFormat.format("https://viacep.com.br/ws/{0}/{1}/{2}/json/",
		 * endereco.getUf(),endereco.getLocalidade(),endereco.getLogradouro());
		 * ResteasyClient client = (ResteasyClient) ClientBuilder.newClient();
		 * ResteasyWebTarget target = client.target(URL);
		 * 
		 * Invocation.Builder request = target.request(); Response response =
		 * request.get(); List<Address> list = response.readEntity(new
		 * GenericType<List<Address>>() { });
		 */

		return null;

	}

}
