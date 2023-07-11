package br.com.don.erp.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Empresa implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Getter
	@Setter
	@EqualsAndHashCode.Include
	private Long id;

	@Getter
	@Setter
	@Column(name = "nome_fantasia", nullable = false, length = 80)
	private String nomeFantasia;

	@Getter
	@Setter
	@Column(name = "razao_social", nullable = false, length = 120)
	private String razaoSocial;

	@Getter
	@Setter
	@Column(nullable = false, length = 18)
	private String cnpj;

	@Getter
	@Setter
	@Temporal(TemporalType.DATE)
	@Column(name = "data_fundacao")
	private Date dataFundacao;

	@Getter
	@Setter
	@Enumerated(EnumType.STRING)
	private TipoEmpresa tipo;

		
}