package br.com.don.erp.model;

import java.io.Serializable;
import java.text.MessageFormat;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.don.erp.enums.TipoChavePix;
import br.com.don.erp.util.Jix;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Entity
@Table(name = "chaves_pix")
public class Pix implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
	@GeneratedValue
	@Getter
	@Setter
	@EqualsAndHashCode.Include
	private Long id;

	@Getter
	@Setter
	@Enumerated(EnumType.STRING)
	private TipoChavePix tipo;

    private String chave;


    public String getPayload(String nome, String valor, String cidade, String txtId) {
		return new Jix(nome, this.chave, valor, cidade, txtId).gerarPayload();
	}


	public String getChaveDebug() {
		return this.chave;
	}


	public String getChave() {
		if(this.chave == null || this.chave.isEmpty()) {
			return this.chave;
		}

		switch (this.tipo) {
			case CELULAR:
				return MessageFormat.format("({0}) {1}-{2}",
					this.chave.substring(3, 5),
					this.chave.substring(5, 10),
					this.chave.substring(10));
			case CPF:
				return MessageFormat.format("{0}.{1}.{2}-{3}",
					this.chave.substring(0, 3),
					this.chave.substring(3, 6),
					this.chave.substring(6, 9),
					this.chave.substring(9));
			case CNPJ:
				return MessageFormat.format("{0}.{1}.{2}/{3}-{4}",
					this.chave.substring(0, 2),
					this.chave.substring(2, 5),
					this.chave.substring(5, 8),
					this.chave.substring(8, 12),
					this.chave.substring(12));
			default:
				return this.chave;
		}
	}


	public void setChave(String chavePix) {
		switch (this.tipo) {
			case CELULAR:
				this.chave = "+55" + chavePix.replaceAll("[^0-9]", "");
				break;
			case CPF:
			case CNPJ:
				this.chave = chavePix.replaceAll("[^0-9]", "");
				break;
			default:
				this.chave = chavePix;
				break;
		}
	}
}
