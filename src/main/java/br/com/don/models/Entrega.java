package br.com.don.models;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.don.enums.TipoStatusPagamento;
import br.com.don.util.Util;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
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
@Table(name = "entregas")
public class Entrega extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer pedido;

	private Double valor;

	private LocalDate data;

	private TipoStatusPagamento status;

	@ManyToOne
    @JoinColumn(name = "entregador_id")
	@JsonBackReference
    private Entregador entregador;

	@Transient
	@JsonIgnore
	private Integer qtd;


	public Entrega() {
		this.status = TipoStatusPagamento.PENDENTE;
	}


	public String getDataFormatada() {
		return Util.localDateFormatado(data);
	}
}
