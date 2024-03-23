package br.com.don.erp.model;

import java.io.Serializable;
import java.text.MessageFormat;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import br.com.don.erp.enums.TipoChavePix;
import br.com.don.erp.enums.TipoColaborador;
import br.com.don.erp.util.Jix;
import br.com.don.erp.util.Util;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@ToString
public class Colaborador implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Getter
	@Setter
	@EqualsAndHashCode.Include
	private Long id;

	@Getter
	@Setter
	private String nome;

	private String telefone;

	@Getter
	@Setter
	private LocalDate dataNascimento;

	private String chavePix;

	@Getter
	@Setter
	@Enumerated(EnumType.STRING)
	private TipoChavePix tipoChavePix;

	@Getter
	@Setter
	@Enumerated(EnumType.STRING)
	private TipoColaborador tipoColaborador;


	public Colaborador() {
		this.tipoChavePix = TipoChavePix.CELULAR;
	}


	@Transient
	public String getDataNascimentoFormatada() {
		return Util.localDateFormatado(this.dataNascimento);
	}


	public String getTelefone() {
        if (this.telefone == null) {
            return this.telefone;
        } else {
            return MessageFormat.format("({0}) {1}-{2}",
                this.telefone.substring(0, 2),
                this.telefone.substring(2, 7),
                this.telefone.substring(7));
        }
    }


	public void setTelefone(String telefone) {
        this.telefone = telefone.replaceAll("[^0-9]", "");
    }


	public String getPixPayload(String valor) {
		return new Jix(this.nome, this.chavePix, valor, "Sao Paulo", "PizzaDon").gerarPayload();
	}

	public String getChavePixDebug() {
		return this.chavePix;
	}

	public String getChavePix() {
		if (this.chavePix == null) {
			return this.chavePix;
		} else if (this.tipoChavePix.equals(TipoChavePix.CELULAR)) {
			return MessageFormat.format("({0}) {1}-{2}",
					this.chavePix.substring(3, 5),
					this.chavePix.substring(5, 10),
					this.chavePix.substring(10));
		} else if (this.tipoChavePix.equals(TipoChavePix.CPF)) {
			return MessageFormat.format("{0}.{1}.{2}-{3}",
				this.chavePix.substring(0, 3),
				this.chavePix.substring(3, 6),
				this.chavePix.substring(6, 9),
				this.chavePix.substring(9));
		} else if (this.tipoChavePix.equals(TipoChavePix.CNPJ)) {
			return MessageFormat.format("{0}.{1}.{2}/{3}-{4}",
				this.chavePix.substring(0, 2),
				this.chavePix.substring(2, 5),
				this.chavePix.substring(5, 8),
				this.chavePix.substring(8, 12),
				this.chavePix.substring(12));
		} else {
			return this.chavePix;
		}
	}


	public void setChavePix(String chavePix) {
		if (this.tipoChavePix.equals(TipoChavePix.CELULAR)) {
			this.chavePix = "+55" + chavePix.replaceAll("[^0-9]", "");
		} else {
			this.chavePix = chavePix.replaceAll("[^0-9]", "");
		}
	}
}
