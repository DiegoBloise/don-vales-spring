package br.com.don.erp.model;


import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.com.don.erp.util.Util;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "entregas")
public class Entrega implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Getter
	@Setter
	@EqualsAndHashCode.Include
	private Long id;

	@Getter
	@Setter
	private Integer pedido;

	@Getter
	@Setter
	private String entregador;

	@Getter
	@Setter
	private BigDecimal valor;

	@Getter
	@Setter
	private LocalDate data;

	@Getter
	@Setter
	@Transient
	private Integer qtd;


	public String getDataFormatada() {
		return Util.localDateFormatado(data);
	}
}
