package br.com.don.erp.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import br.com.don.erp.util.Util;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@ToString
public class Vale {
	
	public Vale() {
		this.valor = new BigDecimal(0);
	}
	
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
	private TipoVale tipoVale;
			
	@Transient
	public String getDataFormatada() {
		return Util.localDateFormatado(data);
	}
}
