package br.com.don.erp.model;

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
public class Colaborador {
	
	@Id
	@GeneratedValue
	@Getter
	@Setter
	@EqualsAndHashCode.Include
	private Long id;
		
	@Getter
	@Setter
	private String nome;
	
	@Getter
	@Setter
	private String telefone;
	
	@Getter
	@Setter
	private LocalDate dataNascimento;
	
	@Getter
	@Setter
	@Enumerated(EnumType.STRING)
	private TipoColaborador tipoColaborador;
	
	@Transient
	public String getDataNascimentoFormatada() {
		return Util.localDateFormatado(this.dataNascimento);
	}
	
}
