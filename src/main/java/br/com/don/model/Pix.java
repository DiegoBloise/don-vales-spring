package br.com.don.model;

import java.io.Serializable;
import java.text.MessageFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;

import br.com.don.enums.TipoChavePix;
import br.com.don.util.Jix;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "chaves_pix")
public class Pix extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

	private TipoChavePix tipo;

    private String chave;

	@OneToOne
	@JoinColumn(name = "colaborador_id")
	@JsonBackReference
    private Colaborador colaborador;


	public Pix() {

	}


	public Pix(Colaborador colaborador) {
		this.colaborador = colaborador;
	}


    public String getPayload(String nome, String valor, String cidade, String txtId) {
		return new Jix(nome, this.chave, valor, cidade, txtId).gerarPayload();
	}


	public String getChaveDebug() {
		return this.chave;
	}


	public String getChaveFormatada() {
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


	public void setChaveFormatada(String chavePix) {
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
