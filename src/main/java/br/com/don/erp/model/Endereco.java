package br.com.don.erp.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Endereco implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String cep;
	
    private String logradouro;
    
    private String complemento;
    
    private String bairro;
    
    private String localidade;
    
    private String uf;
    
    private String ibge;
    
    private String gia;
    
    private String ddd;
    
    private String siafi;

}
