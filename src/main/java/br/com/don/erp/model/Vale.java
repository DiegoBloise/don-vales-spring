package br.com.don.erp.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.com.don.erp.enums.TipoVale;
import br.com.don.erp.util.Util;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@ToString
@Table(name = "vales")
public class Vale implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Getter
	@Setter
	@EqualsAndHashCode.Include
	private Long id;

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
	@Enumerated(EnumType.STRING)
	private TipoVale tipo;

	@Getter
	@Setter
	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "colaborador_id")
    private Colaborador colaborador;


	public Vale() {
		this.valor = new BigDecimal(0);
	}


	@Transient
	public String getDataFormatada() {
		return Util.localDateFormatado(data);
	}
}
