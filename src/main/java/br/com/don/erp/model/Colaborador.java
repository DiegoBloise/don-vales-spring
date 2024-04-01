package br.com.don.erp.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.com.don.erp.util.Util;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@ToString
@Table(name = "colaboradores")
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
	@Column(name = "data_nascimento")
	private LocalDate dataNascimento;

	@Getter
	@Setter
	private Pix pix;


///////////////////////////////////////////////////////////


	@Getter
	@Setter
	@Column(name = "qtd_total_dias")
	private Integer qtdTotalDias;

	@Getter
	@Setter
	@Column(name = "qtd_entregas")
	private Integer qtdEntregas;

	@Getter
	@Setter
	@Column(name = "valor_total_entregas")
	private BigDecimal valorTotalEntregas;

	@Getter
	@Setter
	@Column(name = "valor_total_ifood")
	private BigDecimal valorTotalIfood;

	@Getter
	@Setter
	@Column(name = "valor_total_sem_desconto")
	private BigDecimal valorTotalSemDesconto;

	@Getter
	@Setter
	@Column(name = "valor_total_com_desconto")
	private BigDecimal valorTotalComDesconto;

	@Getter
	@Setter
	@Column(name = "valor_total_diarias")
	private BigDecimal valorTotalDiarias;

	@Getter
	@Setter
	@Column(name = "valor_total_vales")
	private BigDecimal valorTotalVales;

	@Getter
	@Setter
	@Column(name = "valor_saldo")
	private BigDecimal valorSaldo;


	public Colaborador() {

	}


	public void calcularAsParadas(List<Entrega> entregas) {

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


	public String gerarPayloadPix() {
		return this.pix.getPayload(this.nome, this.valorTotalComDesconto.toString(), "Sao Paulo", "PizzaDon");
	}
}